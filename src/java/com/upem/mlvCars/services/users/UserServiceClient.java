/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upem.mlvCars.services.users;

import com.upem.mlvCars.services.client.mlvStudents.Student;
import com.upem.mlvCars.services.client.mlvStudents.StudentService;
import com.upem.mlvCars.services.client.mlvStudents.StudentService_Service;
import com.upem.mlvCars.services.client.mlvTeachers.Teacher;
import com.upem.mlvCars.services.client.mlvTeachers.TeacherService;
import com.upem.mlvCars.services.client.mlvTeachers.TeacherService_Service;
import com.upem.mlvCars.services.client.model.PersonEntity;
import java.util.List;

/**
 *
 * @author Davide Andrea Guastella <davide.guastella90@gmail.com>
 */
public class UserServiceClient {

    /**
     * Check if the given person is a student or a teacher
     *
     * @param o
     * @return
     */
    public static String getUserType(PersonEntity o) {
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

    public static PersonEntity retrieveMLVUserByID(int id) {
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

    public static boolean userHasEnoughMoney(int user_id, int purchase_amount) {
        return userHasEnoughMoney(retrieveMLVUserByID(user_id), purchase_amount);
    }

    public static boolean userHasEnoughMoney(PersonEntity o, int purchase_amount) {
        if (getUserType(o).equals("Student")) {
            return studentHasEnoughMoney(o.getId(), purchase_amount);
        }
        return teacherHasEnoughMoney(o.getId(), purchase_amount);
    }

    private static boolean studentHasEnoughMoney(int student_id, int purchase_amount) {
        StudentService_Service sv = new StudentService_Service();
        StudentService service = sv.getStudentServicePort();
        return service.studentHasEnoughMoney(student_id, purchase_amount);
    }

    private static boolean teacherHasEnoughMoney(int teacher_id, int purchase_amount) {
        TeacherService_Service sv = new TeacherService_Service();
        TeacherService service = sv.getTeacherServicePort();
        return service.teacherHasEnoughMoney(teacher_id, purchase_amount);
    }

    /**
     * Retrieve all the students by using the web service StudentService
     *
     * @return
     */
    public static List<Student> retrieveMLVStudents() {
        StudentService_Service sv = new StudentService_Service();
        StudentService service = sv.getStudentServicePort();

        return service.getAllStudents();
    }

    /**
     * Retrieve all the teachers by using the web service TeacherService
     *
     * @return
     */
    public static List<Teacher> retrieveMLVTeachers() {
        TeacherService_Service sv = new TeacherService_Service();
        TeacherService service = sv.getTeacherServicePort();

        return service.getAllTeachers();
    }

}
