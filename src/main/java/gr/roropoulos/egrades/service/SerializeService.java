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

    void serializeInfo(HashMap<String, String> studentInfo);

    void serializeRecentCourses(List<Course> courseList);

    void serializeRegister(List<Course> registerCourses);

    // De-Serialize Student Methods
    Student deserializeStudent();

    List<Course> deserializeCourses();

    HashMap<String, String> deserializeStats();

    List<Course> fetchRegisterCourseList(HashMap<String, String> courseIdList);

    HashMap<String, String> deserializeInfo();

    List<Course> deserializeRecentCourses();

    List<Course> deserializeRegister();

    // Misc Methods
    Boolean checkIfSerializedFileExist();

    Boolean studentCheckAuthentication(Student student);

    void deleteSerializedFile();

    void clearRecentCourses();

}
