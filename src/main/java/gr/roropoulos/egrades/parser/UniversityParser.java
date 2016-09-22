/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.parser;

import gr.roropoulos.egrades.model.University;
import gr.roropoulos.egrades.service.ExceptionService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UniversityParser {

    private ExceptionService exceptionService = new ExceptionService();

    public List<University> parseUniDB() {

        List<University> universitiesList = new ArrayList<>();

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File xml = new File(classLoader.getResource("unidb.xml").getFile());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("university");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    University uni = new University();
                    uni.setUniversityName(eElement.getElementsByTagName("universityName").item(0).getTextContent());
                    uni.setUniversityURL(eElement.getElementsByTagName("universityURL").item(0).getTextContent());
                    String dataString = eElement.getElementsByTagName("universityData").item(0).getTextContent();
                    String[] dataArray = dataString.split("\\s+");
                    for (int i = 0; i < dataArray.length; i++) {
                        dataArray[i] = dataArray[i].replaceAll("[^\\w]", "");
                    }
                    uni.setUniversityData(dataArray);
                    universitiesList.add(uni);
                }
            }
        } catch (Exception e) {
            exceptionService.showException(e, "Συνέβη κάποιο σφάλμα κατά την φόρτωση των πανεπιστημίων.");
        }
        return universitiesList;
    }

}
