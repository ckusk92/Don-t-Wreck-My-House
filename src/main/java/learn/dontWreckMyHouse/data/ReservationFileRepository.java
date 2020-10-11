package learn.dontWreckMyHouse.data;

import learn.dontWreckMyHouse.models.Guest;
import learn.dontWreckMyHouse.models.Host;
import learn.dontWreckMyHouse.models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationFileRepository implements ReservationRepository {

    private static final String HEADER = "id,start_date,end_date,guest_id,total";
    private String reservationDirectory;
    private GuestRepository guestRepository;

    @Autowired
    public void setReservationDirectory(@Value("./data/reservations") String reservationDirectory) {
        this.reservationDirectory = reservationDirectory;
    }

    @Autowired
    public void setGuestRepository(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Override
    public List<Reservation> findReservationsForHost(String hostId) throws FileNotFoundException {

        if(hostId == null) {
            return null;
        }

        ArrayList<Reservation> reservations = new ArrayList<>();

        String hostFileName = String.format("%s.csv", hostId);
        boolean hostFileFound = false;
        File dir = new File(reservationDirectory);
        File[] directoryListing = dir.listFiles();
        if(directoryListing != null) {
            // Loop through every file in reservations directory
            for(File child : directoryListing) {
                try(BufferedReader reader = new BufferedReader(new FileReader(child))) {
                    String fileName = child.getName();
                    // Grab filename minus the .csv
                    String[] fileNameArray = fileName.split("\\.", -1);
                    // When we hit a match
                    if(fileName.equalsIgnoreCase(hostFileName)) {
                        // Read header line first
                        reader.readLine();
                        for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                            String[] fields = line.split(",", -1);
                            if(fields.length == 5) {
                                reservations.add(deserializeReservation(fields, fileNameArray[0]));
                            } else {
                                continue;
                            }
                        }
                        // Should only be one file per host
                        hostFileFound = true;
                        break;
                    }
                } catch (IOException ex) {
                    // don't throw on read
                }
            }
        } else {
            return null;
        }

        if(!hostFileFound) {
            return null;
        } else {
            return reservations;
        }
    }

    @Override
    public Reservation add(Reservation reservation, Host host) throws FileNotFoundException, DataException {
        if (reservation == null) {
            return null;
        }

        List<Reservation> allForHost = findReservationsForHost(host.getId());

        int nextId = allForHost.stream()
                .mapToInt(Reservation::getId)
                .max()
                .orElse(0) + 1;

        reservation.setId(nextId);
        allForHost.add(reservation);
        writeAll(allForHost, host.getId());

        return reservation;
    }

    @Override
    public Reservation update(Reservation reservation, Host host) throws DataException, FileNotFoundException {
        List<Reservation> allByHost = findReservationsForHost(host.getId());
        for (int i = 0; i < allByHost.size(); i++) {
            if (allByHost.get(i).getId() == reservation.getId()) {
                allByHost.set(i, reservation);
                writeAll(allByHost, host.getId());
                return reservation;
            }
        }
        return null;
    }

    @Override
    public Reservation deleteReservation(Reservation reservation, String hostId) throws FileNotFoundException, DataException {
        List<Reservation> allByHost = findReservationsForHost(hostId);
        for(int i = 0; i < allByHost.size(); i++) {
            if(allByHost.get(i).getId() == reservation.getId()) {
                allByHost.remove(i);
                writeAll(allByHost, hostId);
                return reservation;
            }
        }
        return null;
    }

    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%s,%s",
                reservation.getId(),
                String.format("%s-%s-%s", reservation.getStartDate().getYear(), reservation.getStartDate().getMonthValue(), reservation.getStartDate().getDayOfMonth()),
                String.format("%s-%s-%s", reservation.getEndDate().getYear(), reservation.getEndDate().getMonthValue(), reservation.getEndDate().getDayOfMonth()),
                reservation.getGuest().getId(),
                reservation.getTotal().setScale(2, RoundingMode.HALF_UP));
    }

    private Reservation deserializeReservation(String[] fields, String hostId) {
        Reservation result = new Reservation();
        result.setId(Integer.parseInt(fields[0]));

        String[] startDateArray = fields[1].split("-", -1);
        int startDateYear = Integer.parseInt(startDateArray[0]);
        int startDateMonth = Integer.parseInt(startDateArray[1]);
        int startDateDay = Integer.parseInt(startDateArray[2]);
        result.setStartDate(LocalDate.of(startDateYear,startDateMonth,startDateDay));

        String[] endDateArray = fields[2].split("-", -1);
        int endDateYear = Integer.parseInt(endDateArray[0]);
        int endDateMonth = Integer.parseInt(endDateArray[1]);
        int endDateDay = Integer.parseInt(endDateArray[2]);
        result.setEndDate(LocalDate.of(endDateYear,endDateMonth,endDateDay));

        result.setGuest(guestRepository.findById(Integer.parseInt(fields[3])));  // Only instance where guestRepository is needed
        result.setTotal(BigDecimal.valueOf(Double.parseDouble(fields[4])).setScale(2, RoundingMode.HALF_UP));
        result.setHostId(hostId);

        return result;
    }

    protected void writeAll(List<Reservation> reservations, String hostId) throws DataException {

        // Need it to write to reservations-test for testing function
        String filePath = String.format("%s/%s.csv", reservationDirectory, hostId);

        try (PrintWriter writer = new PrintWriter(filePath)) {

            writer.println(HEADER);

            if (reservations == null) {
                return;
            }

            for (Reservation reservation : reservations) {
                writer.println(serialize(reservation));
            }

        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    // Used primarily for the deletion of host and guest to delete belonging reservations
    public List<Reservation> findAll() {
        ArrayList<Reservation> result = new ArrayList<>();
        File dir = new File(reservationDirectory);
        File[] directoryListing = dir.listFiles();
        if(directoryListing != null) {
            for(File child : directoryListing) {
                // Need to confirm that directory is correct
                try (BufferedReader reader = new BufferedReader(new FileReader(child))) {
                    String fileName = child.getName();
                    // Grab filename minus the .csv
                    String[] fileNameArray = fileName.split("\\.", -1);
                    // Get date from file name for delimiter method
                    reader.readLine(); // read header
                    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                        String[] fields = line.split(",", -1);
                        if (fields.length == 5) {
                            // Second parameter needs to date as Forage's are stored in separate files
                            result.add(deserializeReservation(fields, fileNameArray[0]));
                        }
                    }
                } catch (IOException ex) {
                    // don't throw on read
                }
            }
        }
        return result;
    }
}
