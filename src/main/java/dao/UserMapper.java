package dao;

import po.User;

import java.util.List;

public interface UserMapper {
    List<User> findAllUsers();
    User findUserById(String id);
    User findUserByName(String name);
    void deleteUserById(String id);
    void updateUserById(User user);
    void addUser(User u);
}
