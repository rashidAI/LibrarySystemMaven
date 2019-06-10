package com.mobilelive.service;

import com.mobilelive.helper.Utils;
import com.mobilelive.model.Student;
import com.mobilelive.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentService {

    private static List<Student> students = new ArrayList<>();

    static {
        students = Utils.readStudentFile.get();
    }


    public List<Student> getStudents(){
        return students;
    }

    public void setStudents(List<Student> students){
        this.students = students;
    }

}
