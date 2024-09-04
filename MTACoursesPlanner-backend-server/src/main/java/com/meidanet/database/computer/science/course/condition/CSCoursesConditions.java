package com.meidanet.database.computer.science.course.condition;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CSCoursesConditions {
    @Id
    private String course_id_name;
    private String prerequisite;
    private String pre_exchangeable;
    private String parallel_condition;
    private String parallel_exchangeable;
    private String exclusive_condition;


    public String getCourse_id_name() {
        return course_id_name;
    }

    public void setCourse_id_name(String course_id_name) {
        this.course_id_name = course_id_name;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
    }

    public String getParallel_condition() {
        return parallel_condition;
    }

    public void setParallel_condition(String parallel_condition) {
        this.parallel_condition = parallel_condition;
    }

    public String getExclusive_condition() {
        return exclusive_condition;
    }

    public void setExclusive_condition(String exclusive_condition) {
        this.exclusive_condition = exclusive_condition;
    }

    public String getPre_exchangeable() {
        return pre_exchangeable;
    }

    public void setPre_exchangeable(String pre_exchangeable) {
        this.pre_exchangeable = pre_exchangeable;
    }

    public String getParallel_exchangeable() {
        return parallel_exchangeable;
    }

    public void setParallel_exchangeable(String parallel_exchangeable) {
        this.parallel_exchangeable = parallel_exchangeable;
    }
}
