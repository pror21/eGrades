/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.service.Impl;

import gr.roropoulos.egrades.domain.Student;
import gr.roropoulos.egrades.service.ExceptionService;
import gr.roropoulos.egrades.service.StudentService;

import java.io.*;

public class StudentServiceImpl implements StudentService {

    private ExceptionService exceptionService = new ExceptionServiceImpl();
    private String path = System.getProperty("user.home") + File.separator + "eGrades" + File.separator + "stud.ser";

    public void studentSerialize(Student student) {
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(student);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            exceptionService.showException(i, "Η αποθήκευση του χρήστη απέτυχε. Τοποθεσία: " + System.getProperty("user.home") + File.separator + "eGrades" + File.separator + "stud.ser");
        }
    }

    public Student studentDeSerialize() {
        Student student = null;
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            student = (Student) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            exceptionService.showException(i, "Δεν βρέθηκε το αρχείο χρήστη στη τοποθεσία: " + System.getProperty("user.home") + File.separator + "eGrades" + File.separator + "stud.ser");
            return student;
        } catch (ClassNotFoundException c) {
            exceptionService.showException(c, "Δεν βρέθηκε η κλάση Student.");
            return student;
        }
        return student;
    }

    public Boolean studentCheckIfExist() {
        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }

    public void studentDelete() {
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            exceptionService.showException(e, "Η διαγραφή των δεδομένων του φοιτητή απέτυχε.");
        }
    }
}
