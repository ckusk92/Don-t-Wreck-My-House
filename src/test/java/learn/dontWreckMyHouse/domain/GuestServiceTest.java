package learn.dontWreckMyHouse.domain;

import learn.dontWreckMyHouse.data.*;
import learn.dontWreckMyHouse.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GuestServiceTest {

    static final String SEED_PATH = "./data/guests-seed.csv";
    static final String TEST_PATH = "./data/guests-test.csv";

    GuestService service = new GuestService(new GuestFileRepository(TEST_PATH));

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

}
