package com.mobilelive.model;

import java.util.Date;

public class Student {

    private final Long id;
    private final String callno;
    private final String studentName;
    private final boolean returnStatus;
    private final int studentMobNumber;
    private final Date issuedDate;

    public Student() {
        this.callno = "";
        this.studentName = "";
        this.id = null;
        this.issuedDate = null;
        this.studentMobNumber = 0;
        this.returnStatus = false;
    }


    public Student(long id, String callno, String studentName, boolean returnStatus, int studentMobNumber, Date issuedDate) {
        this.id = id;
        this.callno = callno;
        this.studentName = studentName;
        this.returnStatus = returnStatus;
        this.studentMobNumber = studentMobNumber;
        this.issuedDate = issuedDate;
    }

    public Long getId() {
        return id;
    }

    public String getCallno() {
        return callno;
    }

    public String getStudentName() {
        return studentName;
    }

    public boolean isReturnStatus() {
        return returnStatus;
    }

    public int getStudentMobNumber() {
        return studentMobNumber;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }
}
