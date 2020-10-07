package learn.dontWreckMyHouse.ui;

import learn.dontWreckMyHouse.data.DataException;
import learn.dontWreckMyHouse.domain.GuestService;
import learn.dontWreckMyHouse.domain.HostService;
import learn.dontWreckMyHouse.domain.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Controller {

    private final GuestService guestService;
    private final HostService forageService;
    private final ReservationService reservationService;
    private final View view;

    public Controller(GuestService guestService, HostService forageService, ReservationService reservationService, View view) {
        this.guestService = guestService;
        this.forageService = forageService;
        this.reservationService = reservationService;
        this.view = view;
    }

    public void run() {
        view.displayHeader("Welcome to Don't Wreck My House");
        try {
            runAppLoop();
        } catch (DataException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye.");
    }

    private void runAppLoop() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS_FOR_HOST:
                    viewReservations();
                    break;
                case MAKE_A_RESERVATION:
                    makeReservation();
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

    private void viewReservations() {

    }

    private void makeReservation() {

    }

    private void editReservation() {

    }

    private void cancelReservation() {

    }

}
