/*******************************************************************************
 Copyright (c) Panos Roropoulos - All Rights Reserved

 Unauthorized copying of this file, via any medium is strictly prohibited
 Proprietary and confidential
 Written by Panos Roropoulos <panos@roropoulos.gr>, 2016
 ******************************************************************************/

package gr.roropoulos.egrades.parser;

import gr.roropoulos.egrades.domain.University;
import org.jsoup.nodes.Document;

import java.util.Map;

public interface TreeConstructor {

    Map<String, String> openConnection(University uniConn, String username, String password);

    Document getTreeStudentInfo(University uniConn, Map<String, String> cookieJar);

    Document getTreeStudentGrades(University uniConn, Map<String, String> cookieJar);

    Document getTreeStudentRegistration(University uniConn, Map<String, String> cookieJar);

    Document getTreeLogoutStudent(University uniConn, Map<String, String> cookieJar);

}
