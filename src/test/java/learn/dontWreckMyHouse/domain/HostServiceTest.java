package learn.dontWreckMyHouse.domain;

import learn.dontWreckMyHouse.data.*;
import learn.dontWreckMyHouse.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HostServiceTest {

    static final String SEED_PATH = "./data/hosts-seed.csv";
    static final String TEST_PATH = "./data/hosts-test.csv";
    static final String TEST_DIRECTORY_PATH = "./data/reservations-test";
    static final String GUEST_TEST_PATH = "./data/guests-test.csv";

    HostRepositoryDouble repository = new HostRepositoryDouble();
    HostService service = new HostService();

    public HostServiceTest() {
        repository.setFilePath(TEST_PATH);
        service.setRepository(repository);
    }

    @BeforeEach
    void setup() throws IOException {
        Files.copy(Paths.get(SEED_PATH),
                Paths.get(TEST_PATH),
                StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindTwoRLastNames() {
        List<Host> rLastNames = service.findByLastName("R");
        assertNotNull(rLastNames);
        assertEquals(2, rLastNames.size());
    }
}
