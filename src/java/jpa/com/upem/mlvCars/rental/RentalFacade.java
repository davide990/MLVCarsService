/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.com.upem.mlvCars.rental;

import com.upem.mlvCars.model.Rental;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Davide Andrea Guastella <davide.guastella90@gmail.com>
 */
@Stateless
public class RentalFacade extends AbstractFacade<Rental> {

    @PersistenceContext(unitName = "MlvCarsServicePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RentalFacade() {
        super(Rental.class);
    }
    
}
