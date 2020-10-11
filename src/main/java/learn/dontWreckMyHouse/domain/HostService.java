package learn.dontWreckMyHouse.domain;

import learn.dontWreckMyHouse.data.*;
import learn.dontWreckMyHouse.models.Guest;
import learn.dontWreckMyHouse.models.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HostService {

    private HostRepository repository;
    private ReservationRepository reservationRepository;

    @Autowired
    public void setRepository(HostRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setReservationRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Host> findByLastName(String prefix) {
        return repository.findAll().stream()
                .filter(i -> i.getLastName().startsWith(prefix))
                .collect(Collectors.toList());
    }

    public List<Host> findAll() { return repository.findAll(); }  // Used for testing purposes

    public Host findById(String id) { return repository.findById(id); }

    public Result<Host> add(Host host) throws DataException, IOException {
        List<Host> hosts = repository.findAll();

        Result<Host> result = new Result<>();

        if(host == null) {
            result.addErrorMessage("Host must not be null");
            return result;
        }
        if(host.getLastName() == null || host.getLastName().isBlank()) {
            result.addErrorMessage("Host last name is required");
        }
        if(host.getEmail() == null || host.getEmail().isBlank()) {
            result.addErrorMessage("Host email is required");
        }
        if(host.getPhone() == null || host.getPhone().isBlank()) {
            result.addErrorMessage("Host phone number is required");
        }
        if(host.getAddress() == null || host.getAddress().isBlank()) {
            result.addErrorMessage("Host address is required");
        }
        if(host.getCity() == null || host.getCity().isBlank()) {
            result.addErrorMessage("Host city is required");
        }
        if(host.getState() == null || host.getState().isBlank()) {
            result.addErrorMessage("Host state is required");
        }
        // Not sure how to apply to int and BigDecimal, should never happen as IO functions will require them

        for(Host existingHost : hosts) {
            if(existingHost.getEmail().equalsIgnoreCase(host.getEmail())) {
                result.addErrorMessage("Email already exists for host");
            }
        }

        if(!result.isSuccess()) {
            return result;
        }

        result.setPayload(repository.add(host));

        return result;
    }

    public Result<Host> update(Host host) throws DataException {
        List<Host> hosts = repository.findAll();

        Result<Host> result = new Result<>();

        if(host == null) {
            result.addErrorMessage("Host must not be null");
            return result;
        }

        if(findById(host.getId()) == null) {
            result.addErrorMessage("Host Id not found");
        }
        if(host.getLastName() == null || host.getLastName().isBlank()) {
            result.addErrorMessage("Host last name is required");
        }
        if(host.getEmail() == null || host.getEmail().isBlank()) {
            result.addErrorMessage("Host first name is required");
        }
        if(host.getPhone() == null || host.getPhone().isBlank()) {
            result.addErrorMessage("Host phone number is required");
        }
        if(host.getAddress() == null || host.getAddress().isBlank()) {
            result.addErrorMessage("Host address is required");
        }
        if(host.getCity() == null || host.getCity().isBlank()) {
            result.addErrorMessage("Host city is required");
        }
        if(host.getState() == null || host.getState().isBlank()) {
            result.addErrorMessage("Host state is required");
        }

        for(Host existingHost : hosts) {
            if(existingHost.getEmail().equalsIgnoreCase(host.getEmail()) && (!existingHost.getId().equalsIgnoreCase(host.getId()))) {
                result.addErrorMessage("email already exists for host");
            }
        }

        if(!result.isSuccess()) {
            return result;
        }

        // Need to change total in reservation incase rates change

        result.setPayload(repository.update(host));

        return result;
    }

    public Result<Host> remove(Host host) throws FileNotFoundException, DataException {
        Result<Host> result = new Result<>();

        if(host == null) {
            result.addErrorMessage("Host must not be null");
            return result;
        }

        if(!result.isSuccess()) {
            return result;
        }

        if(repository.findById(host.getId()) == null) {
            result.addErrorMessage("Host not found");
        }

        result.setPayload(repository.delete(host));

        return result;
    }

}
