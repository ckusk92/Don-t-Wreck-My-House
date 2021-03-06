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

    Reservation update(Reservation reservation, Host host) throws DataException, FileNotFoundException;

    Reservation deleteReservation(Reservation reservation, String hostId) throws FileNotFoundException, DataException;

    List<Reservation> findReservationsForHost(String hostId) throws FileNotFoundException;

    List<Reservation> findAll();

}
