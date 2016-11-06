/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.com.upem.mlvCars.cars;

import com.upem.mlvCars.dao.CarController;
import com.upem.mlvCars.model.Car;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

/**
 *
 * @author Davide Andrea Guastella <davide.guastella90@gmail.com>
 */
@Named
@FacesConverter("carConverter")
public class CarConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String id) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        CarController neededBean
                = (CarController) facesContext.getApplication()
                .getELResolver().getValue(context.getELContext(), null, "carController");

        return neededBean.getCar(Integer.parseInt(id));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return Integer.toString(((Car) value).getId());
    }

}
