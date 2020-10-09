package learn.dontWreckMyHouse.domain;

import learn.dontWreckMyHouse.data.HostRepository;
import learn.dontWreckMyHouse.models.Guest;
import learn.dontWreckMyHouse.models.Host;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HostService {

    private final HostRepository repository;

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    public List<Host> findByLastName(String prefix) {
        return repository.findAll().stream()
                .filter(i -> i.getLastName().startsWith(prefix))
                .collect(Collectors.toList());
    }

    public List<Host> findAll() { return repository.findAll(); }  // Used for testing purposes

}
