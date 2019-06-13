package com.mobilelive.controller;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.HttpApp;
import akka.http.javadsl.server.Route;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import com.mobilelive.actors.BooksActor;
import com.mobilelive.actors.StudentActor;
import com.mobilelive.actors.UserActor;
import com.mobilelive.messages.BookMessages;
import com.mobilelive.messages.StudentMessages;
import com.mobilelive.messages.UserMessages;
import com.mobilelive.model.Book;
import com.mobilelive.model.Student;
import com.mobilelive.model.User;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static akka.http.javadsl.server.PathMatchers.longSegment;
import static akka.http.javadsl.server.PathMatchers.segment;

public class LibarayServer extends HttpApp {

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("libarayServer");
        ActorRef userActor = system.actorOf( UserActor.props(), "userActor");
        ActorRef bookActor = system.actorOf( BooksActor.props(), "bookActor" );
        ActorRef studentActor = system.actorOf( StudentActor.props(), "studentActor" );
        LibarayServer server = new LibarayServer(userActor, bookActor, studentActor);
        server.startServer("localhost", 8080, system);
    }

    private final ActorRef userActor;
    private final ActorRef bookActor;
    private final ActorRef studentActor;

    Timeout timeout = new Timeout( Duration.create(5, TimeUnit.SECONDS));

    LibarayServer(ActorRef userActor, ActorRef bookActor, ActorRef studentActor) {
        this.userActor = userActor;
        this.bookActor = bookActor;
        this.studentActor = studentActor;
    }

    @Override
    public Route routes() {
        return concat(
                path(segment("users").slash(longSegment()), id ->
                        get(() ->
                                getUser(id)).orElse(
                                delete(() ->
                                        deleteUser(id)
                                ))),
                path(segment("books").slash(longSegment()), id ->
                        get(() ->
                                getBook(id)).orElse(
                                delete(() ->
                                        deleteBook(id)
                                ))
                ),
                path("auth", () ->
                        post(() ->
                                loginUser())),
                path("usersUpdateList", () ->
                        get(() ->
                                getUsers()).orElse(
                                post(() ->
                                        postUser()).orElse(
                                        put(() ->
                                                putUser()
                                        )))),
                path("booksUpdateList", () ->
                        get(() ->
                                viewBooks()).orElse(
                                post(() ->
                                        addBook()).orElse(
                                        put(() ->
                                                putBook()
                                        )))),
                path("bookManage", () ->
                        get(() ->
                                viewStudents()).orElse(
                                post(() ->
                                        issueBook()).orElse(
                                        put(() ->
                                                returnBook())
                                )))
        );
    }


    private Route putUser(){
        return route(put(() -> entity( Jackson.unmarshaller(User.class), user -> {
            CompletionStage<UserMessages.ActionPerformed> userCreated = PatternsCS.ask(userActor, new UserMessages.UpdateUserMessage(user), timeout)
                    .thenApply(obj -> (UserMessages.ActionPerformed) obj);

            return onSuccess(() -> userCreated, performed -> {
                if (performed.equals( "User is updated" )){
                    return complete(StatusCodes.RESET_CONTENT, performed, Jackson.marshaller());
                } else {
                    return complete(StatusCodes.BAD_REQUEST, performed, Jackson.marshaller());
                }

            });
        })));
    }

    private Route getUser(Long id) {
        return get(() -> {
            CompletionStage<Optional<User>> user = PatternsCS.ask(userActor, new UserMessages.GetUserMessage(id), timeout)
                    .thenApply(obj -> (Optional<User>) obj);

            return onSuccess(() -> user, performed -> {
                if (performed.isPresent())
                    return complete( StatusCodes.OK, performed.get(), Jackson.marshaller());
                else
                    return complete(StatusCodes.NOT_FOUND);
            });
        });
    }

    private Route postUser() {
        return route(post(() ->
            entity( Jackson.unmarshaller(User.class), user -> {
            CompletionStage<UserMessages.ActionPerformed> userCreated = PatternsCS.ask(userActor, new UserMessages.CreateUserMessage(user), timeout)
                    .thenApply(obj -> (UserMessages.ActionPerformed) obj);

            return onSuccess(() -> userCreated, performed -> {
                if (performed.equals( "New User is added" )){
                    return complete(StatusCodes.CREATED, performed, Jackson.marshaller());
                } else {
                    return complete(StatusCodes.BAD_REQUEST, performed, Jackson.marshaller());
                }
            });
        })));
    }

    private Route getUsers() {
        return get(() -> {
            CompletionStage<List<User>> user = PatternsCS.ask(userActor, new UserMessages.GetUsersMessage(), timeout)
                    .thenApply(obj -> (List<User>) obj);

            return onSuccess(() -> user, performed -> {
                if (!performed.isEmpty())
                    return complete( StatusCodes.OK, performed, Jackson.marshaller());
                else
                    return complete(StatusCodes.NOT_FOUND);
            });
        });
    }

    private Route deleteUser(Long id) {
        return delete(() -> {
            CompletionStage<Boolean> user = PatternsCS.ask(userActor, new UserMessages.DeleteUserMessage(id), timeout)
                    .thenApply(obj -> (Boolean) obj);

            return onSuccess(() -> user, performed -> {
                if (performed == true)
                    return complete( StatusCodes.OK, performed, Jackson.marshaller());
                else
                    return complete(StatusCodes.NOT_FOUND);
            });
        });
    }

    private Route getBook (Long id) {
        return get(() -> {
            CompletionStage<Optional<Book>> book = PatternsCS.ask(userActor, new BookMessages.GetBookMessage(id), timeout)
                    .thenApply(obj -> (Optional<Book>) obj);

            return onSuccess(() -> book, performed -> {
                if (performed.isPresent())
                    return complete( StatusCodes.OK, performed.get(), Jackson.marshaller());
                else
                    return complete(StatusCodes.NOT_FOUND);
            });
        });
    }

    private Route viewBooks() {
        return get(() -> {
            CompletionStage<List<Book>> books = PatternsCS.ask(bookActor, new BookMessages.GetBooksMessage(), timeout)
                    .thenApply(obj -> (List<Book>) obj);

            return onSuccess(() -> books, performed -> {
                if (!performed.isEmpty())
                    return complete( StatusCodes.OK, performed, Jackson.marshaller());
                else
                    return complete(StatusCodes.NOT_FOUND);
            });
        });
    }

    private Route addBook() {
        return route(post(() ->
            entity( Jackson.unmarshaller(Book.class), book -> {
            CompletionStage<BookMessages.ActionPerformed> bookAdded = PatternsCS.ask(bookActor, new BookMessages.AddBookMessage(book), timeout)
                    .thenApply(obj -> (BookMessages.ActionPerformed) obj);

            return onSuccess(() -> bookAdded, performed -> {
                if (performed.equals( "Book is added" )){
                    return complete(StatusCodes.CREATED, performed, Jackson.marshaller());
                } else {
                    return complete(StatusCodes.BAD_REQUEST, performed, Jackson.marshaller());
                }

            });
        })));
    }

    private Route deleteBook(Long id) {
        return delete(() -> {
            CompletionStage<BookMessages.ActionPerformed> book = PatternsCS.ask(bookActor, new BookMessages.DeleteBookMessage(id), timeout)
                    .thenApply(obj -> (BookMessages.ActionPerformed) obj);

            return onSuccess(() -> book, performed -> {
                if (performed.equals( "Book is deleted" ))
                    return complete( StatusCodes.OK, performed, Jackson.marshaller());
                else
                    return complete(StatusCodes.BAD_REQUEST, performed, Jackson.marshaller());
            });
        });
    }

    private Route putBook(){
        return route(put(() -> entity( Jackson.unmarshaller(Book.class), book -> {
            CompletionStage<BookMessages.ActionPerformed> userCreated = PatternsCS.ask(bookActor, new BookMessages.UpdateBookMessage(book), timeout)
                    .thenApply(obj -> (BookMessages.ActionPerformed) obj);

            return onSuccess(() -> userCreated, performed -> {
                return complete(StatusCodes.RESET_CONTENT, performed, Jackson.marshaller());
            });
        })));
    }

    private Route viewStudents() {
        return get(() -> {
            CompletionStage<List<Student>> students = PatternsCS.ask(studentActor, new StudentMessages.GetStudentsMessage(), timeout)
                    .thenApply(obj -> (List<Student>) obj);

            return onSuccess(() -> students, performed -> {
                if (!performed.isEmpty())
                    return complete( StatusCodes.OK, performed, Jackson.marshaller());
                else
                    return complete(StatusCodes.NOT_FOUND);
            });
        });
    }

    private Route issueBook() {
        return route(post(() ->
            entity( Jackson.unmarshaller(Student.class), studentToIssueBook -> {
            CompletionStage<BookMessages.ActionPerformed> bookIssued = PatternsCS.ask(bookActor, new BookMessages.IssueBookMessage(studentToIssueBook), timeout)
                    .thenApply(obj -> (BookMessages.ActionPerformed) obj);

            return onSuccess(() -> bookIssued, performed -> {
                return complete(StatusCodes.CREATED, performed, Jackson.marshaller());
            });
        })));
    }

    private Route returnBook() {
        return route(put(() ->
            entity( Jackson.unmarshaller(Student.class), studentToReturnBook -> {
                CompletionStage<BookMessages.ActionPerformed> bookReturn = PatternsCS.ask(bookActor, new BookMessages.ReturnBookMessage(studentToReturnBook), timeout)
                        .thenApply(obj -> (BookMessages.ActionPerformed) obj);

                return onSuccess(() -> bookReturn, performed -> {
                    return complete(StatusCodes.CREATED, performed, Jackson.marshaller());
                });
            })));
    }

    private Route loginUser() {
        return route(post(() ->
            entity( Jackson.unmarshaller(User.class), user -> {
                CompletionStage<UserMessages.ActionPerformed> userCreated = PatternsCS.ask(userActor, new UserMessages.LoginUserMessage(user), timeout)
                        .thenApply(obj -> (UserMessages.ActionPerformed) obj);

                return onSuccess(() -> userCreated, performed -> {
                    return complete(StatusCodes.CREATED, performed, Jackson.marshaller());
                });
            })));
    }

}
