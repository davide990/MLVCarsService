package com.upem.mlvCars.dao;

import com.upem.mlvCars.model.Car;
import com.upem.mlvCars.model.Vehicle;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Vehicle> vehicle = builder.createQuery(Vehicle.class);

        Root<Vehicle> vehicleRoot = vehicle.from(Vehicle.class);
        vehicle.select(vehicleRoot);
        TypedQuery<Vehicle> pp = em.createQuery(vehicle);

        try {
            return pp.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Vehicle getVehicleByID(int id) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Vehicle> vehicle = builder.createQuery(Vehicle.class);

        Root<Vehicle> vehicleRoot = vehicle.from(Vehicle.class);
        Predicate pd = builder.equal(vehicleRoot.get("id"), id);

        vehicle.select(vehicleRoot).where(pd);
        TypedQuery<Vehicle> pp = em.createQuery(vehicle);

        try {
            return pp.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Vehicle getVehicleByMaxPassengers(int mp) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Vehicle> vehicle = builder.createQuery(Vehicle.class);

        Root<Vehicle> vehicleRoot = vehicle.from(Vehicle.class);
        Predicate pd = builder.equal(vehicleRoot.get("maxPassengers"), mp);

        vehicle.select(vehicleRoot).where(pd);
        TypedQuery<Vehicle> pp = em.createQuery(vehicle);

        try {
            return pp.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Car getCarByID(int id) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Vehicle> vehicle = builder.createQuery(Vehicle.class);

        Root<Vehicle> vehicleRoot = vehicle.from(Vehicle.class); //ROOT
        Root<Car> carRoot = builder.treat(vehicleRoot, Car.class);   //SUBCLASS

        Predicate pd_1 = builder.equal(carRoot.get("id"), vehicleRoot.get("id"));
        Predicate pd_2 = builder.equal(carRoot.get("id"), id);
        Predicate pd_3 = builder.and(pd_1, pd_2);

        vehicle.select(vehicleRoot).where(pd_3);
        TypedQuery<Vehicle> pp = em.createQuery(vehicle);

        try {
            return (Car) pp.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Car getCarByBrand(String brand) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Vehicle> vehicle = builder.createQuery(Vehicle.class);

        Root<Vehicle> vehicleRoot = vehicle.from(Vehicle.class); //ROOT
        Root<Car> carRoot = builder.treat(vehicleRoot, Car.class);   //SUBCLASS

        Predicate pd_1 = builder.equal(carRoot.get("id"), vehicleRoot.get("id"));
        Predicate pd_2 = builder.equal(carRoot.get("brand"), brand);
        Predicate pd_3 = builder.and(pd_1, pd_2);

        vehicle.select(vehicleRoot).where(pd_3);
        TypedQuery<Vehicle> pp = em.createQuery(vehicle);

        try {
            return (Car) pp.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Car getCarByModel(String model) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Vehicle> vehicle = builder.createQuery(Vehicle.class);

        Root<Vehicle> vehicleRoot = vehicle.from(Vehicle.class); //ROOT
        Root<Car> carRoot = builder.treat(vehicleRoot, Car.class);   //SUBCLASS

        Predicate pd_1 = builder.equal(carRoot.get("id"), vehicleRoot.get("id"));
        Predicate pd_2 = builder.equal(carRoot.get("model"), model);
        Predicate pd_3 = builder.and(pd_1, pd_2);

        vehicle.select(vehicleRoot).where(pd_3);
        TypedQuery<Vehicle> pp = em.createQuery(vehicle);

        try {
            return (Car) pp.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
