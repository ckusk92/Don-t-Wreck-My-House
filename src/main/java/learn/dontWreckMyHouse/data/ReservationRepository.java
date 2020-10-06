package learn.dontWreckMyHouse.data;

import learn.dontWreckMyHouse.models.Host;
import learn.dontWreckMyHouse.models.Reservation;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    //Reservation add(int guestId, Host host, LocalDate startDate, LocalDate endDate, BigDecimal total) throws FileNotFoundException, DataException;

    Reservation add(Reservation reservation, Host host) throws FileNotFoundException, DataException;

    public boolean update(Reservation reservation, Host host) throws DataException, FileNotFoundException;

    public boolean deleteById(int reservationId, Host host) throws FileNotFoundException, DataException;

    List<Reservation> findReservationsForHost(Host host) throws FileNotFoundException;
}
