package com.mobilelive.service;

import com.mobilelive.helper.Utils;
import com.mobilelive.model.Book;
import com.mobilelive.model.User;

import javax.rmi.CORBA.Util;
import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

public class UserService {

    private static List<User> users = new ArrayList<>();

    static {
        users = Utils.readUsersFile.get();
        if (users.size()  == 0  || !users.get(0).getUserType().equals( "Admin" )){
            throw new RuntimeException( "Admin user is required." );
        }
    }

    public Optional<User> getUser(Long id) {
        return users.stream()
            .filter(user -> user.getId()
                    .equals(id))
            .findFirst();
    }

    public String createLibrarian(User user) {
        Long currentUserId = 0L;
        User newUser = null;
        Optional<User> userFind = users.stream().filter(innerUser ->
                innerUser.getEmail().equals( user.getEmail() )
        ).findFirst();

        if(!userFind.isPresent()){
            if (users.size() > 0 ){
                try{
                    System.out.println( users );
                    User userInt =  (User) users.stream()
                            .max(Comparator.comparing(User::getId)).get();
                    currentUserId = userInt.getId() ;
                    newUser = new User(++currentUserId,
                            user.getName(),
                            user.getEmail(),
                            user.getPassword(),
                            user.getMobileNumber(),
                            "Librarian"
                    );
                } catch(Exception ex){
                    System.out.print( ex );
                    return "Some Exception accoured while adding User";
                }
            }
            users.add(newUser);
            Utils.updateUserFile(users);
            return "New User is added";
        } else {
            return "User 'email: "+user.getEmail()+"' is already assign.";
        }
    }

    public List<User> getUsers(){
        List<User> updatedUser = users.stream().filter( user ->
            !user.getUserType().equals("Admin")
        ).collect( Collectors.toList());
        return updatedUser;
    }

    public String updateLibrarian(User user){
        Optional<User> userFind = users.stream().filter( innerUser -> innerUser.getId()
                .equals(user.getId()))
                .findFirst();

        if(userFind.isPresent()){
            users = users.stream().map(userObj -> {
                if(userObj.getId() == user.getId()){
                    return user;
                }
                return userObj;
            }).collect( Collectors.toList());
            Utils.updateUserFile(users);
            return "User is updated";
        }  else {
            return "User is not found";
        }
    }

    public boolean deleteUser(Long id){
         users.removeIf(user -> user.getId().equals(id));
         Utils.updateUserFile(users);
         return true;
    }

    public String LoginUser(User user){
        Optional<User> user1 = users.stream().filter( userObj ->
                userObj.getEmail().equals( user.getEmail())
        ).findFirst();
        if (user1.isPresent()  && user1.get().getPassword().equals(  user.getPassword() )){
            return "User is Authenticated as " + user1.get().getUserType();
        }
        return "Email or Passwrod does not match";
    }

}
