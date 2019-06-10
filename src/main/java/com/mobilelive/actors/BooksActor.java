package com.mobilelive.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.FI;
import com.mobilelive.messages.BookMessages;
import com.mobilelive.service.BookService;

public class BooksActor extends AbstractActor{

    private BookService bookService = new BookService();

    public static Props props() {
        return Props.create(BooksActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(BookMessages.AddBookMessage.class, handleAddBook())
                .match(BookMessages.GetBookMessage.class, handleGetBook())
                .match(BookMessages.GetBooksMessage.class, handleGetBooks())
                .match(BookMessages.IssueBookMessage.class, handleIssueBooks())
                .match(BookMessages.ReturnBookMessage.class, handleReturnBooks())
                .match(BookMessages.DeleteBookMessage.class, handleDeleteBook())
                .match(BookMessages.UpdateBookMessage.class, handleUpdateBook())
                .build();
    }

    private FI.UnitApply<BookMessages.AddBookMessage> handleAddBook() {
        return createBookMessage -> {
            sender().tell(new BookMessages.ActionPerformed(
                    bookService.addBook(createBookMessage.getBook())), getSelf());
        };
    }

    private FI.UnitApply<BookMessages.GetBookMessage> handleGetBook() {
        return getUserMessage -> {
            sender().tell(bookService.getBook(getUserMessage.getBookId()), getSelf());
        };
    }

    private FI.UnitApply<BookMessages.GetBooksMessage> handleGetBooks() {
        return getUserMessage -> {
            sender().tell(bookService.getBooks(), getSelf());
        };
    }

    private FI.UnitApply<BookMessages.IssueBookMessage> handleIssueBooks() {
        return issueBookMessage -> {
            sender().tell(new BookMessages.ActionPerformed(
                            bookService.issueBook(issueBookMessage.getStudentToIssueBook())), getSelf());
        };
    }

    private FI.UnitApply<BookMessages.ReturnBookMessage> handleReturnBooks() {
        return returnBookMessage -> {
            sender().tell(new BookMessages.ActionPerformed(
                            bookService.returnBook(returnBookMessage.getStudentToReturnBook())), getSelf());
        };
    }

    private FI.UnitApply<BookMessages.DeleteBookMessage> handleDeleteBook() {
        return deleteBookMessage -> {
            sender().tell(new BookMessages.ActionPerformed(
                    bookService.deleteBook(deleteBookMessage.getBookId())), getSelf());
        };
    }

    private FI.UnitApply<BookMessages.UpdateBookMessage> handleUpdateBook() {
        return updateBookMessage -> {
            sender().tell(new BookMessages.ActionPerformed(
                    bookService.updateBook(updateBookMessage.getBook())), getSelf());
        };
    }
}
