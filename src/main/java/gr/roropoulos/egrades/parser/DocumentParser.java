/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.parser;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

public interface DocumentParser {

    Connection.Response getConnection(String URL, Integer timeout);

    Map<String, String> getCookies(Connection.Response res, String URL, HashMap<String, String> formData, Integer timeout);

    Document getStudentInfoDocument(String URL, Map<String, String> cookieJar, Integer timeout);

    Document getStudentCoursesDocument(String URL, Map<String, String> cookieJar, Integer timeout);

    Document getStudentRegistrationDocument(String URL, Map<String, String> cookieJar, Integer timeout);

    Document getStudentStatsDocument(String URL, Map<String, String> cookieJar, Integer timeout);

}
