package learn.dontWreckMyHouse.data;

import learn.dontWreckMyHouse.models.Host;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    private static final String HEADER = "id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate";
    private final String filePath;
    private final ReservationRepositoryDouble reservationDirectoryDouble;

    public HostRepositoryDouble(@Value("./data/hosts-test.csv") String filePath, ReservationRepositoryDouble reservationDirectoryDouble) {
        this.filePath = filePath;
        this.reservationDirectoryDouble = reservationDirectoryDouble;
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
        return null;
    }

    @Override
    public Host add(Host host) throws DataException {
        return null;
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

}
