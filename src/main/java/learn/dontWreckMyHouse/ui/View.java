package learn.dontWreckMyHouse.ui;

import learn.dontWreckMyHouse.models.Guest;
import learn.dontWreckMyHouse.models.Host;
import learn.dontWreckMyHouse.models.Reservation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class View {

    private final ConsoleIO io;

    public View(ConsoleIO io) {
        this.io = io;
    }

    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MainMenuOption option : MainMenuOption.values()) {
            io.printf("%s. %s%n", option.getValue(), option.getMessage());
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }

        String message = String.format("Select [%s-%s]: ", min, max);
        return MainMenuOption.fromValue(io.readInt(message, min, max));
    }

    public Reservation makeReservation(Host host, Guest guest) {
        Reservation reservation = new Reservation();
        // Id will be set in repository
        reservation.setStartDate(getStartDate());
        reservation.setEndDate(getEndDate());
        reservation.setGuest(guest);
        // Total will be set in service
        reservation.setTotal(BigDecimal.ZERO);
        return reservation;
    }

    public Guest makeGuest() {
        displayHeader(MainMenuOption.ADD_A_GUEST.getMessage());
        Guest guest = new Guest();
        guest.setFirstName(io.readRequiredString("First Name: "));
        guest.setLastName(io.readRequiredString("Last Name: "));
        guest.setEmail(io.readRequiredString("Email: "));
        // Need to create a readPhoneNumber io method
        guest.setPhone(io.readPhone("Phone (123) 4567890 : "));
        guest.setState(io.readState("State (2 letter abbreviation): ", false));

        return guest;
    }

    public Reservation updateReservation(Reservation reservation, LocalDate originalStart, LocalDate originalEnd) {
        String headerString = String.format("Editing Reservation %s", reservation.getId());
        displayHeader(headerString);

        reservation.setStartDate(editStartDate(reservation.getStartDate(), originalStart));
        reservation.setEndDate(editEndDate(reservation.getEndDate(), originalEnd));

        return reservation;
    }

    public Guest updateGuest(Guest guest) {
        String firstName = io.readString("First Name: ");
        if(firstName.trim().length() > 0) {
            guest.setFirstName(firstName);
        }
        String lastName = io.readString("Last Name: ");
        if(lastName.trim().length() > 0) {
            guest.setLastName(lastName);
        }
        String email = io.readString("Email: ");
        if(email.trim().length() > 0) {
            guest.setEmail(email);
        }
        String phone = io.readPhone("Phone (123) 4567890: ");
        if(phone.trim().length() > 0) {
            guest.setPhone(phone);
        }
        String state = io.readState("State (2 letter abbreviation): ", false);
        if(state.trim().length() > 0) {
            guest.setState(state);
        }
        return guest;
    }

    public String getHostLastNamePrefix() {
        String string =  io.readRequiredString("Host last name starts with: ");
        // Allows user to type in lower case looking for last name
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    public String getGuestLastNamePrefix() {
        String string =  io.readRequiredString("Guest last name starts with: ");
        // Allows user to type in lower case looking for last name
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    public Boolean getUserConfirmation(Reservation reservation) {
        displayHeader("Summary");
        io.printf("Start: %s/%s/%s%nEnd: %s/%s/%s%nTotal: $%s%n",
                reservation.getStartDate().getMonthValue(),
                reservation.getStartDate().getDayOfMonth(),
                reservation.getStartDate().getYear(),
                reservation.getEndDate().getMonthValue(),
                reservation.getEndDate().getDayOfMonth(),
                reservation.getEndDate().getYear(),
                reservation.getTotal());
        return io.readBoolean("Is this okay? [y/n]: ");
    }

    public LocalDate getStartDate() {
        return io.readLocalDate("Start (MM/dd/yyyy): ");
    }

    public LocalDate getEndDate() {
        return io.readLocalDate("End (MM/dd/yyyy): ");
    }

    public LocalDate editStartDate(LocalDate currentStartDate, LocalDate originalStart) {
        String prompt = String.format("Start (%s/%s/%s): ", currentStartDate.getMonthValue(), currentStartDate.getDayOfMonth(), currentStartDate.getYear());
        return io.editLocalDate(prompt, originalStart);
    }

    public LocalDate editEndDate(LocalDate currentEndDate, LocalDate originalEnd) {
        String prompt = String.format("End (%s/%s/%s): ", currentEndDate.getMonthValue(), currentEndDate.getDayOfMonth(), currentEndDate.getYear());
        return io.editLocalDate(prompt, originalEnd);
    }

    public Host chooseHost(List<Host> hosts) {
        if (hosts.size() == 0) {
            io.println("No hosts found");
            return null;
        }

        int index = 1;
        for (Host host : hosts.stream().limit(25).collect(Collectors.toList())) {
            io.printf("%s: %s%n", index++, host.getLastName());
        }
        index--;

        if (hosts.size() > 25) {
            io.println("More than 25 hosts found. Showing first 25. Please refine your search.");
        }
        io.println("0: Exit");
        String message = String.format("Select a host by their index [0-%s]: ", index);

        index = io.readInt(message, 0, index);
        if (index <= 0) {
            return null;
        }
        return hosts.get(index - 1);
    }

    public Guest chooseGuest(List<Guest> guests) {
        if (guests.size() == 0) {
            io.println("No guests found");
            return null;
        }

        int index = 1;
        for (Guest guest : guests.stream().limit(25).collect(Collectors.toList())) {
            io.printf("%s: %s%n", index++, guest.getLastName());
        }
        index--;

        if (guests.size() > 25) {
            io.println("More than 25 guests found. Showing first 25. Please refine your search.");
        }
        io.println("0: Exit");
        String message = String.format("Select a guest by their index [0-%s]: ", index);

        index = io.readInt(message, 0, index);
        if (index <= 0) {
            return null;
        }
        return guests.get(index - 1);
    }

    public Reservation chooseReservation(List<Reservation> reservations, Host host) {
        displayReservations(reservations, host);
        boolean validChoice = false;
        while(!validChoice) {
            int reservationId = io.readInt("Reservation ID: ");
            for (Reservation reservation : reservations) {
                if (reservationId == reservation.getId()) {
                    return reservation;
                }
            }
            io.println("Please choose a valid Reservation Id from list above");
        }
        // Should never reach this statement
        return null;
    }

    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }

    // display only
    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }

    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }

    public void displayUserDenialMessage() {
        io.println("Reservation not created");
    }


    public void displayReservations(List<Reservation> reservations, Host host) {

        displayHeader(String.format("%s: %s, %s", host.getLastName(), host.getCity(), host.getState()));

        if (reservations == null || reservations.isEmpty()) {
            io.println("No reservations found.");
            return;
        }
        for (Reservation reservation : reservations) {
            io.printf("ID: %s, %s/%s/%s - %s/%s/%s, Guest: %s, %s, Email: %s%n",
                    reservation.getId(),
                    reservation.getStartDate().getMonthValue(),
                    reservation.getStartDate().getDayOfMonth(),
                    reservation.getStartDate().getYear(),
                    reservation.getEndDate().getMonthValue(),
                    reservation.getEndDate().getDayOfMonth(),
                    reservation.getEndDate().getYear(),
                    reservation.getGuest().getLastName(),
                    reservation.getGuest().getFirstName(),
                    reservation.getGuest().getEmail());
        }
    }
}
