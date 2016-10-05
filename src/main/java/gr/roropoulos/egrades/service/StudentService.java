/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.service;

import gr.roropoulos.egrades.dao.Impl.StudentDAOImpl;
import gr.roropoulos.egrades.dao.StudentDAO;
import gr.roropoulos.egrades.model.Course;
import gr.roropoulos.egrades.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentService {

    private final StudentDAO studentDAO = new StudentDAOImpl();

    public void setStudent(Student student) {
        studentDAO.saveStudent(student);
    }

    public void setStudentCourses(List<Course> courseList) {
        Student student = studentDAO.getStudent();
        student.setStudentCourses(courseList);
        studentDAO.saveStudent(student);
    }

    public void setStudentRegisterCourseList(List<Course> registerCourses) {
        Student student = studentDAO.getStudent();
        student.setStudentRegister(registerCourses);
        studentDAO.saveStudent(student);
    }

    public void setStudentOverallStats(HashMap<String, String> studentStats) {
        Student student = studentDAO.getStudent();
        student.setStudentStats(studentStats);
        studentDAO.saveStudent(student);
    }

    public void setStudentInfo(HashMap<String, String> studentInfo) {
        Student student = studentDAO.getStudent();
        student.setStudentName(studentInfo.get("studentName"));
        student.setStudentSurname(studentInfo.get("studentSurname"));
        student.setStudentAEM(studentInfo.get("studentAEM"));
        student.setStudentDepartment(studentInfo.get("studentDepartment"));
        student.setStudentSemester(studentInfo.get("studentSemester"));
        studentDAO.saveStudent(student);
    }

    public void insertStudentRecentCourses(List<Course> courseList) {
        Student student = studentDAO.getStudent();
        List<Course> recentCourseList = student.getStudentRecentCourses();
        if (recentCourseList == null)
            recentCourseList = new ArrayList<>();
        recentCourseList.addAll(courseList);
        student.setStudentRecentCourses(recentCourseList);
        studentDAO.saveStudent(student);
    }

    public Student getStudent() {
        return studentDAO.getStudent();
    }

    public List<Course> getStudentCourses() {
        return studentDAO.getStudent().getStudentCourses();
    }

    public List<Course> getStudentRegister() {
        return studentDAO.getStudent().getStudentRegister();
    }

    public HashMap<String, String> getStudentOverallStats() {
        return studentDAO.getStudent().getStudentStats();
    }

    public HashMap<String, String> getStudentInfo() {
        Student student = studentDAO.getStudent();
        HashMap<String, String> studentInfoHashMap = new HashMap<>();
        studentInfoHashMap.put("studentName", student.getStudentName());
        studentInfoHashMap.put("studentSurname", student.getStudentSurname());
        studentInfoHashMap.put("studentAEM", student.getStudentAEM());
        studentInfoHashMap.put("studentDepartment", student.getStudentDepartment());
        studentInfoHashMap.put("studentSemester", student.getStudentSemester());
        return studentInfoHashMap;
    }

    public List<Course> getStudentRecentCourses() {
        return studentDAO.getStudent().getStudentRecentCourses();
    }

    public void deleteStudentRecentCourses() {
        Student student = studentDAO.getStudent();
        List<Course> recentList = student.getStudentRecentCourses();
        if (recentList != null) {
            if (!recentList.isEmpty()) {
                recentList.clear();
                student.setStudentRecentCourses(recentList);
                studentDAO.saveStudent(student);
            }
        }
    }

    public Boolean checkIfStudentExists() {
        return studentDAO.checkIfStudentExists();
    }

    public void deleteStudent() {
        studentDAO.deleteStudent();
    }
}
