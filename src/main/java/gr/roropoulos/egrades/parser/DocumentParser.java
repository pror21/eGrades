/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.parser;

import gr.roropoulos.egrades.model.University;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import java.util.Map;

public interface DocumentParser {

    Connection.Response getConnection(University uniConn);

    Map<String, String> getCookies(Connection.Response res, University uniConn, String username, String password);

    Boolean checkAuthentication(Connection.Response res, University uniConn, String username, String password);

    Document getTreeStudentInfo(University uniConn, Map<String, String> cookieJar);

    Document getTreeStudentGrades(University uniConn, Map<String, String> cookieJar);

    Document getTreeStudentRegistration(University uniConn, Map<String, String> cookieJar);

    Document getTreeStudentStats(University uniConn, Map<String, String> cookieJar);

}
