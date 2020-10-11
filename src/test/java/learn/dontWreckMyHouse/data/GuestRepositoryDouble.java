package learn.dontWreckMyHouse.data;

import learn.dontWreckMyHouse.models.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository{

    private static final String HEADER = "guest_id,first_name,last_name,email,phone,state";
    private String filePath;
    private ReservationRepository reservationRepository;

//    public GuestRepositoryDouble(@Value("./data/guests-test.csv") String filePath, GuestRepository guestRepository) {
//        this.filePath = filePath;
//        this.guestRepository = guestRepository;
//    }

    @Autowired
    public void setFilePath(@Value("./data/guests-test.csv") String filePath) {
        this.filePath = filePath;
    }

    @Autowired
    public void setReservationRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Guest> findAll() {

        ArrayList<Guest> guests = new ArrayList<>();

        // THIS LINE IS NOT WORKING
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Skip header
            reader.readLine();

            for(String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if(fields.length == 6) {
                    guests.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return guests;
    }

    @Override
    public Guest findById(int id) {
        return findAll().stream()
                .filter(g -> g.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Guest add(Guest guest) throws DataException {
        if (guest == null) {
            return null;
        }

        List<Guest> all = findAll();

        int nextId = all.stream()
                .mapToInt(Guest::getId)
                .max()
                .orElse(0) + 1;

        guest.setId(nextId);

        all.add(guest);
        writeAll(all);

        return guest;
    }

    @Override
    public Guest update(Guest guest) throws DataException {
        List<Guest> allGuests = findAll();
        for (int i = 0; i < allGuests.size(); i++) {
            if (allGuests.get(i).getId() == guest.getId()) {
                allGuests.set(i, guest);
                writeAll(allGuests);
                return guest;
            }
        }
        // If not found
        return null;
    }

    @Override
    public Guest delete(Guest guest) throws DataException {
        List<Guest> guests = findAll();
        for(int i = 0; i < guests.size(); i++) {
            if(guests.get(i).getId() == guest.getId()) {
                guests.remove(i);
                writeAll(guests);
                return guest;
            }
        }
        return null;
    }

    private String serialize(Guest guest) {
        return String.format("%s,%s,%s,%s,%s,%s",
                guest.getId(),
                guest.getFirstName(),
                guest.getLastName(),
                guest.getEmail(),
                guest.getPhone(),
                guest.getState());
    }

    private Guest deserialize(String[] fields) {
        Guest result = new Guest();
        result.setId(Integer.parseInt(fields[0]));
        result.setFirstName(fields[1]);
        result.setLastName(fields[2]);
        result.setEmail(fields[3]);
        result.setPhone(fields[4]);
        result.setState(fields[5]);
        return result;
    }

    protected void writeAll(List<Guest> guests) throws DataException {
        try (PrintWriter writer = new PrintWriter(filePath)) {

            writer.println(HEADER);

            if (guests == null) {
                return;
            }

            for (Guest guest : guests) {
                writer.println(serialize(guest));
            }

        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }
}
