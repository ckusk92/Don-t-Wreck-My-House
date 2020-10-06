package learn.dontWreckMyHouse.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Host {

    private String id;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private int postalCode;
    private BigDecimal standardRate;
    private BigDecimal weekendRate;
    private List<Reservation> reservations;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public BigDecimal getStandardRate() {
        return standardRate;
    }

    public void setStandardRate(BigDecimal standardRate) {
        this.standardRate = standardRate;
    }

    public BigDecimal getWeekendRate() {
        return weekendRate;
    }

    public void setWeekendRate(BigDecimal weekendRate) {
        this.weekendRate = weekendRate;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Host item = (Host) o;
        return id.equalsIgnoreCase(item.id) &&
                Objects.equals(lastName, item.lastName) &&
                Objects.equals(email, item.email) &&
                Objects.equals(phone, item.phone) &&
                Objects.equals(address, item.address) &&
                Objects.equals(city, item.city) &&
                Objects.equals(state, item.state) &&
                Objects.equals(postalCode, item.postalCode) &&
                Objects.equals(standardRate, item.standardRate) &&
                Objects.equals(weekendRate, item.weekendRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastName, email, phone, address, state, postalCode, standardRate, weekendRate);
    }


}
