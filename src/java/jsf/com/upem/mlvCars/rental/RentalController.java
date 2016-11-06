package jsf.com.upem.mlvCars.rental;

import com.upem.mlvCars.dao.CarFacade;
import com.upem.mlvCars.model.Car;
import com.upem.mlvCars.model.Rental;
import jsf.com.upem.mlvCars.rental.util.JsfUtil;
import jsf.com.upem.mlvCars.rental.util.PaginationHelper;
import jpa.com.upem.mlvCars.rental.RentalFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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

@Named("rentalController")
@SessionScoped
public class RentalController implements Serializable {

    private Rental current;
    private DataModel items = null;
    @EJB
    private jpa.com.upem.mlvCars.rental.RentalFacade ejbFacade;
    
    private Car selectedCar;

    private List<Car> avaibleCar;
    
    @EJB
    private CarFacade carFacade;
    
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public RentalController() {
    }

    public Rental getSelected() {
        if (current == null) {
            current = new Rental();
            selectedItemIndex = -1;
        }
        return current;
    }

    public List<Car> getAvaibleCar() {
        return avaibleCar;
    }

    public void setAvaibleCar(List<Car> avaibleCar) {
        this.avaibleCar = avaibleCar;
    }
    
    public Car getSelectedCar() {
        return selectedCar;
    }

    public void setSelectedCar(Car selectedCar) {
        this.selectedCar = selectedCar;
    }
    
    
    private RentalFacade getFacade() {
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
        current = (Rental) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Rental();
        selectedCar = new Car();
        avaibleCar = carFacade.findAll();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Rental successful added");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return prepareCreate();
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Rental cannot be added");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
    }

    public String prepareEdit() {
        current = (Rental) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public String prepareEdit(Rental item) {
        current = item;
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public List<Car> getAvaibleCars()
    {
        return carFacade.findAll();
    }
    
    
    public String update() {
        try {
            getFacade().edit(current);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Rental successful updated");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "View";
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Rental cannot be updated");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
    }

    public String destroy() {
        current = (Rental) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
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
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Rental successful deleted");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Rental cannot be deleted");
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

    public Rental getRental(long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Rental.class)
    public static class RentalControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RentalController controller = (RentalController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "rentalController");
            return controller.getRental(getKey(value));
        }

        long getKey(String value) {
            long key;
            key = Long.parseLong(value);
            return key;
        }

        String getStringKey(long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Rental) {
                Rental o = (Rental) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Rental.class.getName());
            }
        }

    }

}
