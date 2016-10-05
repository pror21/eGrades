/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.parser;

import gr.roropoulos.egrades.eGrades;
import gr.roropoulos.egrades.model.Department;
import gr.roropoulos.egrades.model.University;
import gr.roropoulos.egrades.service.ExceptionService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("Duplicates")
public class UniversityParser {

    public List<University> parseUniDB() {

        List<University> universitiesList = new ArrayList<>();

        try {
            InputStream xml = eGrades.class.getResourceAsStream("/unidb.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);
            doc.getDocumentElement().normalize();

            NodeList universityNodeList = doc.getElementsByTagName("university");
            for (int temp = 0; temp < universityNodeList.getLength(); temp++) {
                Node universityNode = universityNodeList.item(temp);
                if (universityNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element universityElement = (Element) universityNode;
                    Map<String, String> universityDataMap = new HashMap<>();
                    University uni = new University();

                    uni.setUniversityName(universityElement.getElementsByTagName("universityName").item(0).getTextContent());
                    uni.setUniversityURL(universityElement.getElementsByTagName("universityURL").item(0).getTextContent());

                    // parse university form data
                    NodeList universityDataNodeList = ((Element) universityNode).getElementsByTagName("data");
                    for (int tempDataNode = 0; tempDataNode < universityDataNodeList.getLength(); tempDataNode++) {
                        Node dataNode = universityDataNodeList.item(tempDataNode);
                        if (dataNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element dataNodeElement = (Element) dataNode;
                            universityDataMap.put(
                                    dataNodeElement.getElementsByTagName("key").item(0).getTextContent(),
                                    dataNodeElement.getElementsByTagName("value").item(0).getTextContent()
                            );
                        }
                    }
                    uni.setUniversityData(universityDataMap);

                    // parse any addition department form data
                    NodeList departmentNodeList = universityElement.getElementsByTagName("universityDepartment");
                    if (departmentNodeList.getLength() > 0) {
                        List<Department> departmentList = new ArrayList<>();
                        for (int tempDepNode = 0; tempDepNode < departmentNodeList.getLength(); tempDepNode++) {
                            Node departNode = departmentNodeList.item(tempDepNode);
                            if (departNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element depNodeElement = (Element) departNode;
                                Department department = new Department();
                                department.setDepartmentUniversity(uni);
                                HashMap<String, String> departmentDataMap = new HashMap<>();

                                String departmentName = depNodeElement.getElementsByTagName("universityDepartmentName").item(0).getTextContent();

                                department.setDepartmentName(departmentName);

                                NodeList departmentDataNodeList = depNodeElement.getElementsByTagName("data");
                                for (int tempDepartDataNode = 0; tempDepartDataNode < departmentDataNodeList.getLength(); tempDepartDataNode++) {
                                    Node departDataNode = departmentDataNodeList.item(tempDepartDataNode);
                                    if (departDataNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Element depDataNodeElement = (Element) departDataNode;
                                        departmentDataMap.put(
                                                depDataNodeElement.getElementsByTagName("key").item(0).getTextContent(),
                                                depDataNodeElement.getElementsByTagName("value").item(0).getTextContent()
                                        );
                                    }
                                }
                                department.setDepartmentData(departmentDataMap);
                                departmentList.add(department);
                            }
                        }
                        uni.setUniversityDepartment(departmentList);
                    }
                    universitiesList.add(uni);
                }
            }

        } catch (Exception e) {
            ExceptionService.showException(e, "Συνέβη κάποιο σφάλμα κατά την φόρτωση των πανεπιστημίων.");
        }
        return universitiesList;
    }
}
