package learn.dontWreckMyHouse.data;

import learn.dontWreckMyHouse.models.Guest;
import learn.dontWreckMyHouse.models.Host;

import java.util.List;

public interface GuestRepository {

    List<Guest> findAll();

    Guest findById(int id);

    Guest add(Guest guest) throws DataException;

    Guest update(Guest guest) throws DataException;

    Guest delete(Guest guest);
}
