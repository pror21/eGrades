/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.service.Impl;

import gr.roropoulos.egrades.domain.University;
import gr.roropoulos.egrades.parser.Impl.UniversitiesParserImpl;
import gr.roropoulos.egrades.parser.UniversitiesParser;
import gr.roropoulos.egrades.service.UniversityService;

import java.util.List;

public class UniversityServiceImpl implements UniversityService {

    public List<University> getUniversitiesList(){
        UniversitiesParser uniParser = new UniversitiesParserImpl();
        return uniParser.parseUniDB();
    }
}
