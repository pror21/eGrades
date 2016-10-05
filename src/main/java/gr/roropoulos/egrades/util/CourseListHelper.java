/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.util;

import gr.roropoulos.egrades.model.Course;

import java.util.*;
import java.util.stream.Collectors;

public class CourseListHelper {

    public static List<Course> getCourseListByIDs(HashMap<String, String> courseIdList, List<Course> courseList) {
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

    public static List<Course> compareCourseListsAndGetNewlyAnnouncementCourses(List<Course> newList, List<Course> oldList) {
        List<Course> newGradeCourseList = new ArrayList<>();
        for (Course course : oldList) {
            newGradeCourseList.addAll(newList.stream()
                    .filter(courseNew ->
                            Objects.equals(course.getCourseId(), courseNew.getCourseId()) &&
                                    Objects.equals(course.getCourseTitle(), courseNew.getCourseTitle()))
                    .filter(courseNew ->
                            !Objects.equals(course.getCourseExamDate(), courseNew.getCourseExamDate()))
                    .collect(Collectors.toList()));
        }
        return newGradeCourseList;
    }
}
