/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.service.Impl;

import gr.roropoulos.egrades.model.Course;
import gr.roropoulos.egrades.model.Student;
import gr.roropoulos.egrades.service.ExceptionService;
import gr.roropoulos.egrades.service.SerializeService;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class SerializeServiceImpl implements SerializeService {

    private ExceptionService exceptionService = new ExceptionService();
    private String path = System.getProperty("user.home") + File.separator + "eGrades" + File.separator + "stud.ser";

    public void serializeStudent(Student student) {
        try {
            File directory = new File(String.valueOf(System.getProperty("user.home") + File.separator + "eGrades"));
            if (!directory.exists()) directory.mkdir();

            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(student);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            exceptionService.showException(i, "Η αποθήκευση του χρήστη απέτυχε. Τοποθεσία: " + System.getProperty("user.home") + File.separator + "eGrades" + File.separator + "stud.ser");
        }
    }

    public Student deserializeStudent() {
        Student student;
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

    public Boolean checkIfSerializedFileExist() {
        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }

    public void deleteSerializedFile() {
        try {
            File file = new File(path);
            if (file.exists()) file.delete();
        } catch (Exception e) {
            exceptionService.showException(e, "Η διαγραφή των δεδομένων του φοιτητή απέτυχε.");
        }
    }

    public void serializeCourses(List<Course> courseList) {
        Student student = deserializeStudent();
        student.setStudentCourses(courseList);
        serializeStudent(student);
    }

    public List<Course> deserializeCourses() {
        return deserializeStudent().getStudentCourses();
    }

    public List<Course> fetchRegisterCourseList(HashMap<String, String> courseIdList) {
        List<Course> courseList = deserializeCourses();
        List<Course> courseRegList = new ArrayList<>();

        Iterator it = courseIdList.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            courseRegList.addAll(courseList.stream().filter(course ->
                    course.getCourseId().equals(pair.getKey()) && course.getCourseTitle().equals(pair.getValue()))
                    .collect(Collectors.toList()));
            it.remove(); // avoids a ConcurrentModificationException
        }
        return courseRegList;
    }

    public List<Course> getNewlyListedCourses(List<Course> newList) {
        List<Course> oldList = deserializeCourses();
        List<Course> newGradeCourseList = new ArrayList<>();

        for (Course course : oldList) {
            for (Course courseNew : newList) {
                if (Objects.equals(course.getCourseId(), courseNew.getCourseId()) && Objects.equals(course.getCourseTitle(), courseNew.getCourseTitle())) {
                    if (!Objects.equals(course.getCourseExamDate(), courseNew.getCourseExamDate())) {
                        newGradeCourseList.add(courseNew);
                    }
                }
            }
        }
        return newGradeCourseList;
    }

    public void serializeRegister(List<Course> registerCourses) {
        Student student = deserializeStudent();
        student.setStudentRegister(registerCourses);
        serializeStudent(student);
    }

    public List<Course> deserializeRegister() {
        return deserializeStudent().getStudentRegister();
    }

    public void serializeStats(HashMap<String, String> studentStats) {
        Student student = deserializeStudent();
        student.setStudentStats(studentStats);
        serializeStudent(student);
    }

    public HashMap<String, String> deserializeStats() {
        return deserializeStudent().getStudentStats();
    }

    public void serializeInfo(HashMap<String, String> studentInfo) {
        Student student = deserializeStudent();
        student.setStudentName(studentInfo.get("studentName"));
        student.setStudentSurname(studentInfo.get("studentSurname"));
        student.setStudentAEM(studentInfo.get("studentAEM"));
        student.setStudentDepartment(studentInfo.get("studentDepartment"));
        student.setStudentSemester(studentInfo.get("studentSemester"));
        serializeStudent(student);
    }

    public HashMap<String, String> deserializeInfo() {
        Student student = deserializeStudent();
        HashMap<String, String> studentInfoHashMap = new HashMap<>();
        studentInfoHashMap.put("studentName", student.getStudentName());
        studentInfoHashMap.put("studentSurname", student.getStudentSurname());
        studentInfoHashMap.put("studentAEM", student.getStudentAEM());
        studentInfoHashMap.put("studentDepartment", student.getStudentDepartment());
        studentInfoHashMap.put("studentSemester", student.getStudentSemester());
        return studentInfoHashMap;
    }

    public void serializeRecentCourses(List<Course> courseList) {
        Student student = deserializeStudent();
        List<Course> recentCourseList = student.getStudentRecentCourses();
        if (recentCourseList == null)
            recentCourseList = new ArrayList<>();
        recentCourseList.addAll(courseList);
        student.setStudentRecentCourses(recentCourseList);
        serializeStudent(student);
    }

    public List<Course> deserializeRecentCourses() {
        return deserializeStudent().getStudentRecentCourses();
    }

    public void clearRecentCourses() {
        Student student = deserializeStudent();
        List<Course> recentList = student.getStudentRecentCourses();
        if (recentList != null) {
            if (!recentList.isEmpty()) {
                recentList.clear();
                student.setStudentRecentCourses(recentList);
                serializeStudent(student);
            }
        }
    }
}
