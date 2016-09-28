/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.parser;

import gr.roropoulos.egrades.model.Course;
import gr.roropoulos.egrades.model.University;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface StudentParser {

    HashMap<String, String> parseStudentInfo(University studentUniversity, Map<String, String> cookieJar);

    List<Course> parseStudentGrades(University studentUniversity, Map<String, String> cookieJar);

    HashMap<String, String> parseStudentStats(University studentUniversity, Map<String, String> cookieJar);

    HashMap<String, String> parseStudentRegistration(University studentUniversity, Map<String, String> cookieJar);

}
