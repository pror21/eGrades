/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.parser.Impl;

import gr.roropoulos.egrades.domain.Course;
import gr.roropoulos.egrades.domain.Student;
import gr.roropoulos.egrades.parser.CardisoftParser;
import gr.roropoulos.egrades.parser.TreeConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.Map;

public class CardisoftParserImpl implements CardisoftParser {

    private TreeConstructor treeConstructor = new TreeConstructorImpl();

    public Student parseStudentInfo(Student student) {
        Map<String, String> cookieJar = treeConstructor.openConnection(student.getStudentUniversity(), student.getStudentUsername(), student.getStudentPassword());
        Document doc = treeConstructor.getTreeStudentInfo(student.getStudentUniversity(), cookieJar);

        String studentName = doc.select("#main > div:nth-child(4) > table > tbody > tr:nth-child(3) > td:nth-child(2)").text();
        String studentSurname = doc.select("#main > div:nth-child(4) > table > tbody > tr:nth-child(2) > td:nth-child(2)").text();
        String studentAEM = doc.select("#main > div:nth-child(4) > table > tbody > tr:nth-child(4) > td:nth-child(2)").text();
        String studentDepartment = doc.select("#main > div:nth-child(4) > table > tbody > tr:nth-child(5) > td:nth-child(2)").text();
        String studentSemester = doc.select("#main > div:nth-child(4) > table > tbody > tr:nth-child(6) > td:nth-child(2)").text();

        student.setStudentName(studentName);
        student.setStudentSurname(studentSurname);
        student.setStudentAEM(studentAEM);
        student.setStudentDepartment(studentDepartment);
        student.setStudentSemester(Integer.parseInt(studentSemester));

        return student;
    }

    public Student parseStudentGrades(Student student) {
        Map<String, String> cookieJar = treeConstructor.openConnection(student.getStudentUniversity(), student.getStudentUsername(), student.getStudentPassword());
        Document doc = treeConstructor.getTreeStudentInfo(student.getStudentUniversity(), cookieJar);


        return student;
    }

    public Student parseStudentRegistration(Student student) {

        return new Student();
    }

    public Course parseCourse(Course course) {

        return new Course();
    }
}
