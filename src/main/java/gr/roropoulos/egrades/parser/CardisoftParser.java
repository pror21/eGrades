/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.parser;

import gr.roropoulos.egrades.domain.Course;
import gr.roropoulos.egrades.domain.Student;
import org.jsoup.nodes.Document;

public interface CardisoftParser {

    Student parseStudentInfo(Student student);
    Student parseStudentGrades(Student student);
    Student parseStudentRegistration(Student student);

    Course parseCourse(Course course);
}
