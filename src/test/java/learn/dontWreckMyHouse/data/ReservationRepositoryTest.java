package learn.dontWreckMyHouse.data;

import learn.dontWreckMyHouse.models.Guest;
import learn.dontWreckMyHouse.models.Host;
import learn.dontWreckMyHouse.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.ReverbType;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationRepositoryTest {

    static final String SEED_FILE_PATH = "./data/reservations-test/2e25f6f7-3ef0-4f38-8a1a-2b5eea81409d.csv";
    static final String TEST_FILE_PATH = "./data/reservations-test/2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c.csv";
    static final String TEST_DIR_PATH = "./data/reservations-test";
    static final String GUEST_TEST_PATH = "./data/guests-test.csv";

    ReservationFileRepository repository = new ReservationFileRepository(TEST_DIR_PATH, new GuestFileRepository(GUEST_TEST_PATH));

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindThirteenReservations() throws FileNotFoundException {
        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");
        List<Reservation> reservations = repository.findReservationsForHost(host);
        assertNotNull(reservations);
        assertEquals(13, reservations.size());
    }

    @Test
    void shouldReturnNull() throws FileNotFoundException {
        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409e");
        List<Reservation> reservations = repository.findReservationsForHost(host);
        assertNull(reservations);
    }

    @Test
    void shouldAdd() throws FileNotFoundException, DataException {
        Guest guest = new Guest();
        guest.setId(40);

        Reservation reservation = new Reservation();
        reservation.setId(14);
        reservation.setStartDate(LocalDate.of(2020, 10, 6));
        reservation.setEndDate(LocalDate.of(2020, 10, 14));
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.valueOf(500));

        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");

        Reservation result = repository.add(reservation, host);
        assertEquals(reservation, result);
        assertEquals(13, repository.findReservationsForHost(host).size());
    }

    @Test
    void shouldUpdate() throws DataException, FileNotFoundException {
        Guest guest = new Guest();
        guest.setId(8);

        Reservation reservation = new Reservation();
        reservation.setId(5);
        reservation.setStartDate(LocalDate.of(2015, 10, 6));
        reservation.setEndDate(LocalDate.of(2015, 10, 14));
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.valueOf(525));

        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");

        Reservation result = repository.update(reservation, host);
        assertEquals(result, reservation);
        assertEquals(8, repository.findReservationsForHost(host).get(4).getGuest().getId());
    }

    @Test
    void shouldNotUpdate() throws DataException, FileNotFoundException {
        Guest guest = new Guest();
        guest.setId(42);

        Reservation reservation = new Reservation();
        reservation.setId(200);
        reservation.setStartDate(LocalDate.of(2015, 10, 6));
        reservation.setEndDate(LocalDate.of(2015, 10, 14));
        reservation.setGuest(guest);
        reservation.setTotal(BigDecimal.valueOf(525));

        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");

        assertNull(repository.update(reservation, host));;
    }

    @Test
    void shouldDelete() throws FileNotFoundException, DataException {
        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");

        assertTrue(repository.deleteById(5, host));
    }

    @Test
    void shouldNotDelete() throws FileNotFoundException, DataException {
        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");

        assertFalse(repository.deleteById(50000, host));
    }
}
