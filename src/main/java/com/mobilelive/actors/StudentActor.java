package com.mobilelive.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.FI;
import com.mobilelive.messages.StudentMessages;
import com.mobilelive.service.StudentService;

public class StudentActor extends AbstractActor{

    private StudentService studentService = new StudentService();

    public static Props props() {
        return Props.create(StudentActor.class);
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
            .match(StudentMessages.GetStudentsMessage.class, handleGetStudents())
            .build();
    }

    private FI.UnitApply<StudentMessages.GetStudentsMessage> handleGetStudents() {
        return getUserMessage -> {
            sender().tell(studentService.getStudents(), getSelf());
        };
    }

}
