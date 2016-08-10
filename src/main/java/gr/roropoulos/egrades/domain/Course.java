/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.domain;

import java.io.Serializable;
import java.util.List;

public class Course implements Serializable {

    private String courseId;
    private String courseTitle;
    private String courseSemester;
    private Integer courseCredits;
    private Integer courseHours;
    private Integer courseECTS;
    private List<Course> courseCompCourse;
    private String courseExamDate;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseSemester() {
        return courseSemester;
    }

    public void setCourseSemester(String courseSemester) {
        this.courseSemester = courseSemester;
    }

    public Integer getCourseCredits() {
        return courseCredits;
    }

    public void setCourseCredits(Integer courseCredits) {
        this.courseCredits = courseCredits;
    }

    public Integer getCourseHours() {
        return courseHours;
    }

    public void setCourseHours(Integer courseHours) {
        this.courseHours = courseHours;
    }

    public Integer getCourseECTS() {
        return courseECTS;
    }

    public void setCourseECTS(Integer courseECTS) {
        this.courseECTS = courseECTS;
    }

    public List<Course> getCourseCompCourse() {
        return courseCompCourse;
    }

    public void setCourseCompCourse(List<Course> courseCompCourse) {
        this.courseCompCourse = courseCompCourse;
    }

    public String getCourseExamDate() {
        return courseExamDate;
    }

    public void setCourseExamDate(String courseExamDate) {
        this.courseExamDate = courseExamDate;
    }

    private enum courseType {SIMPLE, COMPOSITE, PART}
}
