package learn.dontWreckMyHouse.domain;

import learn.dontWreckMyHouse.data.GuestRepository;
import org.springframework.stereotype.Service;

@Service
public class GuestService {

    private final GuestRepository repository;

    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

}
