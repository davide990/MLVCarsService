package jsf.com.upem.mlvCars.rental;

import com.upem.mlvCars.dao.CarFacade;
import com.upem.mlvCars.model.Car;
import com.upem.mlvCars.model.Rental;
import com.upem.mlvCars.services.client.mlvStudents.Student;
import com.upem.mlvCars.services.client.mlvStudents.StudentService;
import com.upem.mlvCars.services.client.mlvStudents.StudentService_Service;
import com.upem.mlvCars.services.client.mlvTeachers.Teacher;
import com.upem.mlvCars.services.client.mlvTeachers.TeacherService;
import com.upem.mlvCars.services.client.mlvTeachers.TeacherService_Service;
import com.upem.mlvCars.services.client.model.PersonEntity;
import jsf.com.upem.mlvCars.rental.util.JsfUtil;
import jsf.com.upem.mlvCars.rental.util.PaginationHelper;
import jpa.com.upem.mlvCars.rental.RentalFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.NoResultException;

@Named("rentalController")
@SessionScoped
public class RentalController implements Serializable {

    private Rental current;
    private DataModel items = null;

    @EJB
    private jsf.com.upem.mlvCars.rental.view.ScheduleView scheduleView;

    @EJB
    private jpa.com.upem.mlvCars.rental.RentalFacade ejbFacade;

    private Car selectedCar;

    private List<Car> avaibleCar;

    private PersonEntity selectedUser;
    private List<PersonEntity> mlvUsers;

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

    public List<PersonEntity> getMlvUsers() {
        return mlvUsers;
    }

    public void setMlvUsers(List<PersonEntity> mlvUsers) {
        this.mlvUsers = mlvUsers;
    }

    public PersonEntity getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(PersonEntity selectedUser) {
        this.selectedUser = selectedUser;
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

    /**
     * Retrieve all the students by using the web service StudentService
     *
     * @return
     */
    public List<Student> retrieveMLVStudents() {
        StudentService_Service sv = new StudentService_Service();
        StudentService service = sv.getStudentServicePort();

        return service.getAllStudents();
    }

    /**
     * Retrieve all the teachers by using the web service TeacherService
     *
     * @return
     */
    public List<Teacher> retrieveMLVTeachers() {
        TeacherService_Service sv = new TeacherService_Service();
        TeacherService service = sv.getTeacherServicePort();

        return service.getAllTeachers();
    }

    /**
     * Retrieve all the students/teachers using the specific web services
     *
     * @return
     */
    public List<PersonEntity> retrieveMLVUsers() {
        List<PersonEntity> persons = new ArrayList<>();

        retrieveMLVStudents().stream().forEach((s) -> {
            persons.add(PersonEntity.fromStudent(s));
        });

        retrieveMLVTeachers().stream().forEach((t) -> {
            persons.add(PersonEntity.fromTeacher(t));
        });

        return persons;
    }

    /**
     * Check if the given person is a student or a teacher
     *
     * @param o
     * @return
     */
    public String getUserType(PersonEntity o) {
        StudentService_Service sv = new StudentService_Service();
        StudentService service = sv.getStudentServicePort();
        TeacherService_Service sv_t = new TeacherService_Service();
        TeacherService service_t = sv_t.getTeacherServicePort();

        try {
            service.getStudentByID(o.getId());
            return "Student";
        } catch (Exception e) {
        }

        try {
            service_t.getTeacherByID(o.getId());
            return "Teacher";
        } catch (Exception ee) {
        }

        return "Unknown";
    }

    public PersonEntity retrieveMLVUserByID(long id) {
        StudentService_Service sv = new StudentService_Service();
        StudentService service = sv.getStudentServicePort();
        TeacherService_Service sv_t = new TeacherService_Service();
        TeacherService service_t = sv_t.getTeacherServicePort();

        try {
            return PersonEntity.fromStudent(service.getStudentByID(id));
        } catch (Exception e) {
        }

        try {
            return PersonEntity.fromTeacher(service_t.getTeacherByID(id));
        } catch (Exception ee) {
        }

        return null;
    }

    public List<Rental> getAllRentals() {
        return getFacade().findAll();
    }

    public String prepareScheduleView() {
//        scheduleView.populate();
//        List<Rental> allRentals = getFacade().findAll();
//        for (Rental r : allRentals) {
//            scheduleView.AddRentalSchedule(r);
//        }
        return "CalendarView";
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
        mlvUsers = retrieveMLVUsers();

        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.setClient_id(selectedUser.getId());
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

    public List<Car> getAvaibleCars() {
        return carFacade.findAll();
    }

    public String getCarBrand(int carID) {
        return carFacade.find(carID).getBrand();
    }

    public String getCarModel(int carID) {
        return carFacade.find(carID).getModel();
    }

    public String update() {
        try {
            current.setClient_id(selectedUser.getId());
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
