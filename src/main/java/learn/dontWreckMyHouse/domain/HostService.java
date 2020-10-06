package learn.dontWreckMyHouse.domain;

import learn.dontWreckMyHouse.data.HostRepository;
import learn.dontWreckMyHouse.models.Host;

import java.util.List;
import java.util.stream.Collectors;

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

}
