package learn.dontWreckMyHouse.domain;

import learn.dontWreckMyHouse.data.*;
import learn.dontWreckMyHouse.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GuestServiceTest {

    static final String SEED_PATH = "./data/guests-seed.csv";
    static final String TEST_PATH = "./data/guests-test.csv";
    static final String RESERVATION_TEST_DIR = "./data/reservations-test";

    //GuestService service = new GuestService(new GuestFileRepository(TEST_PATH));
    GuestRepositoryDouble guestRepository = new GuestRepositoryDouble();
    ReservationRepositoryDouble reservationRepository = new ReservationRepositoryDouble();
    //GuestService service = new GuestService(new GuestFileRepository());
    GuestService service = new GuestService();

    public GuestServiceTest() {
        guestRepository.setFilePath(TEST_PATH);
        guestRepository.setReservationRepository(reservationRepository);
        reservationRepository.setReservationTestDirectory(RESERVATION_TEST_DIR);
        reservationRepository.setGuestRepository(guestRepository);
        service.setRepository(guestRepository);
    }

    @BeforeEach
    void setup() throws IOException {
        Files.copy(Paths.get(SEED_PATH),
                Paths.get(TEST_PATH),
                StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindThreeGLastNames() {
        List<Guest> guests = service.findByLastName("G");
        assertNotNull(guests);
        assertEquals(3, guests.size());
    }

    @Test
    void shouldFindNoLastNames() {
        List<Guest> guests = service.findByLastName("Z");
        assertNotNull(guests);
        assertEquals(0, guests.size());
    }

    @Test
    void shouldAdd() throws DataException {
        Guest guest = new Guest();
        guest.setFirstName("Charles");
        guest.setLastName("Kusk");
        guest.setEmail("fake@fake.com");

        int sizeBefore = service.findAll().size();
        Result<Guest> result = service.add(guest);
        int sizeAfter = service.findAll().size();

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(1, sizeAfter - sizeBefore);
    }

    @Test
    void shouldNotAddNoLastName() throws DataException {
        Guest guest = new Guest();
        guest.setFirstName("Charles");
        guest.setEmail("fake@fake.com");

        int sizeBefore = service.findAll().size();
        Result<Guest> result = service.add(guest);
        int sizeAfter = service.findAll().size();

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(sizeAfter, sizeBefore);
    }

    @Test
    void shouldNotAddDuplicateEmail() throws DataException {
        Guest guest = new Guest();
        guest.setFirstName("Charles");
        guest.setLastName("Kusk");
        guest.setEmail("kcurson5@youku.com");

        int sizeBefore = service.findAll().size();
        Result<Guest> result = service.add(guest);
        int sizeAfter = service.findAll().size();

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(sizeAfter, sizeBefore);
    }

    @Test
    void shouldUpdate() throws DataException {
        Guest guest = new Guest();
        guest.setFirstName("Charles");
        guest.setLastName("Kusk");
        guest.setEmail("fake@fake.com");
        guest.setId(1);

        Result<Guest> result = service.update(guest);

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Charles", service.findById(1).getFirstName());
    }

    @Test
    void shouldNotUpdateNotFoundId() throws DataException {
        Guest guest = new Guest();
        guest.setFirstName("Charles");
        guest.setLastName("Kusk");
        guest.setEmail("fake@fake.com");
        guest.setId(1000);

        Result<Guest> result = service.update(guest);

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Sullivan", service.findById(1).getFirstName());
    }

    @Test
    void shouldNotUpdateDuplicateEmail() throws DataException {
        Guest guest = new Guest();
        guest.setFirstName("Charles");
        guest.setLastName("Kusk");
        guest.setEmail("tcarncross2@japanpost.jp");
        guest.setId(1);

        Result<Guest> result = service.update(guest);

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Sullivan", service.findById(1).getFirstName());
    }

    @Test
    void shouldDelete() throws FileNotFoundException, DataException {
        Guest guest = new Guest();
        guest.setId(2);

        int sizeBefore = service.findAll().size();
        Result<Guest> result = service.remove(guest);
        int sizeAfter = service.findAll().size();

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNull(service.findById(2));
        assertEquals(1, sizeBefore - sizeAfter);
    }

    @Test
    void shouldNotDelete() throws FileNotFoundException, DataException {
        Guest guest = new Guest();
        guest.setId(100);

        int sizeBefore = service.findAll().size();
        Result<Guest> result = service.remove(guest);
        int sizeAfter = service.findAll().size();

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(sizeBefore, sizeAfter);
    }
}
