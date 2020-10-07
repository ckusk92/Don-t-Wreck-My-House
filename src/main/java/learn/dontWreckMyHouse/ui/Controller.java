package learn.dontWreckMyHouse.ui;

import learn.dontWreckMyHouse.data.DataException;
import learn.dontWreckMyHouse.domain.GuestService;
import learn.dontWreckMyHouse.domain.HostService;
import learn.dontWreckMyHouse.domain.ReservationService;
import learn.dontWreckMyHouse.domain.Result;
import learn.dontWreckMyHouse.models.Guest;
import learn.dontWreckMyHouse.models.Host;
import learn.dontWreckMyHouse.models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Component
public class Controller {

    private final GuestService guestService;
    private final HostService hostService;
    private final ReservationService reservationService;
    private final View view;

    public Controller(GuestService guestService, HostService hostService, ReservationService reservationService, View view) {
        this.guestService = guestService;
        this.hostService = hostService;
        this.reservationService = reservationService;
        this.view = view;
    }

    public void run() {
        view.displayHeader("Welcome to Don't Wreck My House");
        try {
            runAppLoop();
        } catch (DataException | FileNotFoundException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye.");
    }

    private void runAppLoop() throws DataException, FileNotFoundException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS_FOR_HOST:
                    viewReservationsForHost();
                    break;
                case MAKE_A_RESERVATION:
                    addReservation();
                    break;
                case EDIT_A_RESERVATION:
                    editReservation();
                    break;
                case CANCEL_A_RESERVATION:
                    cancelReservation();
                    break;
            }
        } while (option != MainMenuOption.EXIT);
    }

    private void viewReservationsForHost() throws FileNotFoundException {
        view.displayHeader(MainMenuOption.VIEW_RESERVATIONS_FOR_HOST.getMessage());
        // Use getHost() to find which host to display reservations for
        Host host = getHost();
        // Use service to get list of reservations
        List<Reservation> reservations = reservationService.reservationsForHost(host);
        view.displayReservations(reservations, host);
        view.enterToContinue();
    }

    private void addReservation() throws FileNotFoundException, DataException {
        view.displayHeader(MainMenuOption.MAKE_A_RESERVATION.getMessage());
        Guest guest = getGuest();
        if(guest == null) { return; }
        Host host = getHost();
        if(host == null) { return; }

        Reservation reservation = view.makeReservation(host, guest);
        Result<Reservation> result = reservationService.add(reservation, host);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
        } else {
            // Display the info to user and ask if ok
            if(view.getUserConfirmation(reservation)) {
                String successMessage = String.format("Reservation %s created.", result.getPayload().getId());
                view.displayStatus(true, successMessage);
            } else {
                // Call delete function on newly created reservation
                view.displayUserDenialMessage();
            }
        }
    }

    private void editReservation() {
        view.displayHeader(MainMenuOption.EDIT_A_RESERVATION.getMessage());
    }

    private void cancelReservation() {
        view.displayHeader(MainMenuOption.CANCEL_A_RESERVATION.getMessage());
    }

    // Support Methods
    private Host getHost() {
        String lastNamePrefix = view.getHostLastNamePrefix();
        List<Host> hosts = hostService.findByLastName(lastNamePrefix);
        return view.chooseHost(hosts);
    }

    private Guest getGuest() {
        String lastNamePrefix = view.getGuestLastNamePrefix();
        List<Guest> guests = guestService.findByLastName(lastNamePrefix);
        return view.chooseGuest(guests);
    }

}
