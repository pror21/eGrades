/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Student implements Serializable {

    private String studentUsername;
    private String studentPassword;
    private String studentName;
    private String studentSurname;
    private String studentAEM;
    private String studentDepartment;
    private String studentSemester;
    private University studentUniversity;
    private Department studentUniversityDepartment;
    private List<Course> studentCourses;
    private List<Course> studentRecentCourses;
    private List<Course> studentRegister;
    private HashMap<String, String> studentStats;

    public List<Course> getStudentRegister() {
        return studentRegister;
    }

    public void setStudentRegister(List<Course> studentRegister) {
        this.studentRegister = studentRegister;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getStudentPassword() {
        return studentPassword;
    }

    public void setStudentPassword(String studentPassword) {
        this.studentPassword = studentPassword;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentSurname() {
        return studentSurname;
    }

    public void setStudentSurname(String studentSurname) {
        this.studentSurname = studentSurname;
    }

    public String getStudentAEM() {
        return studentAEM;
    }

    public void setStudentAEM(String studentAEM) {
        this.studentAEM = studentAEM;
    }

    public String getStudentDepartment() {
        return studentDepartment;
    }

    public void setStudentDepartment(String studentDepartment) {
        this.studentDepartment = studentDepartment;
    }

    public String getStudentSemester() {
        return studentSemester;
    }

    public void setStudentSemester(String studentSemester) {
        this.studentSemester = studentSemester;
    }

    public University getStudentUniversity() {
        return studentUniversity;
    }

    public void setStudentUniversity(University studentUniversity) {
        this.studentUniversity = studentUniversity;
    }

    public List<Course> getStudentCourses() {
        return studentCourses;
    }

    public void setStudentCourses(List<Course> studentCourses) {
        this.studentCourses = studentCourses;
    }

    public HashMap<String, String> getStudentStats() {
        return studentStats;
    }

    public void setStudentStats(HashMap<String, String> studentStats) {
        this.studentStats = studentStats;
    }

    public List<Course> getStudentRecentCourses() {
        return studentRecentCourses;
    }

    public void setStudentRecentCourses(List<Course> studentRecentCourses) {
        this.studentRecentCourses = studentRecentCourses;
    }

    public Department getStudentUniversityDepartment() {
        return studentUniversityDepartment;
    }

    public void setStudentUniversityDepartment(Department studentUniversityDepartment) {
        this.studentUniversityDepartment = studentUniversityDepartment;
    }
}
