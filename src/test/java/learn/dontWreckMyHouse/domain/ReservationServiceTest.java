package learn.dontWreckMyHouse.domain;

import learn.dontWreckMyHouse.data.*;
import learn.dontWreckMyHouse.models.Guest;
import learn.dontWreckMyHouse.models.Host;
import learn.dontWreckMyHouse.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    //ReservationService service = new ReservationService(new ReservationRepositoryDouble(TEST_DIR_PATH, new GuestRepositoryDouble(GUEST_TEST_PATH)));
    //ReservationService service = new ReservationService(new ReservationFileRepository());
    ReservationRepositoryDouble reservationRepository = new ReservationRepositoryDouble();
    GuestRepositoryDouble guestRepository = new GuestRepositoryDouble();
    ReservationService service = new ReservationService();

    public ReservationServiceTest() {
        reservationRepository.setReservationTestDirectory(TEST_DIR_PATH);
        guestRepository.setFilePath(GUEST_TEST_PATH);
        reservationRepository.setGuestRepository(guestRepository);
        guestRepository.setReservationRepository(reservationRepository);
        service.setRepository(reservationRepository);
    }

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

    @Test
    void shouldFindThreeReservationsForGuest() throws FileNotFoundException {
        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");
        host.setStandardRate(BigDecimal.valueOf(20));
        host.setWeekendRate(BigDecimal.valueOf(30));
        Guest guest = new Guest();
        guest.setId(2);

        List<Reservation> guestReservations = service.findForHostAndGuest(host, guest);
        assertNotNull(guestReservations);
        assertEquals(3, guestReservations.size());
    }

    @Test
    void shouldFindNoReservations() throws FileNotFoundException {
        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");
        host.setStandardRate(BigDecimal.valueOf(20));
        host.setWeekendRate(BigDecimal.valueOf(30));
        Guest guest = new Guest();
        guest.setId(9);

        List<Reservation> guestReservations = service.findForHostAndGuest(host, guest);
        assertNotNull(guestReservations);
        assertEquals(0, guestReservations.size());
    }

    @Test
    void shouldUpdate() throws FileNotFoundException, DataException {
        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");
        host.setStandardRate(BigDecimal.valueOf(20));
        host.setWeekendRate(BigDecimal.valueOf(30));
        Guest guest = new Guest();
        guest.setId(3);

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setStartDate(LocalDate.of(2020,10,14));
        reservation.setEndDate(LocalDate.of(2020,10,16));
        reservation.setGuest(guest);

        LocalDate originalStart = LocalDate.of(2020,10,13);
        LocalDate originalEnd = LocalDate.of(2020, 10, 17);

        Result<Reservation> result = service.update(reservation, host, originalStart, originalEnd);
        assertNotNull(result);
        assertEquals(14, result.getPayload().getStartDate().getDayOfMonth());
        assertEquals(16, result.getPayload().getEndDate().getDayOfMonth());
        assertEquals(BigDecimal.valueOf(40.00).setScale(2, RoundingMode.HALF_UP), result.getPayload().getTotal().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void shouldNotUpdateOverlapExistingReservation() throws FileNotFoundException, DataException {
        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");
        host.setStandardRate(BigDecimal.valueOf(20));
        host.setWeekendRate(BigDecimal.valueOf(30));
        Guest guest = new Guest();
        guest.setId(3);

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setStartDate(LocalDate.of(2020,10,29));
        reservation.setEndDate(LocalDate.of(2020,11,2));
        reservation.setGuest(guest);

        LocalDate originalStart = LocalDate.of(2020,10,13);
        LocalDate originalEnd = LocalDate.of(2020, 10, 17);

        Result<Reservation> result = service.update(reservation, host, originalStart, originalEnd);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotUpdateStartAfterEnd() throws FileNotFoundException, DataException {
        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");
        host.setStandardRate(BigDecimal.valueOf(20));
        host.setWeekendRate(BigDecimal.valueOf(30));
        Guest guest = new Guest();
        guest.setId(3);

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setStartDate(LocalDate.of(2020,11,29));
        reservation.setEndDate(LocalDate.of(2020,11,25));
        reservation.setGuest(guest);

        LocalDate originalStart = LocalDate.of(2020,10,13);
        LocalDate originalEnd = LocalDate.of(2020, 10, 17);

        Result<Reservation> result = service.update(reservation, host, originalStart, originalEnd);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotUpdatePastReservation() throws FileNotFoundException, DataException {
        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");
        host.setStandardRate(BigDecimal.valueOf(20));
        host.setWeekendRate(BigDecimal.valueOf(30));
        Guest guest = new Guest();
        guest.setId(3);

        Reservation reservation = new Reservation();
        reservation.setId(6);
        reservation.setStartDate(LocalDate.of(2020,11,29));
        reservation.setEndDate(LocalDate.of(2020,11,25));
        reservation.setGuest(guest);

        LocalDate originalStart = LocalDate.of(2020,9,12);
        LocalDate originalEnd = LocalDate.of(2020, 9, 19);

        Result<Reservation> result = service.update(reservation, host, originalStart, originalEnd);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotUpdateOngoingReservationStart() throws FileNotFoundException, DataException {
        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");
        host.setStandardRate(BigDecimal.valueOf(20));
        host.setWeekendRate(BigDecimal.valueOf(30));
        Guest guest = new Guest();
        guest.setId(3);

        Reservation reservation = new Reservation();
        reservation.setId(6);
        reservation.setStartDate(LocalDate.of(2020,10,10));
        reservation.setEndDate(LocalDate.of(2020,10,13));
        reservation.setGuest(guest);

        LocalDate originalStart = LocalDate.of(2020,10,7);
        LocalDate originalEnd = LocalDate.of(2020, 10, 13);

        Result<Reservation> result = service.update(reservation, host, originalStart, originalEnd);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldDelete() throws FileNotFoundException, DataException {
        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");

        Reservation reservation = new Reservation();
        reservation.setId(5);
        reservation.setStartDate(LocalDate.of(2021,6,9));
        reservation.setEndDate(LocalDate.of(2021,6,12));

        int sizeBefore = service.reservationsForHost(host).size();
        Result<Reservation> result = service.remove(reservation, host);
        int sizeAfter = service.reservationsForHost(host).size();

        assertTrue(result.isSuccess());
        assertEquals(1, sizeBefore - sizeAfter);
    }

    @Test
    void shouldNotDeletePastReservation() throws FileNotFoundException, DataException {
        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");

        Reservation reservation = new Reservation();
        reservation.setId(8);
        reservation.setStartDate(LocalDate.of(2020,5,22));
        reservation.setEndDate(LocalDate.of(2020,5,28));

        int sizeBefore = service.reservationsForHost(host).size();
        Result<Reservation> result = service.remove(reservation, host);
        int sizeAfter = service.reservationsForHost(host).size();

        assertFalse(result.isSuccess());
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void shouldNotDeleteOngoingReservation() throws FileNotFoundException, DataException {
        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");

        Reservation reservation = new Reservation();
        reservation.setId(9);
        reservation.setStartDate(LocalDate.of(2020,10,7));
        reservation.setEndDate(LocalDate.of(2020,10,17));

        int sizeBefore = service.reservationsForHost(host).size();
        Result<Reservation> result = service.remove(reservation, host);
        int sizeAfter = service.reservationsForHost(host).size();

        assertFalse(result.isSuccess());
        assertEquals(sizeBefore, sizeAfter);
    }

}
