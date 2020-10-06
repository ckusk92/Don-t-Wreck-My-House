package learn.dontWreckMyHouse.data;

import learn.dontWreckMyHouse.models.Guest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GuestFileRepository implements GuestRepository{

    private static final String HEADER = "guest_id,first_name,last_name,email,phone,state";
    private final String filePath;

    public GuestFileRepository(@Value("./data/guests.csv") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Guest> findAll() {
        ArrayList<Guest> guests = new ArrayList<>();

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
