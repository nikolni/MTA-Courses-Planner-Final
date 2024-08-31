package com.meidanet.database.student.data;

import jakarta.persistence.*;

@Entity
public class Student {
    @Id
    private String student_id;
    private String user_name;

    public String getStudentID() {
        return student_id;
    }

    public void setStudentID(String student_id) {
        this.student_id = student_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
