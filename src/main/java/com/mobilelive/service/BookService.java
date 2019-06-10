package com.mobilelive.service;

import com.mobilelive.helper.Utils;
import com.mobilelive.messages.BookMessages;
import com.mobilelive.model.Book;
import com.mobilelive.model.Student;
import com.mobilelive.model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BookService {

    private static List<Book> books = new ArrayList<>();
    private StudentService studentService = new StudentService();


    static {
        books = Utils.readBookFile.get();
    }

    public Optional<Book> getBook(Long id) {
        return books.stream()
            .filter(book -> book.getId()
                    .equals(id))
            .findFirst();
    }

    public String addBook(Book book) {
        Long currentBookId = 0L;
        Book newBook = null;
        Optional<Book> bookFind = books.stream().filter(innerBook ->
            innerBook.getCallno().equals( book.getCallno() )
        ).findFirst();

        if(!bookFind.isPresent()){
            if (books.size() > 0 ){
                Book bookInt = (Book) books.stream()
                        .max(Comparator.comparing(Book::getId)).get();
                currentBookId = bookInt.getId();
                newBook = new Book(currentBookId+1,
                        book.getCallno(),
                        book.getName(),
                        book.getAuthor(),
                        book.getPublisher(),
                        book.getQuantity(),
                        book.getIssuedNo()) ;
            }
            books.add(book);
            Utils.updateBookFile(books);
            return "Book is added";
        } else {
            return "Book 'callNo: "+book.getCallno()+"' is already assign.";
        }
    }

    public List<Book> getBooks(){
        return books;
    }

    public Optional<Book> viewBook(Long id){
        return books.stream()
                .filter(book -> book.getId()
                        .equals(id))
                .findFirst();
    }

    public String deleteBook(Long id){
        Optional<Book> bookFind = books.stream()
                .filter(book -> book.getId()
                        .equals(id))
                .findFirst();
        if(bookFind.isPresent()){
            if(bookFind.get().getIssuedNo() == 0){
                books.removeIf(book -> book.getId().equals(id));
                Utils.updateBookFile(books);
                return "Book is deleted";
            } else {
                return "Book can not be delete as it issued to student";
            }
        } else {
            return "Book did not find to delete.";
        }
    }

    public String updateBook (Book book){

        Optional<Book> bookFind = books.stream().filter( innerUser -> innerUser.getId()
                .equals(book.getId()))
                .findFirst();

        if (bookFind.isPresent()){
            books = books.stream().map(bookObj -> {
                if(bookObj.getId() == book.getId()){

                    Book updatebook = new Book( book.getId(),
                            book.getCallno(),
                            book.getName(),
                            book.getAuthor(),
                            book.getPublisher(),
                            book.getQuantity(),
                            bookObj.getIssuedNo());

                    return updatebook;
                }
                return bookObj;
            }).collect( Collectors.toList());
            Utils.updateBookFile(books);
            return "Book is updated";
        } else {
            return "Book is not found";
        }


    }

    public String issueBook(Student student){
        Optional<Book> book1 = books.stream()
                .filter(book -> book.getCallno()
                        .equals(student.getCallno()))
                .findFirst();
        if (book1.isPresent() && book1.get().getQuantity() > book1.get().getIssuedNo()) {
            Student addNewStuden = new Student( student.getId(), student.getCallno(), student.getStudentName(),false,student.getStudentMobNumber(), new Date());

            books  = books.stream()
                .map(book ->{
                    if(book.getCallno()
                        .equals(student.getCallno()
                    )){
                        book = new Book(book.getId(),
                                book.getCallno(),
                                book.getName(),
                                book.getAuthor(),
                                book.getPublisher(),
                                book.getQuantity(),
                                book.getIssuedNo()+1);
                        return book;
                    }
                        return book;
                }).collect(Collectors.toList());
            studentService.getStudents().add( addNewStuden );
            Utils.updateBookFile(books );
            Utils.updateStudentFile(studentService.getStudents());
            return "Book %s Issued.";
        } else {
            return "Book is not available to Issue.";
        }
    }

    public String returnBook(Student student){
        boolean isRecodeUpdate[] = new boolean[] {true, true, false};
        studentService.setStudents( studentService.getStudents().stream()
            .map(studentObj -> {
                if(studentObj.getId() == student.getId() &&
                studentObj.getCallno().equals( student.getCallno())) {
                    studentObj = new Student(studentObj.getId(),
                            studentObj.getCallno(),
                            studentObj.getStudentName(),
                            true,
                            studentObj.getStudentMobNumber(),
                            new Date());
                        isRecodeUpdate[0] = true;
                        return studentObj;
                    }
                    return studentObj;
            }).collect( Collectors.toList() ));

        books  = books.stream()
                .map(book ->{
                    if(book.getCallno()
                            .equals(student.getCallno()
                            )){
                        int issuedBooks = book.getIssuedNo() > 1 ? book.getIssuedNo()-1 : 0;
                        book = new Book(book.getId(),
                                book.getCallno(),
                                book.getName(),
                                book.getAuthor(),
                                book.getPublisher(),
                                book.getQuantity(),
                                issuedBooks);
                        isRecodeUpdate[1] = true;
                    }
                    return book;
                }).collect(Collectors.toList());
        if (isRecodeUpdate[0] && isRecodeUpdate[1]){
            Utils.updateBookFile(books );
            Utils.updateStudentFile(studentService.getStudents());
            return "Book return and updated records";
        } else if (isRecodeUpdate[0]){
            Utils.updateStudentFile(studentService.getStudents());
            return "Student Record updated";
        } else if(isRecodeUpdate[1]){
            Utils.updateBookFile(books );
            return "Book Record updated";
        } else {
            return "Book return fails and record is not updated";
        }
    }

}
