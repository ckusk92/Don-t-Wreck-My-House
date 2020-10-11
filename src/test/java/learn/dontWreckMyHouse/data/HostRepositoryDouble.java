package learn.dontWreckMyHouse.data;

import learn.dontWreckMyHouse.models.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    private static final String HEADER = "id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate";
    private String filePath;

    @Autowired
    public void setFilePath(@Value("./data/hosts-test.csv") String filePath) {
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
    public Host add(Host host) throws DataException {

        if (host == null) {
            return null;
        }

        List<Host> all = findAll();

        host.setId(java.util.UUID.randomUUID().toString());

        all.add(host);
        writeAll(all);
        String newFileName = String.format("./data/reservations-test/%s.csv", host.getId());

        // Create the file for new host
        File newHost = new File(newFileName);

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
            if(hosts.get(i).getId() == host.getId()) {
                String deleteFileName = String.format("./data/reservations-test/%s.csv", host.getId());
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
