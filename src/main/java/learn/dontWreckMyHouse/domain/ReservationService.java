package learn.dontWreckMyHouse.domain;

import learn.dontWreckMyHouse.data.DataException;
import learn.dontWreckMyHouse.data.HostRepository;
import learn.dontWreckMyHouse.data.ReservationFileRepository;
import learn.dontWreckMyHouse.data.ReservationRepository;
import learn.dontWreckMyHouse.models.Guest;
import learn.dontWreckMyHouse.models.Host;
import learn.dontWreckMyHouse.models.Reservation;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
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

    public Result<Reservation> add(Reservation reservation, Host host) throws FileNotFoundException, DataException {

        List<Reservation> hostReservations = reservationsForHost(host);
        Result<Reservation> result = new Result<>();

        if(reservation == null) {
            result.addErrorMessage("Reservation must not be null");
            return result;
        }

        if(reservation.getGuest() == null) {
            result.addErrorMessage("Guest is required");
        }

        // Also make sure start date is before end date and one day apart
        if(reservation.getStartDate().compareTo(reservation.getEndDate()) >= 0) {
            result.addErrorMessage("End date must be after start date");
        }

        // Check that there is no overlapping dates
        for(Reservation existingReservation : hostReservations) {
            if((reservation.getStartDate().compareTo(existingReservation.getStartDate()) >= 0 &&
                    reservation.getStartDate().compareTo(existingReservation.getEndDate()) <= 0) ||
            (reservation.getEndDate().compareTo(existingReservation.getStartDate()) >= 0 &&
                    reservation.getEndDate().compareTo(existingReservation.getEndDate()) <= 0)) {
                result.addErrorMessage("Dates must not overlap with existing reservations");
            }
        }

        if(!result.isSuccess()) {
            return result;
        }

        reservation.setTotal(calculateTotal(reservation, host));
        result.setPayload(repository.add(reservation, host));

        return result;
    }

    public List<Reservation> findForHostAndGuest(Host host, Guest guest) throws FileNotFoundException {
        List<Reservation> hostReservations = reservationsForHost(host);
        ArrayList<Reservation> guestReservations = new ArrayList<>();

        for(Reservation reservation : hostReservations) {
            if(reservation.getGuest().getId() == guest.getId()) {
                guestReservations.add(reservation);
            }
        }
        return guestReservations;
    }

    // Helper functions

    private BigDecimal calculateTotal(Reservation reservation, Host host) {
        LocalDate iterator = reservation.getStartDate();
        BigDecimal total = BigDecimal.ZERO;

        while(iterator.compareTo(reservation.getEndDate()) <= 0) {
            if(iterator.getDayOfWeek() == DayOfWeek.SATURDAY || iterator.getDayOfWeek() == DayOfWeek.SUNDAY) {
                total = total.add(host.getWeekendRate());
            } else {
                total = total.add(host.getStandardRate());
            }

            iterator = iterator.plusDays(1);
        }
        return total;
    }
}
