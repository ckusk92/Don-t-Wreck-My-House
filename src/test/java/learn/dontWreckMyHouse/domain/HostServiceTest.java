package learn.dontWreckMyHouse.domain;

import learn.dontWreckMyHouse.data.*;
import learn.dontWreckMyHouse.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
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

    @Test
    void shouldAdd() throws DataException, IOException {
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

        Result<Host> result = service.add(host);
        assertTrue(result.isSuccess());
        assertEquals(11, service.findAll().size());
    }

    @Test
    void shouldNotAddBlankAddress() throws DataException, IOException {
        Host host = new Host();
        host.setLastName("Kusk");
        host.setEmail("fake@fake.com");
        host.setPhone("(398) 8675309");
        host.setAddress("");
        host.setCity("Milwaukee");
        host.setState("WI");
        host.setPostalCode(53202);
        host.setStandardRate(BigDecimal.valueOf(630));
        host.setWeekendRate(BigDecimal.valueOf(820.25));
        host.setId(java.util.UUID.randomUUID().toString());

        Result<Host> result = service.add(host);
        assertFalse(result.isSuccess());
        assertEquals(10, service.findAll().size());
    }

    @Test
    void shouldNotAddDuplicateEmail() throws DataException, IOException {
        Host host = new Host();
        host.setLastName("Kusk");
        host.setEmail("hvalasek5@fastcompany.com");
        host.setPhone("(398) 8675309");
        host.setAddress("1646 Prospect Ave");
        host.setCity("Milwaukee");
        host.setState("WI");
        host.setPostalCode(53202);
        host.setStandardRate(BigDecimal.valueOf(630));
        host.setWeekendRate(BigDecimal.valueOf(820.25));
        host.setId(java.util.UUID.randomUUID().toString());

        Result<Host> result = service.add(host);
        assertFalse(result.isSuccess());
        assertEquals(10, service.findAll().size());
    }

    @Test
    void shouldUpdate() throws DataException {
        Host host = new Host();
        host.setLastName("Kusk");
        host.setEmail("fake@fake.com");
        host.setPhone("(398) 8675309");
        host.setAddress("123 Fake Address");
        host.setCity("Milwaukee");
        host.setState("WI");
        host.setPostalCode(53202);
        host.setStandardRate(BigDecimal.valueOf(630));
        host.setWeekendRate(BigDecimal.valueOf(820.25));
        host.setId("3edda6bc-ab95-49a8-8962-d50b53f84b15");

        Result<Host> result = service.update(host);
        assertTrue(result.isSuccess());
        assertEquals("Kusk", service.findById("3edda6bc-ab95-49a8-8962-d50b53f84b15").getLastName());
    }

    @Test
    void shouldNotUpdateEmptyEmail() throws DataException {
        Host host = new Host();
        host.setLastName("Kusk");
        host.setEmail("");
        host.setPhone("(398) 8675309");
        host.setAddress("123 Fake Address");
        host.setCity("Milwaukee");
        host.setState("WI");
        host.setPostalCode(53202);
        host.setStandardRate(BigDecimal.valueOf(630));
        host.setWeekendRate(BigDecimal.valueOf(820.25));
        host.setId("3edda6bc-ab95-49a8-8962-d50b53f84b15");

        Result<Host> result = service.update(host);
        assertFalse(result.isSuccess());
        assertEquals("eyearnes0@sfgate.com", service.findById("3edda6bc-ab95-49a8-8962-d50b53f84b15").getEmail());
    }

    @Test
    void shouldNotUpdateDuplicateEmail() throws DataException {
        Host host = new Host();
        host.setLastName("Kusk");
        host.setEmail("krhodes1@posterous.com");
        host.setPhone("(398) 8675309");
        host.setAddress("123 Fake Address");
        host.setCity("Milwaukee");
        host.setState("WI");
        host.setPostalCode(53202);
        host.setStandardRate(BigDecimal.valueOf(630));
        host.setWeekendRate(BigDecimal.valueOf(820.25));
        host.setId("3edda6bc-ab95-49a8-8962-d50b53f84b15");

        Result<Host> result = service.update(host);
        assertFalse(result.isSuccess());
        assertEquals("eyearnes0@sfgate.com", service.findById("3edda6bc-ab95-49a8-8962-d50b53f84b15").getEmail());
    }
}
