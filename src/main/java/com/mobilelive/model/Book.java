package com.mobilelive.model;

public class Book {

    private final Long id;
    private final String callno;
    private final String name;
    private final String author;
    private final String publisher;
    private final int quantity;
    private final int issuedNo;

    public Book(){
        this.id = null;
        this.callno = "";
        this.name = "";
        this.author = "";
        this.publisher = "";
        this.quantity = 0;
        this.issuedNo =0;
    }


    public Book(long id, String callno, String name, String author, String publisher, int quantity, int issuedNo) {
        this.id = id;
        this.callno = callno;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.quantity = quantity;
        this.issuedNo = issuedNo;
    }

    public Long getId() {
        return id;
    }

//    public void setId(long id) {
//        this.id = id;
//    }

    public String getCallno() {
        return callno;
    }

//    public void setCallno(String callno) {
//        this.callno = callno;
//    }

    public String getName() {
        return name;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    public String getAuthor() {
        return author;
    }

//    public void setAuthor(String author) {
//        this.author = author;
//    }

    public String getPublisher() {
        return publisher;
    }

//    public void setPublisher(String publisher) {
//        this.publisher = publisher;
//    }

    public int getQuantity() {
        return quantity;
    }

    public int getIssuedNo() {
        return issuedNo;
    }

//    public void setIssuedNo(int issuedNo) {
//        this.issuedNo = issuedNo;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }


}
