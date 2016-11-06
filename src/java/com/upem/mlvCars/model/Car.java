package com.upem.mlvCars.model;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 *
 * @author Davide Andrea Guastella <davide.guastella90@gmail.com>
 */
@Entity
@DiscriminatorValue("car")
@Table(name = "car")
public class Car extends Vehicle {

    @Enumerated(EnumType.STRING)
    private CarType type;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "airConditioner")
    private boolean airConditioner;

    @Column(name = "automaticTransmission")
    private boolean automaticTransmission;

    @Column(name = "purchaseDate")
    private Date purchaseDate;

    public CarType getType() {
        return type;
    }

    public void setType(CarType type) {
        this.type = type;
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

    public boolean isAirConditioner() {
        return airConditioner;
    }

    public void setAirConditioner(boolean airConditioner) {
        this.airConditioner = airConditioner;
    }

    public boolean isAutomaticTransmission() {
        return automaticTransmission;
    }

    public void setAutomaticTransmission(boolean automaticTransmission) {
        this.automaticTransmission = automaticTransmission;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.type);
        hash = 59 * hash + Objects.hashCode(this.brand);
        hash = 59 * hash + Objects.hashCode(this.model);
        hash = 59 * hash + (this.airConditioner ? 1 : 0);
        hash = 59 * hash + (this.automaticTransmission ? 1 : 0);
        hash = 59 * hash + Objects.hashCode(this.purchaseDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Car other = (Car) obj;
        if (this.airConditioner != other.airConditioner) {
            return false;
        }
        if (this.automaticTransmission != other.automaticTransmission) {
            return false;
        }
        if (!Objects.equals(this.brand, other.brand)) {
            return false;
        }
        if (!Objects.equals(this.model, other.model)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return Objects.equals(this.purchaseDate, other.purchaseDate);
    }

    
    
}
