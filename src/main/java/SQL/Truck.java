package SQL;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "truck_id")
    private int truckId;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "plate_number", nullable = false)
    private String plateNumber;

    @Column(name = "mileage", nullable = false)
    private int mileage;

    @Column(name = "created", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP " +
            "DEFAULT CURRENT_TIMESTAMP")
    private Date created;

    @Column(name = "last_update", nullable = false, updatable = false, insertable = false, columnDefinition =
            "TIMESTAMP " +
                    "DEFAULT CURRENT_TIMESTAMP " + "ON UPDATE CURRENT_TIMESTAMP")
    private Date lastUpdate;

    @OneToOne(mappedBy = "truck")
    private Driver currentDriver;

    private Truck(){}

    public Truck(String brand, String model, String plateNumber, int milage) {
        this.brand = brand;
        this.model = model;
        this.plateNumber = plateNumber;
        this.mileage = mileage;
    }

    public int getTruckId() {
        return truckId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
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

    public Driver getCurrentDriver() {
        return currentDriver;
    }

    public void setCurrentDriver(Driver currentDriver) {
        this.currentDriver = currentDriver;
    }

    public boolean hasDriver(){
        if(currentDriver != null){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Truck{" +
                "truckId=" + truckId +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", plateNumber='" + plateNumber + '\'' +
                ", mileage=" + mileage +
                ", created=" + created +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
