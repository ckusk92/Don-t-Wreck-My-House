package learn.dontWreckMyHouse.data;

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

public class ReservationFileRepositoryTest {

    static final String SEED_FILE_PATH = "./data/reservations-test/2e25f6f7-3ef0-4f38-8a1a-2b5eea81409d.csv";
    static final String TEST_FILE_PATH = "./data/reservations-test/2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c.csv";
    static final String TEST_DIR_PATH = "./data/reservations-test";

    ReservationFileRepository repository = new ReservationFileRepository(TEST_DIR_PATH);

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
    void shouldReturnZeroSizeList() throws FileNotFoundException {
        Host host = new Host();
        host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409e");
        List<Reservation> reservations = repository.findReservationsForHost(host);
        assertNotNull(reservations);
        assertEquals(0, repository.findReservationsForHost(host).size());
    }

    @Test
    void shouldAdd() throws FileNotFoundException, DataException {
       Reservation reservation = new Reservation();
       reservation.setId(14);
       reservation.setStartDate(LocalDate.of(2020,10,6));
       reservation.setEndDate(LocalDate.of(2020,10,14));
       reservation.setGuestId(40);
       reservation.setTotal(BigDecimal.valueOf(500));

       Host host = new Host();
       host.setId("2e25f6f7-3ef0-4f38-8a1a-2b5eea81409c");

       Reservation result = repository.add(reservation, host);
       assertEquals(reservation, result);
       assertEquals(13, repository.findReservationsForHost(host).size());

}
