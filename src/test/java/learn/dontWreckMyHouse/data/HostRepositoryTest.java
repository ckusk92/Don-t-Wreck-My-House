package learn.dontWreckMyHouse.data;

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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HostRepositoryTest {

    static final String SEED_PATH = "./data/hosts-seed.csv";
    static final String TEST_PATH = "./data/hosts-test.csv";
    static final String TEST_DIRECTORY_PATH = "./data/reservations-test";
    static final String GUEST_TEST_PATH = "./data/guests-test.csv";

    //HostFileRepository repository = new HostFileRepository(TEST_PATH, new ReservationFileRepository(TEST_DIRECTORY_PATH, new GuestFileRepository(GUEST_TEST_PATH)));
    HostFileRepository repository = new HostFileRepository();

    public HostRepositoryTest() {
        repository.setFilePath(TEST_PATH);
    }

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_PATH);
        Path testPath = Paths.get(TEST_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() {
        List<Host> hosts = repository.findAll();
        assertNotNull(hosts);
        assertEquals(10, hosts.size());
    }

    @Test
    void shouldFindFader() {
        Host fader = repository.findById("b4f38829-c663-48fc-8bf3-7fca47a7ae70");
        assertNotNull(fader);
        assertEquals("Fader", fader.getLastName());
        assertEquals("mfader2@amazon.co.jp", fader.getEmail());
        assertEquals("(501) 2490895", fader.getPhone());
        assertEquals("99208 Morning Parkway", fader.getAddress());
        assertEquals("North Little Rock", fader.getCity());
        assertEquals("AR", fader.getState());
        assertEquals(72118, fader.getPostalCode());
        assertEquals(BigDecimal.valueOf(451.00).setScale(2, RoundingMode.HALF_UP), fader.getStandardRate().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(563.75).setScale(2, RoundingMode.HALF_UP), fader.getWeekendRate().setScale(2,RoundingMode.HALF_UP));
    }

    @Test
    void shouldNotFindBadId() {
        Host bad = repository.findById("b4f38829-c663-48fc-8bf3-7fca47a7ae77");
        assertNull(bad);
    }

    @Test
    void shouldAdd() throws DataException {
        Host host = new Host();
        host.setLastName("Kusk");
        host.setEmail("fake@fake.com");
        host.setPhone("(398) 8675309");
        host.setAddress("1646 Prospect Ave");
        host.setCity("Milwaukee");
        host.setState("WI");
        host.setPostalCode(53202);
        host.setStandardRate(BigDecimal.valueOf(630));
        host.setWeekendRate(BigDecimal.valueOf(820.25));
        host.setId(java.util.UUID.randomUUID().toString());

        Host result = repository.add(host);
        assertEquals(host, result);
        assertEquals(11, repository.findAll().size());
    }

//    @Test
//    void shouldFindThirteenReservationsForHost() throws FileNotFoundException {
//        Host host = repository.findById("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");
//        List<Reservation> reservations = repository.findReservationsForHost(host);
//        assertNotNull(reservations);
//        assertEquals(13, reservations.size());
//    }
//
//    @Test
//    void shouldReturnNull() throws FileNotFoundException {
//        Host host = repository.findById("3e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");
//        List<Reservation> reservations = repository.findReservationsForHost(host);
//        assertNull(reservations);
//    }
}
