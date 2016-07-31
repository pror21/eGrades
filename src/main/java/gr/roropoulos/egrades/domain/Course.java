/*******************************************************************************
 Copyright (c) Panos Roropoulos - All Rights Reserved

 Unauthorized copying of this file, via any medium is strictly prohibited
 Proprietary and confidential
 Written by Panos Roropoulos <panos@roropoulos.gr>, 2016
 ******************************************************************************/

package gr.roropoulos.egrades.domain;

import java.io.Serializable;
import java.util.List;

public class Course implements Serializable {

    private enum courseType { SIMPLE, COMPOSITE, PART }
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
}
