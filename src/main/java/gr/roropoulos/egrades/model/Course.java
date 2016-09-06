/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.model;

import java.io.Serializable;

public class Course implements Serializable {
    private courseType cType = courseType.SIMPLE; // Default Type

    public enum courseType {SIMPLE, COMPOSITE, PART}

    private String courseId;
    private String courseTitle;
    private String courseSemester;
    private Integer courseCredits;
    private Integer courseHours;
    private Integer courseECTS;
    private String courseExamDate;
    private String courseGrade;

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

    public String getCourseExamDate() {
        return courseExamDate;
    }

    public void setCourseExamDate(String courseExamDate) {
        this.courseExamDate = courseExamDate;
    }

    public String getCourseGrade() {
        return courseGrade;
    }

    public void setCourseGrade(String courseGrade) {
        this.courseGrade = courseGrade;
    }

    public void setCourseType(courseType type) {
        this.cType = type;
    }

    public courseType getCourseType() {
        return cType;
    }
}
