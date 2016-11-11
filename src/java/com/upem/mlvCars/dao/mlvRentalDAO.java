package com.upem.mlvCars.dao;

import com.upem.mlvCars.model.Car;
import com.upem.mlvCars.model.Rental;
import com.upem.mlvCars.model.Vehicle;
import com.upem.mlvCars.services.bank.BankServiceClient;
import com.upem.mlvCars.services.client.model.PersonEntity;
import com.upem.mlvCars.services.users.UserServiceClient;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

    public void addRental(PersonEntity user, Rental rental) {
        /* persist:
            - Insert a new register to the database
            - Attach the object to the entity manager.
         */
        logger.log(Level.INFO, "Adding rental ID: " + rental.getId());
        BankServiceClient.withdrawMoneyFromUserAccount(user.getIban(), rental.getRentalPrice());
        em.persist(rental);
    }

    public boolean isRentalValid(PersonEntity user, Rental rental) {
        return UserServiceClient.userHasEnoughMoney(user.getId(), rental.getRentalPrice())
                && isVehicleAvaibleForRental(rental.getCar().getId(), rental.getRentalStart(), rental.getRentalEnd());
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
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Rental> rental = builder.createQuery(Rental.class);

        Root<Rental> rentalRoot = rental.from(Rental.class);

        Predicate pd_1 = builder.equal(rentalRoot.get("car").get("id"), vehichleID);

        rental.select(rentalRoot).where(pd_1);
        TypedQuery<Rental> pp = em.createQuery(rental);
        return pp.getResultList().size();
    }

    public boolean isVehicleAvaibleForRental(int vehichleID, Date start, Date end) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Rental> rental = builder.createQuery(Rental.class);

        Root<Rental> rentalRoot = rental.from(Rental.class);

        Predicate pd_1 = builder.lessThan(rentalRoot.get("rentalStart"), end);
        Predicate pd_2 = builder.greaterThanOrEqualTo(rentalRoot.get("rentalEnd"), start);
        Predicate pd_3 = builder.and(pd_1, pd_2);

        rental.select(rentalRoot).where(pd_3);
        TypedQuery<Rental> pp = em.createQuery(rental);

        try {
            pp.getSingleResult();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isVehicleRented(int vehichleID) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Rental> rental = builder.createQuery(Rental.class);

        Root<Rental> rentalRoot = rental.from(Rental.class);

        Predicate pd_1 = builder.equal(rentalRoot.get("car").get("id"), vehichleID);
        Predicate pd_2 = builder.greaterThan(rentalRoot.get("rentalEnd"), new Date());
        Predicate pd_3 = builder.and(pd_1, pd_2);

        rental.select(rentalRoot).where(pd_3);
        TypedQuery<Rental> pp = em.createQuery(rental);

        try {
            pp.getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
