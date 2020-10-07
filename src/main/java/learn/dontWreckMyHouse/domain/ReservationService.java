package learn.dontWreckMyHouse.domain;

import learn.dontWreckMyHouse.data.HostRepository;
import learn.dontWreckMyHouse.data.ReservationFileRepository;
import learn.dontWreckMyHouse.data.ReservationRepository;
import learn.dontWreckMyHouse.models.Host;
import learn.dontWreckMyHouse.models.Reservation;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    public List<Reservation> reservationsForHost(Host host) throws FileNotFoundException {
        List<Reservation> reservations = repository.findReservationsForHost(host);
        List<Reservation> sorted = reservations.stream()
                .sorted((a,b) -> a.getStartDate().compareTo(b.getStartDate()))
                .collect(Collectors.toList());

        return sorted;
    }
}
