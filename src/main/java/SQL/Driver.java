package SQL;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Driver {

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

    @Column(name = "age", nullable = false)
    private byte age;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "created", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP " +
            "DEFAULT CURRENT_TIMESTAMP")
    private Date created;

    @Column(name = "last_update", nullable = false, updatable = false, insertable = false, columnDefinition =
            "TIMESTAMP " +
                    "DEFAULT CURRENT_TIMESTAMP " + "ON UPDATE CURRENT_TIMESTAMP")
    private Date lastUpdate;

    @Column(name = "is_on_road", nullable = false)
    private boolean onRoad;

    @ManyToOne
    @JoinColumn(name = "forwarder_id")
    private Forwarder forwarder;

    @OneToOne
    @JoinColumn(name = "truck_id")
    private Truck truck;

    @OneToMany(mappedBy = "driver")
    private Set<Cargo> cargoes;

    private Driver (){}

    public Driver(long driverId,String firstName, String lastName, byte age, String phoneNumber,
                  boolean onRoad) {
        this.idNumber = driverId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.onRoad = onRoad;
        this.cargoes = new HashSet<>();
    }

    public int getRowId() {
        return rowId;
    }

    public void setIdNumber(long idNumber) {
        this.idNumber = idNumber;
    }

    public long getIdNumber() {
        return idNumber;
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

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public boolean isOnRoad() {
        return onRoad;
    }

    public void setOnRoad(boolean onRoad) {
        this.onRoad = onRoad;
    }

    public Forwarder getForwarder() {
        return forwarder;
    }

    public void setForwarder(Forwarder forwarder) {
        this.forwarder = forwarder;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public void addCargo(Cargo cargo){
        this.cargoes.add(cargo);
    }

    public void removeCargo(Cargo cargo){
        this.cargoes.remove(cargo);
    }

    public Set<Cargo> getCargoes() {
        return cargoes;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "rowId=" + rowId +
                ", idNumber=" + idNumber +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", created=" + created +
                ", lastUpdate=" + lastUpdate +
                ", onRoad=" + onRoad +
                ", forwarder=" + forwarder +
                ", truck=" + truck +
                '}';
    }
}
