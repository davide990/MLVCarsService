/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upem.mlvCars.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Davide Andrea Guastella <davide.guastella90@gmail.com>
 */
@Entity
@Table(name = "rental")
public class Rental implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rental_id")
    private long id;

    @ManyToOne
    private Car car;

    @Column(name = "rentalStart")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date rentalStart;

    @Column(name = "rentalEnd")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date rentalEnd;

    @Column(name = "rentalPrice")
    private int rentalPrice;

    @Column(name = "clientId")
    private long client_id;
    
    @Column(name = "carStatusBeforeRental")
    private String carStatusBeforeRental;
    
    @Column(name = "carStatusAfterRental")
    private String carStatusAfterRental;
    
    @Column(name = "userComment")
    private String userComment;
    
    public String getCarStatusBeforeRental() {
        return carStatusBeforeRental;
    }

    public void setCarStatusBeforeRental(String carStatusBeforeRental) {
        this.carStatusBeforeRental = carStatusBeforeRental;
    }

    public String getCarStatusAfterRental() {
        return carStatusAfterRental;
    }

    public void setCarStatusAfterRental(String carStatusAfterRental) {
        this.carStatusAfterRental = carStatusAfterRental;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public long getId() {
        return id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Date getRentalStart() {
        return rentalStart;
    }

    public void setRentalStart(Date rentalStart) {
        this.rentalStart = rentalStart;
    }

    public Date getRentalEnd() {
        return rentalEnd;
    }

    public void setRentalEnd(Date rentalEnd) {
        this.rentalEnd = rentalEnd;
    }

    public int getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(int rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public long getClient_id() {
        return client_id;
    }

    public void setClient_id(long client_id) {
        this.client_id = client_id;
    }

}
