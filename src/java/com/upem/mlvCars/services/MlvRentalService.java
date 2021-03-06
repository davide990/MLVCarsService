/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upem.mlvCars.services;

import com.upem.mlvCars.dao.mlvRentalDAO;
import com.upem.mlvCars.model.Rental;
import com.upem.mlvCars.model.Vehicle;
import com.upem.mlvCars.services.client.model.PersonEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author Davide Andrea Guastella <davide.guastella90@gmail.com>
 */
@WebService(serviceName = "MlvRentalService")
public class MlvRentalService {

    @EJB
    private mlvRentalDAO ejbRef;

    @WebMethod(operationName = "addRental")
    @Oneway
    public void addRental(@WebParam(name = "user") PersonEntity user,
            @WebParam(name = "rental") Rental rental) {
        ejbRef.addRental(user, rental);
    }

    @WebMethod(operationName = "validateRental")
    public boolean validateRental(@WebParam(name = "user") PersonEntity user,
            @WebParam(name = "rental") Rental rental) {
        return ejbRef.isRentalValid(user, rental);
    }

    @WebMethod(operationName = "updateRental")
    @Oneway
    public void updateRental(@WebParam(name = "rental") Rental rental) {
        ejbRef.updateRental(rental);
    }

    @WebMethod(operationName = "getAllRentals")
    public List<Rental> getAllRentals() {
        return ejbRef.getAllRentals();
    }

    @WebMethod(operationName = "getRentalByID")
    public Rental getRentalByID(@WebParam(name = "rental_id") int rental_id) {
        return ejbRef.getRentalByID(rental_id);
    }

    @WebMethod(operationName = "numberOfPreviousRental")
    public int numberOfPreviousRental(@WebParam(name = "vehichleID") int vehichleID) {
        return ejbRef.numberOfPreviousRental(vehichleID);
    }

    @WebMethod(operationName = "isVehicleRented")
    public boolean isVehicleRented(@WebParam(name = "vehichleID") int vehichleID) {
        return ejbRef.isVehicleRented(vehichleID);
    }

}
