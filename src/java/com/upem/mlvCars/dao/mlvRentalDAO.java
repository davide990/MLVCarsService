package com.upem.mlvCars.dao;

import com.upem.mlvCars.model.Car;
import com.upem.mlvCars.model.Rental;
import com.upem.mlvCars.model.Vehicle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
public class mlvRentalDAO {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private mlvCarsDAO carsDAO;

    private final static Logger logger = Logger.getLogger(mlvRentalDAO.class.getName());

    public void addRental(Rental rental) {
        /* persist:
            - Insert a new register to the database
            - Attach the object to the entity manager.
         */
        logger.log(Level.INFO, "Adding rental ID: " + rental.getId());
        if (!em.contains(rental)) {
            em.persist(rental);
            return;
        }

        em.merge(rental);

    }

    public void updateRental(Rental rental) {
        /* merge:
            - Find an attached object with the same id and update it.
            - If exists update and return the already attached object.
            - If doesn't exist insert the new register to the database.
         */
        logger.log(Level.INFO, "Updating rental ID: " + rental.getId());
        em.merge(rental);

    }

    public List<Rental> getAllRentals() {
        TypedQuery<Rental> q = em.createQuery("select e from rental e", Rental.class);
        return q.getResultList();
    }

    public Rental getRentalByID(int rental_id) {
        Rental s;
        TypedQuery<Rental> q = em.createQuery("select e from rental e where e.rental_id = '" + rental_id + "'", Rental.class);

        try {
            s = q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return s;
    }

    public int numberOfPreviousRental(int vehichleID) {
        //SELECT p FROM Teacher t JOIN t.phones p WHERE t.firstName = :firstName
        //SELECT r FROM rental r JOIN r.car c WHERE c.id = :id
        TypedQuery<Rental> q = em.createQuery("SELECT r FROM rental r JOIN r.car c WHERE c.id='" + vehichleID + "'", Rental.class);
        return q.getResultList().size();
    }

    public boolean isVehicleRented(int vehichleID) {
        TypedQuery<Rental> q = em.createQuery("SELECT r FROM rental r JOIN r.car c WHERE c.id='" + vehichleID + "' AND r.rentalEnd > CURRENT_DATE()", Rental.class);
        return q.getResultList().size() > 0;
    }

    public boolean isVehicleOnSale(int vehichleID) {
        //prendi veicolo con l'id dato
        Car v = carsDAO.getCarByID(vehichleID);
        //se la data di acquisto e quella odierna distano piu di 2 anni

        return getDiffYears(v.getPurchaseDate(), new Date()) > 2 && numberOfPreviousRental(vehichleID) >= 1;
    }

    public List<Vehicle> getVehiclesOnSale() {
        List<Vehicle> onSale = new ArrayList<>();

        for (Vehicle v : carsDAO.getAllVehicles()) {
            if (isVehicleOnSale(v.getId())) {
                onSale.add(v);
            }
        }

        return onSale;
    }

    private static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.DAY_OF_YEAR) > b.get(Calendar.DAY_OF_YEAR)) {
            diff--;
        }
        return diff;
    }

    private static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(date);
        return cal;
    }

}
