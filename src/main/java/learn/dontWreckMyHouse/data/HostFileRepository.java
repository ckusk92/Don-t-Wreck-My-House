package learn.dontWreckMyHouse.data;

import learn.dontWreckMyHouse.models.Guest;
import learn.dontWreckMyHouse.models.Host;
import learn.dontWreckMyHouse.models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HostFileRepository implements HostRepository {

    private static final String HEADER = "id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate";
    private String filePath;

    @Autowired
    public void setFilePath(@Value("./data/hosts.csv") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Host> findAll() {
        ArrayList<Host> hosts = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Skip header
            reader.readLine();

            for(String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if(fields.length == 10) {
                    hosts.add(deserializeHost(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return hosts;
    }

    @Override
    public Host findById(String id) {
        return findAll().stream()
                .filter(g -> g.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Host add(Host host) throws DataException, IOException {

        if (host == null) {
            return null;
        }

        List<Host> all = findAll();

        host.setId(java.util.UUID.randomUUID().toString());

        all.add(host);
        writeAll(all);
        String newFileName = String.format("./data/reservations/%s.csv", host.getId());

        // Create the file for new host
        File newHost = new File(newFileName);
        newHost.createNewFile();

        return host;
    }

    @Override
    public Host update(Host host) throws DataException {
        List<Host> allHosts = findAll();
        for (int i = 0; i < allHosts.size(); i++) {
            if (allHosts.get(i).getId().equalsIgnoreCase(host.getId())) {
                allHosts.set(i, host);
                writeAll(allHosts);
                return host;
            }
        }

        // If not found
        return null;
    }

    @Override
    public Host delete(Host host) throws DataException, FileNotFoundException {

        List<Host> hosts = findAll();
        for(int i = 0; i < hosts.size(); i++) {
            if(hosts.get(i).getId().equalsIgnoreCase(host.getId())) {
                String deleteFileName = String.format("./data/reservations/%s.csv", host.getId());
                File deleteFile = new File(deleteFileName);
                deleteFile.delete();
                hosts.remove(i);
                writeAll(hosts);
                return host;
            }
        }
        return null;
    }

    private String serialize(Host host) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                host.getId(),
                host.getLastName(),
                host.getEmail(),
                host.getPhone(),
                host.getAddress(),
                host.getCity(),
                host.getState(),
                host.getPostalCode(),
                host.getStandardRate(),
                host.getWeekendRate());
    }

    private Host deserializeHost(String[] fields) {
        Host result = new Host();
        result.setId(fields[0]);
        result.setLastName(fields[1]);
        result.setEmail(fields[2]);
        result.setPhone(fields[3]);
        result.setAddress(fields[4]);
        result.setCity(fields[5]);
        result.setState(fields[6]);
        result.setPostalCode(Integer.parseInt(fields[7]));
        result.setStandardRate(BigDecimal.valueOf(Double.parseDouble(fields[8])));
        result.setWeekendRate(BigDecimal.valueOf(Double.parseDouble(fields[9])));
        return result;
    }

//    // Needed as a Host will have an array of Reservations
//    private Reservation deserializeReservation(String[] fields) {
//        Reservation result = new Reservation();
//        result.setId(Integer.parseInt(fields[0]));
//
//        String[] startDateArray = fields[1].split("-", -1);
//        int startDateYear = Integer.parseInt(startDateArray[0]);
//        int startDateMonth = Integer.parseInt(startDateArray[1]);
//        int startDateDay = Integer.parseInt(startDateArray[2]);
//        result.setStartDate(LocalDate.of(startDateYear,startDateMonth,startDateDay));
//
//        String[] endDateArray = fields[2].split("-", -1);
//        int endDateYear = Integer.parseInt(endDateArray[0]);
//        int endDateMonth = Integer.parseInt(endDateArray[1]);
//        int endDateDay = Integer.parseInt(endDateArray[2]);
//        result.setStartDate(LocalDate.of(startDateYear,startDateMonth,startDateDay));
//
//        result.setGuestId(Integer.parseInt(fields[3]));
//        result.setTotal(BigDecimal.valueOf(Double.parseDouble(fields[4])));
//
//        return result;
//    }

    protected void writeAll(List<Host> hosts) throws DataException {
        try (PrintWriter writer = new PrintWriter(filePath)) {

            writer.println(HEADER);

            if (hosts == null) {
                return;
            }

            for (Host host : hosts) {
                writer.println(serialize(host));
            }

        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }
}
