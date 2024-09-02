package com.meidanet.database.student.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Student addStudent(List<String> studentData) {
        String studentID = studentData.get(0);
        // Check if the course name already exists
        List<Student> existingStudents = entityManager
                .createQuery("SELECT c FROM Student c WHERE c.student_id = :studentID", Student.class)
                .setParameter("studentID", studentID)
                .getResultList();

        if (existingStudents.isEmpty()) {
            // Course name does not exist, add new course
            Student student = new Student();
            student.setUser_name(studentData.get(1));
            student.setStudentID(studentData.get(0));

            entityManager.persist(student);
            return student;
        } else {
            // Handle the case when the course name is already in the database
            return null;
        }
    }

    public boolean isStudentRegistered(String student_id){
        Student student = studentRepository.findByStudent_id(student_id);
        return  (student != null);
    }
}
