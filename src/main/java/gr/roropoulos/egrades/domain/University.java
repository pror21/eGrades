/*******************************************************************************
 Copyright (c) Panos Roropoulos - All Rights Reserved

 Unauthorized copying of this file, via any medium is strictly prohibited
 Proprietary and confidential
 Written by Panos Roropoulos <panos@roropoulos.gr>, 2016
 ******************************************************************************/

package gr.roropoulos.egrades.domain;

import java.io.Serializable;

public class University implements Serializable {

    private String universityName;
    private String universityURL;
    private String[] universityData;

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

    public String[] getUniversityData() {
        return universityData;
    }

    public void setUniversityData(String[] universityData) {
        this.universityData = universityData;
    }

    @Override
    public String toString() {
        return universityName;
    }
}
