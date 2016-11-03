package com.upem.mlvCars.services;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Davide Andrea Guastella <davide.guastella90@gmail.com>
 */
@WebService(serviceName = "CarsService")
public class CarsService {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
    
    @WebMethod(operationName = "reserveCar")
    public void reserveCar()
    {
        
    }
    
    @WebMethod(operationName = "rentCar")
    public void rentCar()
    {
        
    }
    
    @WebMethod(operationName = "addCar")
    public void addCar()
    {
        
    }
    
    @WebMethod(operationName = "deleteCar")
    public void deleteCar()
    {
        
        
    }
    
    @WebMethod(operationName = "isCarLeased")
    public void isCarLeased()
    {
        
    }
    
    @WebMethod(operationName = "isCarAvaible")
    public void isCarAvaible()
    {
        
    }
    
    @WebMethod(operationName = "getCarStatus")
    public void getCarStatus()
    {
        
    }
    
    @WebMethod(operationName = "getCarInformation")
    public void getCarInformations()
    {
        
    }
    
}
