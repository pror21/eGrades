/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class University implements Serializable {

    private String universityName;
    private String universityURL;
    private Map<String, String> universityData;
    private List<Department> universityDepartment;

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getUniversityURL() {
        return universityURL;
    }

    public void setUniversityURL(String universityURL) {
        this.universityURL = universityURL;
    }

    public Map<String, String> getUniversityData() {
        return universityData;
    }

    public void setUniversityData(Map<String, String> universityData) {
        this.universityData = universityData;
    }

    public List<Department> getUniversityDepartment() {
        return universityDepartment;
    }

    public void setUniversityDepartment(List<Department> universityDepartment) {
        this.universityDepartment = universityDepartment;
    }

    @Override
    public String toString() {
        return universityName;
    }
}
