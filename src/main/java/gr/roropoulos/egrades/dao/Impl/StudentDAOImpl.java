/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.dao.Impl;

import gr.roropoulos.egrades.dao.StudentDAO;
import gr.roropoulos.egrades.model.Student;
import gr.roropoulos.egrades.service.ExceptionService;

import java.io.*;

public class StudentDAOImpl implements StudentDAO {
    private final static String path = System.getProperty("user.home") + File.separator + "eGrades" + File.separator + "stud.ser";

    public void saveStudent(Student student) {
        File directory = new File(String.valueOf(System.getProperty("user.home") + File.separator + "eGrades"));
        boolean isDirectoryCreated = directory.exists();
        if (!isDirectoryCreated) {
            isDirectoryCreated = directory.mkdir();
        }
        if (isDirectoryCreated) {
            try {
                FileOutputStream fileOut = new FileOutputStream(path);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(student);
                out.close();
                fileOut.close();
            } catch (IOException i) {
                ExceptionService.showException(i, "Η αποθήκευση του χρήστη απέτυχε. Τοποθεσία: " + System.getProperty("user.home") + File.separator + "eGrades" + File.separator + "stud.ser");
            }
        } else
            ExceptionService.showException(new Exception(), "Η δημιουργία του φακέλου eGrades στη τοποθεσία: " + System.getProperty("user.home") + " απέτυχε.");
    }

    public Student getStudent() {
        Student student;
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            student = (Student) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            ExceptionService.showException(i, "Υπήρξε πρόβλημα με την ανάκτηση του χρήστη: " + System.getProperty("user.home") + File.separator + "eGrades" + File.separator + "stud.ser");
            return new Student();
        } catch (ClassNotFoundException c) {
            ExceptionService.showException(c, "Δεν βρέθηκε η κλάση Student.");
            return new Student();
        }
        return student;
    }

    public void deleteStudent() {
        File tempFile = new File(path);
        boolean isFileExists = tempFile.exists();
        if (isFileExists) {
            try {
                tempFile.delete();
            } catch (Exception e) {
                ExceptionService.showException(e, "Η διαγραφή των δεδομένων του φοιτητή απέτυχε.");
            }
        }
    }

    public Boolean checkIfStudentExists() {
        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }
}
