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

public interface StudentService {

    void studentSerialize(Student student);

    Student studentDeSerialize();

    Boolean studentCheckAuthentication(Student student);

    Boolean studentCheckIfExist();

    void studentSetInfo(HashMap<String, String> studentInfo);

    HashMap<String, String> studentGetInfo();

    void studentSetAllCourses(List<Course> courseList);

    List<Course> studentGetAllCourses();

    void studentSetStats(HashMap<String, String> studentStats);

    HashMap<String, String> studentGetStats();

    void studentSetSingleCourse(Course course);

    Course studentGetSingleCourse(String courseId);

    HashMap<String, String> studentGetLastReg();

    List<Course> studentGetLastRegCourseList();

    void studentSetLastReg(HashMap<String, String> regCourseMap);

    void studentDelete();

}
