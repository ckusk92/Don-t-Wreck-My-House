package learn.dontWreckMyHouse.data;

import learn.dontWreckMyHouse.models.Guest;
import learn.dontWreckMyHouse.models.Host;
import learn.dontWreckMyHouse.models.Reservation;

import java.io.FileNotFoundException;
import java.util.List;

public interface HostRepository {

    List<Host> findAll();

    Host findById(String id);

    Host add(Host host) throws DataException;

    List<Reservation> findReservationsForHost(Host host) throws FileNotFoundException;
}
