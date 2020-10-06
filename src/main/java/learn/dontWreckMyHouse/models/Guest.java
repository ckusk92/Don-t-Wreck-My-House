package learn.dontWreckMyHouse.models;

import learn.dontWreckMyHouse.data.GuestRepository;

import java.util.Objects;

public class Guest{

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String state;

    public Guest() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest item = (Guest) o;
        return id == item.id &&
                Objects.equals(firstName, item.firstName) &&
                Objects.equals(lastName, item.lastName) &&
                Objects.equals(email, item.email) &&
                Objects.equals(phone, item.phone) &&
                Objects.equals(state, item.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, phone, state);
    }
}
