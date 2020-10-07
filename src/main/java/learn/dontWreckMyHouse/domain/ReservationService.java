package learn.dontWreckMyHouse.domain;

import learn.dontWreckMyHouse.data.HostRepository;
import learn.dontWreckMyHouse.data.ReservationFileRepository;
import learn.dontWreckMyHouse.data.ReservationRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private final ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }
}
