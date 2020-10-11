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
import java.io.IOException;
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

    public void run() throws IOException {
        view.displayHeader("Welcome to Don't Wreck My House");
        try {
            runAppLoop();
        } catch (DataException | FileNotFoundException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye.");
    }

    private void runAppLoop() throws DataException, IOException {
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
                case ADD_A_GUEST:
                    addGuest();
                    break;
                case EDIT_A_GUEST:
                    editGuest();
                    break;
                case DELETE_A_GUEST:
                    deleteGuest();
                    break;
                case ADD_A_HOST:
                    addHost();
                    break;
                case EDIT_A_HOST:
                    editHost();
                    break;
                case DELETE_A_HOST:
                    deleteHost();
                    break;
            }
        } while (option != MainMenuOption.EXIT);
    }

    private void viewReservationsForHost() throws FileNotFoundException {
        view.displayHeader(MainMenuOption.VIEW_RESERVATIONS_FOR_HOST.getMessage());
        // Use getHost() to find which host to display reservations for
        Host host = getHost();
        // Use service to get list of reservations
        List<Reservation> reservations = reservationService.reservationsForHost(host.getId());
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
                // Call delete function on newly created reservation - STILL NEED TO FIGURE OUT
                view.displayUserDenialMessage();
            }
        }
    }

    private void editReservation() throws FileNotFoundException, DataException {
        view.displayHeader(MainMenuOption.EDIT_A_RESERVATION.getMessage());
        Guest guest = getGuest();
        if(guest == null) { return; }
        Host host = getHost();
        if(host == null) { return; }
        // Get all reservations for host
        List<Reservation> matchingReservations = reservationService.findForHostAndGuest(host, guest);
        // Have user pick appropriate one
        Reservation updatedReservation = view.chooseReservation(matchingReservations, host);

        LocalDate originalStart = updatedReservation.getStartDate();
        LocalDate originalEnd = updatedReservation.getEndDate();

        updatedReservation = view.updateReservation(updatedReservation, originalStart, originalEnd);
        Result<Reservation> result = reservationService.update(updatedReservation, host, originalStart, originalEnd);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
        } else {
            // Display the info to user and ask if ok
            if(view.getUserConfirmation(updatedReservation)) {
                String successMessage = String.format("Reservation %s edited.", result.getPayload().getId());
                view.displayStatus(true, successMessage);
            } else {
                view.displayUserDenialMessage();
            }
        }
    }

    private void cancelReservation() throws FileNotFoundException, DataException {
        view.displayHeader(MainMenuOption.CANCEL_A_RESERVATION.getMessage());
        Guest guest = getGuest();
        if(guest == null) { return; }
        Host host = getHost();
        if(host == null) { return; }

        // Get all reservations for host
        List<Reservation> matchingReservations = reservationService.findForHostAndGuest(host, guest);
        // Have user pick appropriate one
        Reservation deleteReservation = view.chooseReservation(matchingReservations, host);
        int deleteId = deleteReservation.getId();

        Result<Reservation> result = reservationService.remove(deleteReservation, host);
        String successMessage = String.format("Reservation %s cancelled.", deleteId);

        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
        } else {
            view.displayStatus(true, successMessage);
        }
    }

    private void addGuest() throws DataException {
        Guest guest = view.makeGuest();
        Result<Guest> result = guestService.add(guest);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
        } else {
            String successMessage = String.format("Guest %s created.", result.getPayload().getId());
            view.displayStatus(true, successMessage);
        }
    }

    private void editGuest() throws DataException {
        view.displayHeader(MainMenuOption.EDIT_A_GUEST.getMessage());
        Guest guest = getGuest();
        if(guest == null) { return; }

        guest = view.updateGuest(guest);
        Result<Guest> result = guestService.update(guest);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
        } else {
            String successMessage = String.format("Guest %s updated.", result.getPayload().getId());
            view.displayStatus(true, successMessage);
        }
    }

    private void deleteGuest() throws FileNotFoundException, DataException {
        view.displayHeader(MainMenuOption.DELETE_A_GUEST.getMessage());
        Guest guest = getGuest();
        if(guest == null) { return; }
        //guest = view.updateGuest(guest);
        Result<Guest> result = guestService.remove(guest);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
        } else {
            String successMessage = String.format("Guest %s deleted.", result.getPayload().getId());
            view.displayStatus(true, successMessage);
        }
    }

    private void addHost() throws DataException, IOException {
        Host host = view.makeHost();
        Result<Host> result = hostService.add(host);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
        } else {
            String successMessage = String.format("Host %s created.", result.getPayload().getId());
            view.displayStatus(true, successMessage);
        }
    }

    private void editHost() throws DataException {
        view.displayHeader(MainMenuOption.EDIT_A_HOST.getMessage());
        Host host = getHost();
        if(host == null) { return; }

        host = view.updateHost(host);
        Result<Host> result = hostService.update(host);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
        } else {
            String successMessage = String.format("Host %s updated.", result.getPayload().getId());
            view.displayStatus(true, successMessage);
        }
    }

    private void deleteHost() {

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
