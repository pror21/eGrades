/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.parser.Impl;

import gr.roropoulos.egrades.model.Course;
import gr.roropoulos.egrades.parser.DocumentParser;
import gr.roropoulos.egrades.parser.StudentParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardisoftStudentParserImpl implements StudentParser {

    private final DocumentParser documentParser = new CardisoftDocumentParserImpl();

    public HashMap<String, String> parseStudentInfo(String URL, Map<String, String> cookieJar) {
        Document doc = documentParser.getStudentInfoDocument(URL, cookieJar);

        String studentName = doc.select("#main > div:nth-child(4) > table > tbody > tr:nth-child(3) > td:nth-child(2)").text();
        String studentSurname = doc.select("#main > div:nth-child(4) > table > tbody > tr:nth-child(2) > td:nth-child(2)").text();
        String studentAEM = doc.select("#main > div:nth-child(4) > table > tbody > tr:nth-child(4) > td:nth-child(2)").text();
        String studentDepartment = doc.select("#main > div:nth-child(4) > table > tbody > tr:nth-child(5) > td:nth-child(2)").text();
        String studentSemester = doc.select("#main > div:nth-child(4) > table > tbody > tr:nth-child(6) > td:nth-child(2)").text();

        HashMap<String, String> studentInfoHashMap = new HashMap<>();

        studentInfoHashMap.put("studentName", studentName);
        studentInfoHashMap.put("studentSurname", studentSurname);
        studentInfoHashMap.put("studentAEM", studentAEM);
        studentInfoHashMap.put("studentDepartment", studentDepartment);
        studentInfoHashMap.put("studentSemester", studentSemester);

        return studentInfoHashMap;
    }

    public List<Course> parseStudentGrades(String URL, Map<String, String> cookieJar) {
        Document doc = documentParser.getStudentCoursesDocument(URL, cookieJar);

        // Select all SIMPLE and COMP courses (they have the same attributes so we'r selecting both of them for now)
        Elements simpleAndCompCourses = doc.select("tr[height=25][bgcolor=#fafafa]");

        // All SIMPLE,COMP,PART courses seperated
        Elements partCourses = doc.select("tr[height=15][bgcolor=#fafafa]"); // select all PART courses
        Elements simpleCourses = new Elements(); // Contains all SIMPLE courses
        Elements compCourses = new Elements(); // Contains all COMP courses

        // Seperate SIMPLE and COMP courses
        for (Element element : simpleAndCompCourses.select("img[src$=course4.gif]")) // select all SIMPLE courses by img course4.gif
        {
            Element tdImg = element.parent(); // Get parent element of 'img'
            Element compCourse = tdImg.parent(); // Get parent of parent.
            compCourses.add(compCourse);
        }

        for (Element element : simpleAndCompCourses.select("img[src$=course1.gif]")) // select all SIMPLE courses by img course1.gif
        {
            Element tdImg = element.parent(); // Get parent element of 'img'
            Element simpleCourse = tdImg.parent(); // Get parent of parent.
            simpleCourses.add(simpleCourse);
        }

        List<Course> courseList = new ArrayList<>();

        // Iterate SIMPLE courses and extract data
        for (Element element : simpleCourses) {
            Course course = new Course();

            // Get course ID and TITLE
            String courseIdName = element.select("td:eq(1)").text();
            // Remove parentheses
            courseIdName = courseIdName.replaceAll("[()]", "");
            // Seperate ID and TITLE
            String courseIdNameArr[] = courseIdName.split(" ", 2);
            // Get course SEMESTER
            String courseSemester = element.select("td:eq(2)").text();
            // Get course CREDITS (DM)
            String courseCredits = element.select("td:eq(3)").text();
            // Get course HOURS
            String courseHours = element.select("td:eq(4)").text();
            // Get course ECTS
            String courseECTS = element.select("td:eq(5)").text();
            // Get course GRADE
            String courseGrade = element.select("td:eq(6)").text();
            // Get course Exam Date
            String courseExamDate = element.select("td:eq(7)").text();

            // Finally set all data to the course entity
            course.setCourseType(Course.courseType.SIMPLE);
            // Also remove any whitespace
            course.setCourseId(courseIdNameArr[0].replace("\u00A0", ""));
            course.setCourseTitle(courseIdNameArr[1]);
            course.setCourseSemester(courseSemester);
            course.setCourseCredits(Integer.parseInt(courseCredits));
            course.setCourseHours(Integer.parseInt(courseHours));
            course.setCourseECTS(Integer.parseInt(courseECTS));
            course.setCourseGrade(courseGrade);
            course.setCourseExamDate(courseExamDate);

            // Add course into the list
            courseList.add(course);
        }

        // Iterate COMP courses and extract data
        for (Element element : compCourses) {
            Course course = new Course();

            String courseIdName = element.select("td:eq(1)").text();
            courseIdName = courseIdName.replaceAll("[()]", "");
            String courseIdNameArr[] = courseIdName.split(" ", 2);
            String courseSemester = element.select("td:eq(2)").text();
            String courseCredits = element.select("td:eq(3)").text();
            String courseHours = element.select("td:eq(4)").text();
            String courseECTS = element.select("td:eq(5)").text();
            String courseGrade = element.select("td:eq(6)").text();
            String courseExamDate = element.select("td:eq(7)").text();

            course.setCourseType(Course.courseType.COMPOSITE);
            course.setCourseId(courseIdNameArr[0].replace("\u00A0", ""));
            course.setCourseTitle(courseIdNameArr[1]);
            course.setCourseSemester(courseSemester);
            course.setCourseCredits(Integer.parseInt(courseCredits));
            course.setCourseHours(Integer.parseInt(courseHours));
            course.setCourseECTS(Integer.parseInt(courseECTS));
            course.setCourseGrade(courseGrade);
            course.setCourseExamDate(courseExamDate);

            courseList.add(course);
        }

        // Iterate PART courses and assign them to their COMP courses accordingly
        for (Element element : partCourses) {
            Course course = new Course();

            String courseIdName = element.select("td:eq(1)").text();
            courseIdName = courseIdName.replaceAll("[()]", "");
            String courseIdNameArr[] = courseIdName.split(" ", 2);
            String courseHours = element.select("td:eq(3)").text();
            String courseECTS = element.select("td:eq(4)").text();
            String courseGrade = element.select("td:eq(5)").text();
            String courseExamDate = element.select("td:eq(6)").text();

            course.setCourseType(Course.courseType.PART);
            course.setCourseId(courseIdNameArr[0].replace("\u00A0", ""));
            course.setCourseTitle(courseIdNameArr[1]);
            course.setCourseHours(Integer.parseInt(courseHours));
            course.setCourseECTS(Integer.parseInt(courseECTS));
            course.setCourseGrade(courseGrade);
            course.setCourseExamDate(courseExamDate);

            courseList.add(course);
        }

        return courseList;
    }

    public HashMap<String, String> parseStudentRegistration(String URL, Map<String, String> cookieJar) {
        Document doc = documentParser.getStudentRegistrationDocument(URL, cookieJar);

        Element element = doc.select("tr[bgcolor=#FFFAF0").first();
        HashMap<String, String> regCourseMap = new HashMap<>();
        //String regDate = regDate = element.select("span:eq(1)").text();
        // Remove junk chars
        //regDate = regDate.replaceAll(" [^ ]+$", "").trim();
        Elements courses = element.select("tr[height=25]");
        for (Element regCourse : courses) {
            String courseId = regCourse.select("td:eq(0)").text();
            courseId = courseId.replaceAll("[()]", "");
            courseId = courseId.replace("\u00A0", "");
            String courseTitle = regCourse.select("td:eq(1) > span").text();
            regCourseMap.put(courseId, courseTitle);
        }
        return regCourseMap;
    }

    public HashMap<String, String> parseStudentStats(String URL, Map<String, String> cookieJar) {
        Document doc = documentParser.getStudentStatsDocument(URL, cookieJar);

        Element overallStats = doc.select("tr[height=20][class=subHeaderBack]").last();
        HashMap<String, String> studentStats = new HashMap<>();
        studentStats.put("sumCourses", overallStats.select("td:eq(0) > b").text().replaceAll("[^\\d.]", ""));
        studentStats.put("averageGrade", overallStats.select("span:eq(0)").text());
        studentStats.put("sumCredits", overallStats.select("span:eq(1)").text());
        studentStats.put("sumHours", overallStats.select("span:eq(2)").text());
        studentStats.put("sumECTS", overallStats.select("span:eq(3)").text());

        return studentStats;
    }

}
