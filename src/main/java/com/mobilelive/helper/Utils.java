package com.mobilelive.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobilelive.model.Book;
import com.mobilelive.model.Student;
import com.mobilelive.model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Utils {

    static ObjectMapper mapper = new ObjectMapper();
    private static String bookFileName = "books.json";
    private static String userFileName = "users.json";
    private static String studentFileName = "students.json";

    public static Supplier<List<Book>> readBookFile = () -> {

        List<Book> books = new ArrayList<>();
        try{
            File file = new File(bookFileName);
            if (!file.exists()){
                File.createTempFile("books", ".json");
            }
            InputStream fileStream = new FileInputStream(bookFileName);
            books = new java.util.ArrayList<>( Arrays.asList(mapper.readValue(fileStream, Book[].class)));
        } catch (Exception  ex) {
            books = new ArrayList<>();
        }
        return books;
    };

    public static Supplier<List<User>> readUsersFile = ()  -> {
        List<User> users = new ArrayList<>();
        try{
            InputStream fileStream = new FileInputStream(userFileName);
            users = new java.util.ArrayList<>( Arrays.asList(mapper.readValue(fileStream, User[].class)));
        } catch (Exception  ex) {
            throw new RuntimeException( ex );
        }
        return users;
    };

    public static Supplier<List<Student>> readStudentFile = () -> {
        List<Student> students = new ArrayList<>();
        try{
            File file = new File(studentFileName);
            if (!file.exists()){
                File.createTempFile("students", ".json");
            }
            InputStream fileStream = new FileInputStream(studentFileName);
            students = new java.util.ArrayList<>( Arrays.asList(mapper.readValue(fileStream, Student[].class)));
        } catch (Exception  ex) {
            students = new ArrayList<>();
        }
        return students;
    };

    public static void updateUserFile  (List<User> updatedUserList) {
        try{
            updateJsonFile(mapper.writeValueAsString(updatedUserList), userFileName);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void updateBookFile  (List<Book> updatedUserList) {
        try{
            updateJsonFile(mapper.writeValueAsString(updatedUserList), bookFileName);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void updateStudentFile  (List<Student> updatedStudentList) {
        try{
            updateJsonFile(mapper.writeValueAsString(updatedStudentList), studentFileName);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static void updateJsonFile(String updateJsonString, String fileName){
        try{
            FileWriter fileWriter = new FileWriter( fileName);
            fileWriter.write( updateJsonString);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
