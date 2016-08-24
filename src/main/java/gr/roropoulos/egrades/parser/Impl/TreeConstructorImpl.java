/*******************************************************************************
 Copyright (c) Panos Roropoulos - All Rights Reserved

 Unauthorized copying of this file, via any medium is strictly prohibited
 Proprietary and confidential
 Written by Panos Roropoulos <panos@roropoulos.gr>, 2016
 ******************************************************************************/

package gr.roropoulos.egrades.parser.Impl;

import gr.roropoulos.egrades.domain.University;
import gr.roropoulos.egrades.parser.TreeConstructor;
import gr.roropoulos.egrades.service.ExceptionService;
import gr.roropoulos.egrades.service.Impl.ExceptionServiceImpl;
import gr.roropoulos.egrades.service.Impl.PreferenceServiceImpl;
import gr.roropoulos.egrades.service.PreferenceService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

public class TreeConstructorImpl implements TreeConstructor {

    private ExceptionService exceptionService = new ExceptionServiceImpl();
    private PreferenceService preferenceService = new PreferenceServiceImpl();

    private Integer timeout = preferenceService.getPreferences().getPrefTimeout();

    public Map<String, String> openConnection(University uniConn, String username, String password) {
        Connection.Response res = null;
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
            Jsoup.connect(uniConn.getUniversityURL())
                    .data(uniConn.getUniversityData()[0], username, uniConn.getUniversityData()[1], password, uniConn.getUniversityData()[2], uniConn.getUniversityData()[3], uniConn.getUniversityData()[4], uniConn.getUniversityData()[5])
                    .cookies(res.cookies())
                    .method(Connection.Method.POST)
                    .timeout(timeout)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                    .post();
        } catch (IOException e) {
            exceptionService.showException(e, "Η σύνδεση με την γραμματεία απέτυχε.");
        }

        Map<String, String> cookieJar = res.cookies();
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
