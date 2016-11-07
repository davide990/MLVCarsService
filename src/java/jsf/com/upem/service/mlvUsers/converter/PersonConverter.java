/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.com.upem.service.mlvUsers.converter;

import com.upem.mlvCars.services.client.mlvStudents.Person;
import com.upem.mlvCars.services.client.mlvStudents.Student;
import com.upem.mlvCars.services.client.mlvTeachers.Teacher;
import com.upem.mlvCars.services.client.model.PersonEntity;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import jsf.com.upem.mlvCars.rental.RentalController;

/**
 *
 * @author Davide Andrea Guastella <davide.guastella90@gmail.com>
 */
@Named
@FacesConverter("personConverter")
public class PersonConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        RentalController neededBean
                = (RentalController) facesContext.getApplication()
                .getELResolver().getValue(context.getELContext(), null, "rentalController");

        PersonEntity result = neededBean.retrieveMLVUserByID(Long.parseLong(value));

        return result;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (!(value instanceof PersonEntity)) {
            return "";
        }
        PersonEntity s = (PersonEntity) value;
        String l = Long.toString(s.getId());
        return l;
    }
    
}
