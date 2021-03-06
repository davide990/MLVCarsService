package com.upem.mlvCars.dao;

import com.upem.mlvCars.model.Car;
import com.upem.mlvCars.dao.util.JsfUtil;
import com.upem.mlvCars.dao.util.PaginationHelper;
import com.upem.mlvCars.model.CarType;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("carController")
@SessionScoped
public class CarController implements Serializable {

    private Car current;
    private DataModel items = null;
    @EJB
    private com.upem.mlvCars.dao.CarFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public CarController() {
    }

    public CarType[] getCarTypes() {
        return CarType.values();
    }

    public Car getSelected() {
        if (current == null) {
            current = new Car();
            selectedItemIndex = -1;
        }
        return current;
    }

    private CarFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Car) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Car();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Car " + current.getBrand() + " " + current.getModel() + " successful added");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return prepareCreate();
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cannot add car " + current.getModel() + ".\nPlease check your input!\n(Error: " + e + ")");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
    }

    public String prepareEdit() {
        current = (Car) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String prepareEdit(Car item) {
        //current = (Car) getItems().getRowData();
        current = item;
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Car " + current.getBrand() + " " + current.getModel() + " modified");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "View";
            //return "/car/List";
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Car " + current.getBrand() + " " + current.getModel() + " cannot be updated.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
    }

    public String destroy() {
        current = (Car) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    @EJB
    private mlvCarsDAO carsDAO;

    @EJB
    private mlvRentalDAO rentalDAO;

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
    
    public boolean isVehicleOnSale(int vehichleID) {
        //prendi veicolo con l'id dato
        Car v = carsDAO.getCarByID(vehichleID);

        //se la data di acquisto e quella odierna distano piu di 2 anni
        return getDiffYears(v.getPurchaseDate(), new Date()) > 2 && rentalDAO.numberOfPreviousRental(vehichleID) >= 1;
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Car " + current.getBrand() + " " + current.getModel() + " deleted");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Car " + current.getBrand() + " " + current.getModel() + " cannot be deleted");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Car getCar(int id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Car.class)
    public static class CarControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CarController controller = (CarController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "carController");
            return controller.getCar(getKey(value));
        }

        int getKey(String value) {
            int key;
            key = Integer.parseInt(value);
            return key;
        }

        String getStringKey(int value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Car) {
                Car o = (Car) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Car.class.getName());
            }
        }

    }

}
