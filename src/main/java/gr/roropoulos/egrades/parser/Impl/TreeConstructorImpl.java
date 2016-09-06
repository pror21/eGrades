/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */


package gr.roropoulos.egrades.parser.Impl;

import gr.roropoulos.egrades.model.University;
import gr.roropoulos.egrades.parser.TreeConstructor;
import gr.roropoulos.egrades.service.ExceptionService;
import gr.roropoulos.egrades.service.Impl.ExceptionServiceImpl;
import gr.roropoulos.egrades.service.Impl.PreferenceServiceImpl;
import gr.roropoulos.egrades.service.PreferenceService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Map;

public class TreeConstructorImpl implements TreeConstructor {

    private ExceptionService exceptionService = new ExceptionServiceImpl();
    private PreferenceService preferenceService = new PreferenceServiceImpl();
    private Integer timeout = preferenceService.getPreferences().getPrefTimeout();

    public Map<String, String> openConnection(University uniConn, String username, String password) {
        Connection.Response res = null;
        Document respDoc = null;
        try {
            res = Jsoup.connect(uniConn.getUniversityURL())
                    .method(Connection.Method.GET)
                    .timeout(timeout)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                    .execute();
        } catch (IOException e) {
            exceptionService.showException(e, "Η σύνδεση με την γραμματεία απέτυχε.");
        }
        try {
            respDoc = Jsoup.connect(uniConn.getUniversityURL())
                    .data(uniConn.getUniversityData()[0], username, uniConn.getUniversityData()[1], password, uniConn.getUniversityData()[2], uniConn.getUniversityData()[3], uniConn.getUniversityData()[4], uniConn.getUniversityData()[5])
                    .cookies(res.cookies())
                    .method(Connection.Method.POST)
                    .timeout(timeout)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                    .followRedirects(true)
                    .post();
        } catch (IOException e) {
            exceptionService.showException(e, "Η σύνδεση με την γραμματεία απέτυχε.");
        }

        Map<String, String> cookieJar;

        // Check if user failed to login
        Element error = respDoc.select("div.error").first();
        if (error != null) {
            cookieJar = null;
            exceptionService.showException(new Exception(), "Η ταυτοποιήση του χρήστη με τη γραμματεία απέτυχε. Ελέξτε το όνομα χρήστη και τον κωδικό.");
        } else
            cookieJar = res.cookies();

        return cookieJar;
    }

    public Document getTreeStudentInfo(University uniConn, Map<String, String> cookieJar) {
        Document doc = null;
        try {
            doc = Jsoup.connect(uniConn.getUniversityURL() + "studentMain.asp")
                    .cookies(cookieJar)
                    .method(Connection.Method.GET)
                    .timeout(timeout)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                    .get();
        } catch (IOException e) {
            exceptionService.showException(e, "Η σύνδεση με την γραμματεία απέτυχε.");
        }
        return doc;
    }

    public Document getTreeStudentGrades(University uniConn, Map<String, String> cookieJar) {
        Document doc = null;
        try {
            doc = Jsoup.connect(uniConn.getUniversityURL() + "stud_CResults.asp?studPg=1&mnuid=mnu3&")
                    .data("sortBy", "ctypeID")
                    .cookies(cookieJar)
                    .method(Connection.Method.POST)
                    .timeout(timeout)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                    .post();
        } catch (IOException e) {
            exceptionService.showException(e, "Η σύνδεση με την γραμματεία απέτυχε.");
        }
        return doc;
    }

    public Document getTreeStudentRegistration(University uniConn, Map<String, String> cookieJar) {
        Document doc = null;
        try {
            doc = Jsoup.connect(uniConn.getUniversityURL() + "stud_vClasses.asp?studPg=1&mnuid=diloseis;showDil&")
                    .cookies(cookieJar)
                    .method(Connection.Method.GET)
                    .timeout(timeout)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                    .get();
        } catch (IOException e) {
            exceptionService.showException(e, "Η σύνδεση με την γραμματεία απέτυχε.");
        }
        return doc;
    }

    public Document getTreeStudentStats(University uniConn, Map<String, String> cookieJar) {
        Document doc = null;
        try {
            doc = Jsoup.connect(uniConn.getUniversityURL() + "stud_CResults.asp?studPg=1&mnuid=mnu3&")
                    .cookies(cookieJar)
                    .method(Connection.Method.GET)
                    .timeout(timeout)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                    .get();
        } catch (IOException e) {
            exceptionService.showException(e, "Η σύνδεση με την γραμματεία απέτυχε.");
        }
        return doc;
    }

    public Document getTreeLogoutStudent(University uniConn, Map<String, String> cookieJar) {
        Document doc = null;
        try {
            doc = Jsoup.connect(uniConn.getUniversityURL() + "disconnect.asp?mnuid=mnu7&")
                    .cookies(cookieJar)
                    .method(Connection.Method.GET)
                    .timeout(timeout)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                    .get();
        } catch (IOException e) {
            exceptionService.showException(e, "Η σύνδεση με την γραμματεία απέτυχε.");
        }
        return doc;
    }
}
