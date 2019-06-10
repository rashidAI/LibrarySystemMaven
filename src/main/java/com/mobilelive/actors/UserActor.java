package com.mobilelive.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.FI;
import com.mobilelive.messages.UserMessages;
import com.mobilelive.service.UserService;

public class UserActor extends AbstractActor {

    private UserService userService = new UserService();

    public static Props props() {
        return Props.create(UserActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(UserMessages.CreateUserMessage.class, handleCreateUser())
                .match(UserMessages.GetUserMessage.class, handleGetUser())
                .match(UserMessages.UpdateUserMessage.class, handleUpdateUser())
                .match(UserMessages.DeleteUserMessage.class, handleDeleteUser())
                .match( UserMessages.GetUsersMessage.class, handleGetUsers())
                .match( UserMessages.LoginUserMessage.class, handleLoginUser())
                .build();
    }

    private FI.UnitApply<UserMessages.UpdateUserMessage> handleUpdateUser() {
        return createUserMessage -> {
            sender().tell(new UserMessages.ActionPerformed(
                    userService.updateLibrarian(createUserMessage.getUser())), getSelf());
        };
    }

    private FI.UnitApply<UserMessages.CreateUserMessage> handleCreateUser() {
        return createUserMessage -> {
            sender().tell(new UserMessages.ActionPerformed(
                    userService.createLibrarian(createUserMessage.getUser())), getSelf());
        };
    }

    private FI.UnitApply<UserMessages.GetUserMessage> handleGetUser() {
        return getUserMessage -> {
            sender().tell(userService.getUser(getUserMessage.getUserId()), getSelf());
        };
    }

    private FI.UnitApply<UserMessages.DeleteUserMessage> handleDeleteUser() {
        return deleteUserMessage -> {
            sender().tell(userService.deleteUser(deleteUserMessage.getUserId()), getSelf());
        };
    }

    private FI.UnitApply<UserMessages.GetUsersMessage> handleGetUsers() {
        return getUserMessage -> {
            sender().tell(userService.getUsers(), getSelf());
        };
    }

    private FI.UnitApply<UserMessages.LoginUserMessage> handleLoginUser() {
        return loginUserMessage -> {
            sender().tell(new UserMessages.ActionPerformed(
                            userService.LoginUser( loginUserMessage.getUser())), getSelf());
        };
    }
}
