package learn.dontWreckMyHouse.domain;

import learn.dontWreckMyHouse.data.DataException;
import learn.dontWreckMyHouse.data.GuestFileRepository;
import learn.dontWreckMyHouse.data.GuestRepository;
import learn.dontWreckMyHouse.models.Guest;
import learn.dontWreckMyHouse.models.Host;
import learn.dontWreckMyHouse.models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuestService {

    private GuestRepository repository;

    @Autowired
    public void setRepository(GuestRepository repository) {
        this.repository = repository;
    }

    public List<Guest> findByLastName(String prefix) {
        return repository.findAll().stream()
                .filter(i -> i.getLastName().startsWith(prefix))
                .collect(Collectors.toList());
    }

    public List<Guest> findAll() { return repository.findAll(); }  // Used for testing purposes

    public Guest findById(int id) { return repository.findById(id); }

    public Result<Guest> add(Guest guest) throws DataException {
        List<Guest> guests = repository.findAll();

        Result<Guest> result = new Result<>();

        if(guest == null) {
            result.addErrorMessage("Guest must not be null");
            return result;
        }

        if(guest.getFirstName() == null || guest.getFirstName().isBlank()) {
            result.addErrorMessage("Guest first name is required");
        }

        if(guest.getLastName() == null || guest.getLastName().isBlank()) {
            result.addErrorMessage("Guest last name is required");
        }

        if(guest.getEmail() == null || guest.getEmail().isBlank()) {
            result.addErrorMessage("Guest email is required");
        }

        // Should phone and state be required?
        for(Guest existingGuest : guests) {
            if(existingGuest.getEmail().equalsIgnoreCase(guest.getEmail())) {
                result.addErrorMessage("email already exists for guest");
            }
        }

        if(!result.isSuccess()) {
            return result;
        }

        result.setPayload(repository.add(guest));

        return result;
    }

    public Result<Guest> update(Guest guest) throws DataException {
        List<Guest> guests = repository.findAll();

        Result<Guest> result = new Result<>();

        if(guest == null) {
            result.addErrorMessage("Guest must not be null");
            return result;
        }

        if(findById(guest.getId()) == null) {
            result.addErrorMessage("Guest Id not found");
        }

        if(guest.getFirstName() == null || guest.getFirstName().isBlank()) {
            result.addErrorMessage("Guest first name is required");
        }

        if(guest.getLastName() == null || guest.getLastName().isBlank()) {
            result.addErrorMessage("Guest last name is required");
        }

        if(guest.getEmail() == null || guest.getFirstName().isBlank()) {
            result.addErrorMessage("Guest first name is required");
        }

        // Should phone and state be required?
        for(Guest existingGuest : guests) {
            if(existingGuest.getEmail().equalsIgnoreCase(guest.getEmail()) && existingGuest.getId() != guest.getId()) {
                result.addErrorMessage("email already exists for guest");
            }
        }

        if(!result.isSuccess()) {
            return result;
        }

        result.setPayload(repository.update(guest));

        return result;
    }

    public Result<Guest> remove(Guest guest) throws FileNotFoundException, DataException {
        Result<Guest> result = new Result<>();

        if(guest == null) {
            result.addErrorMessage("Guest must not be null");
            return result;
        }

        if(!result.isSuccess()) {
            return result;
        }

        if(repository.findById(guest.getId()) == null) {
            result.addErrorMessage("Guest not found");
        }

        result.setPayload(repository.delete(guest));

        return result;
    }

}
