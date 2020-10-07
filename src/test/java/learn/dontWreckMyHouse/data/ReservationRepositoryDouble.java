package learn.dontWreckMyHouse.data;

import learn.dontWreckMyHouse.models.Host;
import learn.dontWreckMyHouse.models.Reservation;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileNotFoundException;
import java.util.List;

public class ReservationRepositoryDouble implements ReservationRepository{

    private static final String HEADER = "id,start_date,end_date,guest_id,total";
    private final String reservationTestDirectory;
    private final GuestRepositoryDouble guestRepositoryDouble;

    public ReservationRepositoryDouble(@Value("./data/reservations-test") String reservationTestDirectory, GuestRepositoryDouble guestRepositoryDouble) {
        this.reservationTestDirectory = reservationTestDirectory;
        this.guestRepositoryDouble = guestRepositoryDouble;
    }

    @Override
    public Reservation add(Reservation reservation, Host host) throws FileNotFoundException, DataException {
        return null;
    }

    @Override
    public boolean update(Reservation reservation, Host host) throws DataException, FileNotFoundException {
        return false;
    }

    @Override
    public boolean deleteById(int reservationId, Host host) throws FileNotFoundException, DataException {
        return false;
    }

    @Override
    public List<Reservation> findReservationsForHost(Host host) throws FileNotFoundException {
        return null;
    }
}
