/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.task;

import gr.roropoulos.egrades.model.Course;
import gr.roropoulos.egrades.model.Student;
import gr.roropoulos.egrades.parser.DocumentParser;
import gr.roropoulos.egrades.parser.Impl.CardisoftDocumentParserImpl;
import gr.roropoulos.egrades.parser.Impl.CardisoftStudentParserImpl;
import gr.roropoulos.egrades.parser.StudentParser;
import gr.roropoulos.egrades.service.StudentService;
import gr.roropoulos.egrades.util.CourseListHelper;
import javafx.concurrent.Task;
import org.jsoup.Connection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentTasks {
    private final StudentParser studentParser = new CardisoftStudentParserImpl();
    private final DocumentParser documentParser = new CardisoftDocumentParserImpl();
    private final StudentService studentService = new StudentService();

    private final Student student;
    private final Integer timeout;

    private Map<String, String> cookieJar;

    public StudentTasks(Student student, Integer timeout) {
        this.student = student;
        this.timeout = timeout;
    }

    public StudentTasks(Student student, Map<String, String> cookieJar, Integer timeout) {
        this.student = student;
        this.cookieJar = cookieJar;
        this.timeout = timeout;
    }

    public final Task<Map<String, String>> getCookiesTask = new Task<Map<String, String>>() {
        @Override
        public Map<String, String> call() {
            Connection.Response res = documentParser.getConnection(student.getStudentUniversity().getUniversityURL(), timeout);
            HashMap<String, String> formData = new HashMap<>();
            formData.putAll(student.getStudentUniversity().getUniversityData());

            if (student.getStudentUniversityDepartment() != null)
                formData.putAll(student.getStudentUniversityDepartment().getDepartmentData());

            String usernameKey = formData.get("username");
            formData.remove("username");
            formData.put(usernameKey, student.getStudentUsername());
            String passwordKey = formData.get("password");
            formData.remove("password");
            formData.put(passwordKey, student.getStudentPassword());

            return documentParser.getCookies(res, student.getStudentUniversity().getUniversityURL(), formData, timeout);
        }
    };

    public final Task parseAndSerializeInfoTask = new Task<Void>() {
        @Override
        public Void call() {
            HashMap<String, String> infoMap = studentParser.parseStudentInfo(student.getStudentUniversity().getUniversityURL(), cookieJar, timeout);
            studentService.setStudentInfo(infoMap);
            return null;
        }
    };

    public final Task parseAndSerializeCoursesTask = new Task<Void>() {
        @Override
        public Void call() {
            List<Course> courseList = studentParser.parseStudentGrades(student.getStudentUniversity().getUniversityURL(), cookieJar, timeout);
            studentService.setStudentCourses(courseList);
            return null;
        }
    };

    public final Task parseAndSerializeStatsTask = new Task<Void>() {
        @Override
        public Void call() {
            HashMap<String, String> statsMap = studentParser.parseStudentStats(student.getStudentUniversity().getUniversityURL(), cookieJar, timeout);
            studentService.setStudentOverallStats(statsMap);
            return null;
        }
    };

    public final Task parseAndSerializeRegisterTask = new Task<Void>() {
        @Override
        public Void call() {
            HashMap<String, String> regMap = studentParser.parseStudentRegistration(student.getStudentUniversity().getUniversityURL(), cookieJar, timeout);
            List<Course> regList = CourseListHelper.getCourseListByIDs(regMap, studentService.getStudentCourses());
            studentService.setStudentRegisterCourseList(regList);
            return null;
        }
    };

    public final Task<List<Course>> parseCoursesTask = new Task<List<Course>>() {
        @Override
        public List<Course> call() {
            return studentParser.parseStudentGrades(student.getStudentUniversity().getUniversityURL(), cookieJar, timeout);
        }
    };
}
