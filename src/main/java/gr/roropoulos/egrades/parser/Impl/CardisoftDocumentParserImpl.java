/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */


package gr.roropoulos.egrades.parser.Impl;

import gr.roropoulos.egrades.parser.DocumentParser;
import gr.roropoulos.egrades.service.ExceptionService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CardisoftDocumentParserImpl implements DocumentParser {

    public Connection.Response getConnection(String URL, Integer timeout) {
        Connection.Response res = null;
        try {
            res = Jsoup.connect(URL)
                    .method(Connection.Method.GET)
                    .timeout(timeout)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                    .execute();
        } catch (IOException e) {
            ExceptionService.showException(e, "Η σύνδεση με την γραμματεία απέτυχε.");
        }
        return res;
    }

    public Map<String, String> getCookies(Connection.Response res, String URL, HashMap<String, String> formData, Integer timeout) {
        Document responseDoc = null;

        try {
            responseDoc = Jsoup.connect(URL + "login.asp")
                    .data(formData)
                    .cookies(res.cookies())
                    .method(Connection.Method.POST)
                    .timeout(timeout)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                    .followRedirects(true)
                    .post();
        } catch (IOException e) {
            ExceptionService.showException(e, "Η σύνδεση με την γραμματεία απέτυχε.");
        }

        Element errorElement = responseDoc != null ? responseDoc.select("div.error").first() : null;
        if (errorElement != null) {
            return null;
        }
        return res.cookies();
    }

    public Document getStudentInfoDocument(String URL, Map<String, String> cookieJar, Integer timeout) {
        Document doc = null;
        try {
            doc = Jsoup.connect(URL + "studentMain.asp")
                    .cookies(cookieJar)
                    .method(Connection.Method.GET)
                    .timeout(timeout)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                    .get();
        } catch (IOException e) {
            ExceptionService.showException(e, "Η σύνδεση με την γραμματεία απέτυχε.");
        }
        return doc;
    }

    public Document getStudentCoursesDocument(String URL, Map<String, String> cookieJar, Integer timeout) {
        Document doc = null;
        try {
            doc = Jsoup.connect(URL + "stud_CResults.asp?studPg=1&mnuid=mnu3&")
                    .data("sortBy", "ctypeID")
                    .cookies(cookieJar)
                    .method(Connection.Method.POST)
                    .timeout(timeout)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                    .post();
        } catch (IOException e) {
            ExceptionService.showException(e, "Η σύνδεση με την γραμματεία απέτυχε.");
        }
        return doc;
    }

    public Document getStudentRegistrationDocument(String URL, Map<String, String> cookieJar, Integer timeout) {
        Document doc = null;
        try {
            doc = Jsoup.connect(URL + "stud_vClasses.asp?studPg=1&mnuid=diloseis;showDil&")
                    .cookies(cookieJar)
                    .method(Connection.Method.GET)
                    .timeout(timeout)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                    .get();
        } catch (IOException e) {
            ExceptionService.showException(e, "Η σύνδεση με την γραμματεία απέτυχε.");
        }
        return doc;
    }

    public Document getStudentStatsDocument(String URL, Map<String, String> cookieJar, Integer timeout) {
        Document doc = null;
        try {
            doc = Jsoup.connect(URL + "stud_CResults.asp?studPg=1&mnuid=mnu3&")
                    .cookies(cookieJar)
                    .method(Connection.Method.GET)
                    .timeout(timeout)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                    .get();
        } catch (IOException e) {
            ExceptionService.showException(e, "Η σύνδεση με την γραμματεία απέτυχε.");
        }
        return doc;
    }

}
