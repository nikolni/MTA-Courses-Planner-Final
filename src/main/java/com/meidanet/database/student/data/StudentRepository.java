package com.meidanet.database.student.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long >
{
    @Query("SELECT c FROM Student c WHERE c.student_id = :student_id")
    List<Student> findByStudent_id(String student_id);
}
