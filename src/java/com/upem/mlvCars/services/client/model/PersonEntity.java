/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upem.mlvCars.services.client.model;

import com.upem.mlvCars.services.client.mlvStudents.Person;
import com.upem.mlvCars.services.client.mlvStudents.Student;
import com.upem.mlvCars.services.client.mlvTeachers.Teacher;
import java.util.Objects;

/**
 *
 * Since both person and their inherited classes (student and teacher) they have
 * no equals and hashcode methods inherited from the web service, this class 
 * act like a binding for those classes.
 * 
 * @author Davide Andrea Guastella <davide.guastella90@gmail.com>
 */
public class PersonEntity extends Person {

    
    public static PersonEntity fromStudent(Student s)
    {
        PersonEntity p = new PersonEntity();
        p.setBirthDate(s.getBirthDate());
        p.setEmail(s.getEmail());
        p.setFirstName(s.getFirstName());
        p.setLastName(s.getLastName());
        p.setId(s.getId());
        return p;
    }
    
    public static PersonEntity fromTeacher(Teacher s)
    {
        PersonEntity p = new PersonEntity();
        p.setBirthDate(s.getBirthDate());
        p.setEmail(s.getEmail());
        p.setFirstName(s.getFirstName());
        p.setLastName(s.getLastName());
        p.setId(s.getId());
        return p;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.birthDate);
        hash = 67 * hash + Objects.hashCode(this.email);
        hash = 67 * hash + Objects.hashCode(this.firstName);
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 67 * hash + Objects.hashCode(this.lastName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if (this.id != other.getId()) {
            return false;
        }
        if (!Objects.equals(this.email, other.getEmail())) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.getFirstName())) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.getLastName())) {
            return false;
        }
        if (!Objects.equals(this.birthDate, other.getBirthDate())) {
            return false;
        }
        return true;
    }
}
