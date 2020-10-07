package learn.dontWreckMyHouse.data;

import learn.dontWreckMyHouse.models.Host;
import org.springframework.beans.factory.annotation.Value;

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
        return null;
    }

    @Override
    public Host findById(String id) {
        return null;
    }

    @Override
    public Host add(Host host) throws DataException {
        return null;
    }
}
