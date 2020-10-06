package learn.dontWreckMyHouse.data;

import learn.dontWreckMyHouse.models.Guest;
import learn.dontWreckMyHouse.models.Host;
import learn.dontWreckMyHouse.models.Reservation;
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
    private final String reservationDirectory;

    public ReservationFileRepository(@Value("./data/reservations") String reservationDirectory) {
        this.reservationDirectory = reservationDirectory;
    }

//    @Override
//    public Reservation add(int guestId, Host host, LocalDate startDate, LocalDate endDate, BigDecimal total) throws FileNotFoundException, DataException {
//        Reservation reservation = new Reservation();
//
//        List<Reservation> allForHost = findReservationsForHost(host);
//
//        int nextId = allForHost.stream()
//                .mapToInt(Reservation::getId)
//                .max()
//                .orElse(0) + 1;
//        reservation.setGuestId(nextId);
//        reservation.setStartDate(startDate);
//        reservation.setEndDate(endDate);
//        reservation.setGuestId(guestId);
//        reservation.setTotal(total);
//
//        allForHost.add(reservation);
//        writeAll(allForHost, host);
//        return reservation;
//    }

    @Override
    public Reservation add(Reservation reservation, Host host) throws FileNotFoundException, DataException {
        if (reservation == null) {
            return null;
        }

        List<Reservation> allForHost = findReservationsForHost(host);

        int nextId = allForHost.stream()
                .mapToInt(Reservation::getId)
                .max()
                .orElse(0) + 1;

        allForHost.add(reservation);
        writeAll(allForHost, host);

        return reservation;
    }

    @Override
    public List<Reservation> findReservationsForHost(Host host) throws FileNotFoundException {

        if(host == null) {
            return null;
        }

        ArrayList<Reservation> reservations = new ArrayList<>();

        String hostFileName = String.format("%s.csv", host.getId());

        File dir = new File(reservationDirectory);
        File[] directoryListing = dir.listFiles();
        if(directoryListing != null) {
            // Loop through every file in reservations directory
            for(File child : directoryListing) {
                try(BufferedReader reader = new BufferedReader(new FileReader(child))) {
                    String fileName = child.getName();
                    // When we hit a match
                    if(fileName.equalsIgnoreCase(hostFileName)) {
                        // Read header line first
                        reader.readLine();
                        for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                            String[] fields = line.split(",", -1);
                            if(fields.length == 5) {
                                reservations.add(deserializeReservation(fields));
                            } else {
                                return null;
                            }
                        }
                        // Should only be one file per host
                        break;
                    }
                } catch (IOException ex) {
                    // don't throw on read
                }
            }
        } else {
            return null;
        }

        return reservations;
    }

    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%s,%s",
                reservation.getId(),
                String.format("%s-%s-%s", reservation.getStartDate().getYear(), reservation.getStartDate().getMonthValue(), reservation.getStartDate().getDayOfMonth()),
                String.format("%s-%s-%s", reservation.getEndDate().getYear(), reservation.getEndDate().getMonthValue(), reservation.getEndDate().getDayOfMonth()),
                reservation.getGuestId(),
                reservation.getTotal().setScale(2, RoundingMode.HALF_UP));
    }

    private Reservation deserializeReservation(String[] fields) {
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

        result.setGuestId(Integer.parseInt(fields[3]));
        result.setTotal(BigDecimal.valueOf(Double.parseDouble(fields[4])));

        return result;
    }

    protected void writeAll(List<Reservation> reservations, Host host) throws DataException {

        String filePath = String.format("./data/reservations/%s.csv", host.getId());

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
}
