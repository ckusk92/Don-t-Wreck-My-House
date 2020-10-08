package learn.dontWreckMyHouse.domain;

import learn.dontWreckMyHouse.data.DataException;
import learn.dontWreckMyHouse.data.GuestRepositoryDouble;
import learn.dontWreckMyHouse.data.ReservationRepository;
import learn.dontWreckMyHouse.data.ReservationRepositoryDouble;
import learn.dontWreckMyHouse.models.Guest;
import learn.dontWreckMyHouse.models.Host;
import learn.dontWreckMyHouse.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

public class ReservationServiceTest {

    static final String SEED_FILE_PATH = "./data/reservations-test/2e25f6f7-3ef0-4f38-8a1a-2b5eea81409d.csv";
    static final String TEST_FILE_PATH = "./data/reservations-test/2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c.csv";
    static final String TEST_DIR_PATH = "./data/reservations-test";
    static final String GUEST_TEST_PATH = "./data/guests-test.csv";

    //ReservationService service = new ReservationService(new ReservationRepositoryDouble(TEST_FILE_PATH, new GuestRepositoryDouble(GUEST_TEST_PATH)));
    // Running into problem of directory being null
    ReservationService service = new ReservationService(new ReservationRepositoryDouble(TEST_DIR_PATH, new GuestRepositoryDouble(GUEST_TEST_PATH)));

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void hostRosenkrancShouldFindThirteenReservations() throws FileNotFoundException {
        Host rosenkranc = new Host();
        rosenkranc.setLastName("Rosenkrank");
        rosenkranc.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");
        List<Reservation> reservations = service.reservationsForHost(rosenkranc);
        assertNotNull(reservations);
        assertEquals(13, reservations.size());
    }

    @Test
    void shouldAdd() throws FileNotFoundException, DataException {
        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");
        host.setStandardRate(BigDecimal.valueOf(20));
        host.setWeekendRate(BigDecimal.valueOf(30));
        Guest guest = new Guest();
        guest.setId(3);

        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2024,5,20));
        reservation.setEndDate(LocalDate.of(2024,5,24));
        reservation.setGuest(guest);

        Result<Reservation> result = service.add(reservation, host);
        assertNotNull(result);
        assertEquals(14, result.getPayload().getId());
    }

    @Test
    void shouldNotAddStartAfterEndDate() throws FileNotFoundException, DataException {
        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");
        host.setStandardRate(BigDecimal.valueOf(20));
        host.setWeekendRate(BigDecimal.valueOf(30));
        Guest guest = new Guest();
        guest.setId(3);

        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2024,5,28));
        reservation.setEndDate(LocalDate.of(2024,5,24));
        reservation.setGuest(guest);

        Result<Reservation> result = service.add(reservation, host);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddOverlappingExisingReservation() throws FileNotFoundException, DataException {
        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");
        host.setStandardRate(BigDecimal.valueOf(20));
        host.setWeekendRate(BigDecimal.valueOf(30));
        Guest guest = new Guest();
        guest.setId(3);

        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2021,5,1));
        reservation.setEndDate(LocalDate.of(2021,5,15));
        reservation.setGuest(guest);

        Result<Reservation> result = service.add(reservation, host);
        assertFalse(result.isSuccess());
    }
}
