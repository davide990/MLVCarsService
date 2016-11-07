/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.com.upem.service.mlvUsers.converter;

import com.upem.mlvCars.services.client.mlvStudents.Student;
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
@FacesConverter("studentConverter")
public class StudentConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        RentalController neededBean
                = (RentalController) facesContext.getApplication()
                .getELResolver().getValue(context.getELContext(), null, "rentalController");

        return neededBean.retrieveMLVStudentByID(Integer.parseInt(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (!(value instanceof Student)) {
            return "";
        }
        Student s = ((Student) value);
        String l = Long.toString(s.getId());
        return l;
    }

}
