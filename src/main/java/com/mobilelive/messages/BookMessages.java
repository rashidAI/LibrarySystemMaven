package com.mobilelive.messages;

import com.mobilelive.model.Book;
import com.mobilelive.model.Student;

import java.io.Serializable;

public interface BookMessages {

    class ActionPerformed implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String description;

        public ActionPerformed(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    class AddBookMessage implements Serializable {

        private static final long serialVersionUID = 1L;
        private final Book book;

        public AddBookMessage(Book book) {
            this.book = book;
        }

        public Book getBook() {
            return book;
        }
    }

    class GetBookMessage implements Serializable {

        private static final long serialVersionUID = 1L;
        private final Long bookId;

        public GetBookMessage(Long bookId) {
            this.bookId = bookId;
        }

        public Long getBookId() {
            return bookId;
        }
    }

    class UpdateBookMessage implements Serializable {

        private static final long serialVersionUID = 1L;
        private final Book book;

        public UpdateBookMessage(Book book) {
            this.book = book;
        }

        public Book getBook() {
            return book;
        }
    }

    class DeleteBookMessage implements Serializable {

        private static final long serialVersionUID = 1L;
        private final Long bookId;

        public DeleteBookMessage(Long bookId) {
            this.bookId = bookId;
        }

        public Long getBookId() {
            return bookId;
        }
    }

    class GetBooksMessage implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    class IssueBookMessage implements Serializable {

        private static final long serialVersionUID = 1L;
        private final Student student;

        public IssueBookMessage(Student student) {
            this.student = student;
        }

        public Student getStudentToIssueBook() {
            return student;
        }
    }

    class ReturnBookMessage implements Serializable {

        private static final long serialVersionUID = 1L;
        private final Student student;

        public ReturnBookMessage(Student student) {
            this.student = student;
        }

        public Student getStudentToReturnBook() {
            return student;
        }
    }
}
