/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.service;

import gr.roropoulos.egrades.model.Course;
import gr.roropoulos.egrades.model.Student;

import java.util.HashMap;
import java.util.List;

public interface SerializeService {

    // Serialize Student Methods
    void serializeStudent(Student student);

    void serializeCourses(List<Course> courseList);

    void serializeStats(HashMap<String, String> studentStats);

    void serializeLastRegister(HashMap<String, String> regCourseMap);

    void serializeInfo(HashMap<String, String> studentInfo);

    void serializeRecentCourses(List<Course> courseList);

    // De-Serialize Student Methods
    Student deserializeStudent();

    List<Course> deserializeCourses();

    HashMap<String, String> deserializeStats();

    List<Course> deserializeLastRegisterCourseList();

    HashMap<String, String> deserializeInfo();

    List<Course> deserializeRecentCourses();

    // Misc Methods
    Boolean checkIfSerializedFileExist();

    Boolean studentCheckAuthentication(Student student);

    void deleteSerializedFile();

}
