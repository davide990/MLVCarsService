package com.upem.mlvCars.dao;

import com.upem.mlvCars.model.Car;
import com.upem.mlvCars.model.Vehicle;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Davide Andrea Guastella <davide.guastella90@gmail.com>
 */
@Stateless
public class mlvCarsDAO {

    @PersistenceContext
    private EntityManager em;

    private final static Logger logger = Logger.getLogger(mlvCarsDAO.class.getName());

    public void addVehicle(Vehicle v) {
        /* persist:
            - Insert a new register to the database
            - Attach the object to the entity manager.
         */
        logger.log(Level.INFO, "Adding vehicle ID: " + v.getId());

        if (!em.contains(v)) {
            em.persist(v);
            return;
        }

        em.merge(v);
    }

    public void updateVehicle(Vehicle v) {
        /* merge:
            - Find an attached object with the same id and update it.
            - If exists update and return the already attached object.
            - If doesn't exist insert the new register to the database.
         */
        logger.log(Level.INFO, "Updating vehicle ID: " + v.getId());
        em.merge(v);

    }

    public void deleteVehicle(Vehicle student) {
        logger.log(Level.INFO, "Deleting vehicle ID: " + student.getId());
        em.remove(student);
    }

    public List<Vehicle> getAllVehicles() {
        TypedQuery<Vehicle> q = em.createQuery("select e from vehicle e", Vehicle.class);
        return q.getResultList();
    }

    public Vehicle getVehicleByID(int student_id) {
        Vehicle s;
        TypedQuery<Vehicle> q = em.createQuery("select e from vehicle e where e.id = '" + student_id + "'", Vehicle.class);

        try {
            s = q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return s;
    }

    public Vehicle getVehicleByMaxPassengers(int mp) {
        Vehicle s;
        TypedQuery<Vehicle> q = em.createQuery("select e from vehicle e where e.maxPassengers = '" + mp + "'", Vehicle.class);

        try {
            s = q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return s;
    }

    public Car getCarByID(int id) {
        Car s;
        TypedQuery<Car> q = em.createQuery("select e from vehicle e where e.id = '" + id + "'", Car.class);

        try {
            s = q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return s;
    }

    public Car getCarByBrand(String brand) {
        Car s;
        TypedQuery<Car> q = em.createQuery("select e from vehicle e where e.id = '" + brand + "'", Car.class);

        try {
            s = q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return s;
    }

    public Car getCarByModel(String model) {
        Car s;
        TypedQuery<Car> q = em.createQuery("select e from vehicle e where e.model = '" + model + "'", Car.class);

        try {
            s = q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return s;
    }

}
