/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.model;

import java.io.Serializable;
import java.util.Map;

public class Department implements Serializable {

    private University departmentUniversity;
    private String departmentName;
    private Map<String, String> departmentData;

    public University getDepartmentUniversity() {
        return departmentUniversity;
    }

    public void setDepartmentUniversity(University departmentUniversity) {
        this.departmentUniversity = departmentUniversity;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Map<String, String> getDepartmentData() {
        return departmentData;
    }

    public void setDepartmentData(Map<String, String> departmentData) {
        this.departmentData = departmentData;
    }

    @Override
    public String toString() {
        return departmentName;
    }
}
