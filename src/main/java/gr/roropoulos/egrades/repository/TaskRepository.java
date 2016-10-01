/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.repository;

import gr.roropoulos.egrades.model.Course;
import gr.roropoulos.egrades.model.Student;
import gr.roropoulos.egrades.parser.DocumentParser;
import gr.roropoulos.egrades.parser.Impl.CardisoftDocumentParserImpl;
import gr.roropoulos.egrades.parser.Impl.CardisoftStudentParserImpl;
import gr.roropoulos.egrades.parser.StudentParser;
import gr.roropoulos.egrades.service.Impl.SerializeServiceImpl;
import gr.roropoulos.egrades.service.SerializeService;
import javafx.concurrent.Task;
import org.jsoup.Connection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskRepository {
    private StudentParser studentParser = new CardisoftStudentParserImpl();
    private SerializeService serializeService = new SerializeServiceImpl();
    private DocumentParser documentParser = new CardisoftDocumentParserImpl();

    private Student student;
    private Map<String, String> cookieJar;

    public TaskRepository(Student student) {
        this.student = student;
    }

    public TaskRepository(Student student, Map<String, String> cookieJar) {
        this.student = student;
        this.cookieJar = cookieJar;
    }

    public Task<Map<String, String>> getCookiesTask = new Task<Map<String, String>>() {
        @Override
        public Map<String, String> call() {
            Connection.Response res = documentParser.getConnection(student.getStudentUniversity().getUniversityURL());
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

            return documentParser.getCookies(res, student.getStudentUniversity().getUniversityURL(), formData);
        }
    };

    public Task parseAndSerializeInfoTask = new Task<Void>() {
        @Override
        public Void call() {
            HashMap<String, String> infoMap = studentParser.parseStudentInfo(student.getStudentUniversity().getUniversityURL(), cookieJar);
            serializeService.serializeInfo(infoMap);
            return null;
        }
    };

    public Task parseAndSerializeCoursesTask = new Task<Void>() {
        @Override
        public Void call() {
            List<Course> courseList = studentParser.parseStudentGrades(student.getStudentUniversity().getUniversityURL(), cookieJar);
            serializeService.serializeCourses(courseList);
            return null;
        }
    };

    public Task parseAndSerializeStatsTask = new Task<Void>() {
        @Override
        public Void call() {
            HashMap<String, String> statsMap = studentParser.parseStudentStats(student.getStudentUniversity().getUniversityURL(), cookieJar);
            serializeService.serializeStats(statsMap);
            return null;
        }
    };

    public Task parseAndSerializeRegisterTask = new Task<Void>() {
        @Override
        public Void call() {
            HashMap<String, String> regMap = studentParser.parseStudentRegistration(student.getStudentUniversity().getUniversityURL(), cookieJar);
            List<Course> regList = serializeService.fetchRegisterCourseList(regMap);
            serializeService.serializeRegister(regList);
            return null;
        }
    };

    public Task<List<Course>> parseCoursesTask = new Task<List<Course>>() {
        @Override
        public List<Course> call() {
            return studentParser.parseStudentGrades(student.getStudentUniversity().getUniversityURL(), cookieJar);
        }
    };
}
