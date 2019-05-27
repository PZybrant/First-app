package SQL;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by $(USER) on $(DATE)
 */
@Entity
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cargo_id")
    private int id;

    @Column(name = "cargo_type", nullable = false)
    private String type;

    @OneToOne
    @JoinColumn(name = "sender")
    private Company sender;

    @OneToOne
    @JoinColumn(name = "customer")
    private Company customer;

    @Column(nullable = false)
    private int distance;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "delivery_date", columnDefinition = "TIMESTAMP")
    private Timestamp deliveryDate;

    @Column(name = "date_of_order", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP " +
            "DEFAULT CURRENT_TIMESTAMP")
    private Timestamp dateOfOrder;

    @Column(name = "last_update", nullable = false, updatable = false, insertable = false, columnDefinition =
            "TIMESTAMP " + "DEFAULT CURRENT_TIMESTAMP " + "ON UPDATE CURRENT_TIMESTAMP")
    private Date lastUpdate;

    @ManyToOne
    @JoinColumn(name = "driver")
    private Driver driver;

    public Cargo(){}

    public Cargo( String type, int distance, Company sender, Company customer) {
        this.type = type;
        this.distance = distance;
        this.sender = sender;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Timestamp getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(Timestamp dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Company getSender() {
        return sender;
    }

    public void setSender(Company sender) {
        this.sender = sender;
    }

    public Company getCustomer() {
        return customer;
    }

    public void setCustomer(Company customer) {
        this.customer = customer;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", sender=" + sender +
                ", customer=" + customer +
                ", distance=" + distance +
                ", description='" + description + '\'' +
                ", deliveryDate=" + deliveryDate +
                ", dateOfOrder=" + dateOfOrder +
                ", lastUpdate=" + lastUpdate +
                ", driver=" + driver +
                '}';
    }
}
