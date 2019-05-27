package SQL;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Forwarder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "row_id")
    private int rowId;

    @Column(name = "id_number", nullable = false, unique = true)
    private long idNumber;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "created", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP " +
            "DEFAULT CURRENT_TIMESTAMP")
    private Date created;

    @Column(name = "last_update", nullable = false, updatable = false, insertable = false, columnDefinition =
            "TIMESTAMP " +
            "DEFAULT CURRENT_TIMESTAMP " + "ON UPDATE CURRENT_TIMESTAMP")
    private Date lastUpdate;

    @OneToOne()
    @JoinColumn(name = "login_data_id")
    private LoginData loginData;

    @OneToMany(mappedBy = "forwarder")
    private Set<Driver> drivers;

    private Forwarder() {
    }

    public Forwarder(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.drivers = new HashSet<>();
    }

    public int getRowId() {
        return rowId;
    }

    public long getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(long idNumber) {
        this.idNumber = idNumber;
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

    public Date getCreated() {
        return created;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public LoginData getLoginData() {
        return loginData;
    }

    public void setLoginData(LoginData loginData) {
        this.loginData = loginData;
    }

    public void addDriver(Driver driver){
        this.drivers.add(driver);
    }

    public void removeDriver(Driver driver){
        this.drivers.remove(driver);
    }

    public Set<Driver> getDrivers() {
        return drivers;
    }

    @Override
    public String toString() {
        return "Forwarder{" +
                "rowId=" + rowId +
                "idNumber=" + idNumber +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", created=" + created +
                ", lastUpdate=" + lastUpdate +
                ", loginData=" + loginData +
                '}';
    }
}
