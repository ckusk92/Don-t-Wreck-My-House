package learn.dontWreckMyHouse.data;

import learn.dontWreckMyHouse.models.Guest;
import learn.dontWreckMyHouse.models.Host;
import learn.dontWreckMyHouse.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GuestRepositoryTest {

    static final String SEED_PATH = "./data/guests-seed.csv";
    static final String TEST_PATH = "./data/guests-test.csv";
    static final String TEST_REPOSITORY_PATH = "./data/reservation-test";
    static final int NEXT_ID = 11;

    GuestRepositoryDouble repository = new GuestRepositoryDouble();
    ReservationFileRepository reservationFileRepository = new ReservationFileRepository();

    public GuestRepositoryTest(){
        repository.setFilePath(TEST_PATH);
        repository.setReservationRepository(reservationFileRepository);
        reservationFileRepository.setReservationDirectory(TEST_REPOSITORY_PATH);
        reservationFileRepository.setGuestRepository(repository);
    }

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_PATH);
        Path testPath = Paths.get(TEST_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() {
        List<Guest> guests = repository.findAll();
        assertNotNull(guests);
        assertEquals(10, guests.size());
    }

    @Test
    void shouldFindBerta() {
        Guest berta = repository.findById(5);
        assertNotNull(berta);
        assertEquals("Berta", berta.getFirstName());
        assertEquals("Seppey", berta.getLastName());
        assertEquals("bseppey4@yahoo.com", berta.getEmail());
        assertEquals("(202) 2668098", berta.getPhone());
        assertEquals("DC", berta.getState());
    }

    @Test
    void shouldNotFindBadId() {
        Guest bad = repository.findById(1000);
        assertNull(bad);
    }

    @Test
    void shouldAdd() throws DataException {

        Guest guest = new Guest();
        guest.setFirstName("Charles");
        guest.setLastName("Kusk");
        guest.setEmail("fake@fake.com");
        guest.setPhone("(398) 8675309");
        guest.setState("WI");
        guest.setId(NEXT_ID);

        Guest result = repository.add(guest);
        assertEquals(guest, result);
        assertEquals(11, repository.findAll().size());
    }

    @Test
    void shouldUpdate() throws DataException {
        Guest guest = new Guest();
        guest.setFirstName("Charles");
        guest.setLastName("Kusk");
        guest.setEmail("fake@fake.com");
        guest.setPhone("(398) 8675309");
        guest.setState("WI");
        guest.setId(1);

        Guest result = repository.update(guest);
        assertEquals(guest, result);
        assertEquals("Charles", repository.findById(1).getFirstName());
    }

    @Test
    void shouldNotUpdate() throws DataException {
        Guest guest = new Guest();
        guest.setFirstName("Charles");
        guest.setLastName("Kusk");
        guest.setEmail("fake@fake.com");
        guest.setPhone("(398) 8675309");
        guest.setState("WI");
        guest.setId(1000);

        Guest result = repository.update(guest);
        assertNull(result);
        assertEquals("Sullivan", repository.findById(1).getFirstName());
    }
}
