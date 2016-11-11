/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upem.mlvCars.services;

import com.upem.mlvCars.dao.mlvCarsDAO;
import com.upem.mlvCars.model.Car;
import com.upem.mlvCars.model.Vehicle;
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
@WebService(serviceName = "MlvCarsService")
public class MlvCarsService {

    @EJB
    private mlvCarsDAO ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "addVehicle")
    @Oneway
    public void addVehicle(@WebParam(name = "v") Vehicle v) {
        ejbRef.addVehicle(v);
    }

    @WebMethod(operationName = "updateVehicle")
    @Oneway
    public void updateVehicle(@WebParam(name = "v") Vehicle v) {
        ejbRef.updateVehicle(v);
    }

    @WebMethod(operationName = "deleteVehicle")
    @Oneway
    public void deleteVehicle(@WebParam(name = "student") Vehicle student) {
        ejbRef.deleteVehicle(student);
    }

    @WebMethod(operationName = "getAllVehicles")
    public List<Vehicle> getAllVehicles() {
        return ejbRef.getAllVehicles();
    }

    @WebMethod(operationName = "getVehicleByID")
    public Vehicle getVehicleByID(@WebParam(name = "student_id") int student_id) {
        return ejbRef.getVehicleByID(student_id);
    }

    @WebMethod(operationName = "getVehicleByMaxPassengers")
    public Vehicle getVehicleByMaxPassengers(@WebParam(name = "mp") int mp) {
        return ejbRef.getVehicleByMaxPassengers(mp);
    }

    @WebMethod(operationName = "getCarByID")
    public Car getCarByID(@WebParam(name = "id") int id) {
        return ejbRef.getCarByID(id);
    }

    @WebMethod(operationName = "getCarByBrand")
    public Car getCarByBrand(@WebParam(name = "brand") String brand) {
        return ejbRef.getCarByBrand(brand);
    }

    @WebMethod(operationName = "getCarByModel")
    public Car getCarByModel(@WebParam(name = "model") String model) {
        return ejbRef.getCarByModel(model);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "isVehicleOnSale")
    public Boolean isVehicleOnSale(@WebParam(name = "vehicleID") int vehicleID) {
        return ejbRef.isVehicleOnSale(vehicleID);
    }
    
    
    
}
