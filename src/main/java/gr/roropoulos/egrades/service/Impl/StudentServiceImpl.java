/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.service.Impl;

import gr.roropoulos.egrades.model.Course;
import gr.roropoulos.egrades.model.Student;
import gr.roropoulos.egrades.parser.Impl.TreeConstructorImpl;
import gr.roropoulos.egrades.parser.TreeConstructor;
import gr.roropoulos.egrades.service.ExceptionService;
import gr.roropoulos.egrades.service.StudentService;

import java.io.*;
import java.util.*;

public class StudentServiceImpl implements StudentService {

    private TreeConstructor treeConstructor = new TreeConstructorImpl();
    private ExceptionService exceptionService = new ExceptionServiceImpl();
    private String path = System.getProperty("user.home") + File.separator + "eGrades" + File.separator + "stud.ser";

    public void studentSerialize(Student student) {
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(student);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            exceptionService.showException(i, "Η αποθήκευση του χρήστη απέτυχε. Τοποθεσία: " + System.getProperty("user.home") + File.separator + "eGrades" + File.separator + "stud.ser");
        }
    }

    public Student studentDeSerialize() {
        Student student = null;
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            student = (Student) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            exceptionService.showException(i, "Υπήρξε πρόβλημα με την ανάκτηση του χρήστη: " + System.getProperty("user.home") + File.separator + "eGrades" + File.separator + "stud.ser");
            return new Student();
        } catch (ClassNotFoundException c) {
            exceptionService.showException(c, "Δεν βρέθηκε η κλάση Student.");
            return new Student();
        }
        return student;
    }

    public Boolean studentCheckAuthentication(Student student) {
        Map<String, String> cookieJar = treeConstructor.openConnection(student.getStudentUniversity(), student.getStudentUsername(), student.getStudentPassword());
        return cookieJar != null;
    }

    public Boolean studentCheckIfExist() {
        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }

    public void studentDelete() {
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            exceptionService.showException(e, "Η διαγραφή των δεδομένων του φοιτητή απέτυχε.");
        }
    }

    public void studentSetAllCourses(List<Course> courseList) {
        Student student = studentDeSerialize();
        student.setStudentCourses(courseList);
        studentSerialize(student);
    }

    public List<Course> studentGetAllCourses() {
        return studentDeSerialize().getStudentCourses();
    }

    public void studentSetSingleCourse(Course course) {
        Student student = studentDeSerialize();
        List<Course> courseList = student.getStudentCourses();
        for (Course c : courseList) {
            if (c.getCourseId() == course.getCourseId()) {
                c = course;
                break;
            } else
                courseList.add(course);
        }
        student.setStudentCourses(courseList);
        studentSerialize(student);
    }

    public Course studentGetSingleCourse(String courseId) {
        Student student = studentDeSerialize();
        List<Course> courseList = student.getStudentCourses();
        for (Course c : courseList) {
            if (c.getCourseId() == courseId) {
                return c;
            } else
                exceptionService.showException(new Exception(), "Το μάθημα δεν βρέθηκε.");
        }
        return new Course();
    }

    public void studentSetLastReg(HashMap<String, String> regCourseMap) {
        Student student = studentDeSerialize();
        student.setStudentLastReg(regCourseMap);
        studentSerialize(student);
    }

    public HashMap<String, String> studentGetLastReg() {
        return studentDeSerialize().getStudentLastReg();
    }

    public List<Course> studentGetLastRegCourseList() {
        Student student = studentDeSerialize();
        List<Course> courseList = student.getStudentCourses();
        HashMap<String, String> courseIdList = student.getStudentLastReg();

        List<Course> courseRegList = new ArrayList<>();

        Iterator it = courseIdList.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            for (Course course : courseList) {
                if (course.getCourseId().equals(pair.getKey()) && course.getCourseTitle().equals(pair.getValue())) {
                    courseRegList.add(course);
                }
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        return courseRegList;
    }

    public void studentSetStats(HashMap<String, String> studentStats) {
        Student student = studentDeSerialize();
        student.setStudentStats(studentStats);
        studentSerialize(student);
    }

    public HashMap<String, String> studentGetStats() {
        return studentDeSerialize().getStudentStats();
    }

    public void studentSetInfo(HashMap<String, String> studentInfo) {
        Student student = studentDeSerialize();
        student.setStudentName(studentInfo.get("studentName"));
        student.setStudentSurname(studentInfo.get("studentSurname"));
        student.setStudentAEM(studentInfo.get("studentAEM"));
        student.setStudentDepartment(studentInfo.get("studentDepartment"));
        student.setStudentSemester(studentInfo.get("studentSemester"));
        studentSerialize(student);
    }

    public HashMap<String, String> studentGetInfo() {
        Student student = studentDeSerialize();
        HashMap<String, String> studentInfoHashMap = new HashMap<>();
        studentInfoHashMap.put("studentName", student.getStudentName());
        studentInfoHashMap.put("studentSurname", student.getStudentSurname());
        studentInfoHashMap.put("studentAEM", student.getStudentAEM());
        studentInfoHashMap.put("studentDepartment", student.getStudentDepartment());
        studentInfoHashMap.put("studentSemester", student.getStudentSemester());
        return studentInfoHashMap;
    }
}
