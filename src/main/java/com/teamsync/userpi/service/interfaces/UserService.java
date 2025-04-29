package com.teamsync.userpi.service.interfaces;

import com.teamsync.userpi.entity.ChatMessage;
import com.teamsync.userpi.entity.User;
import com.teamsync.userpi.exception.UserCollectionException;

import java.util.List;

public interface UserService {
    public User createUser(User user) throws UserCollectionException ;
    public List<User> getAllUsers() ;
    public User getSingleUser(String id) throws UserCollectionException;
    public void updateUser(String id,User user) throws UserCollectionException;
    public void deleteUserById(String id) throws UserCollectionException;
    User loginUser(String email, String password) throws UserCollectionException;
    void blockUserById(String id) throws UserCollectionException;
    void unblockUserById(String id) throws UserCollectionException;
    void initiatePasswordReset(String email) throws UserCollectionException;

    void resetPassword(String token, String newPassword) throws UserCollectionException;
    void saveChatMessage(ChatMessage message);
    public List<ChatMessage> getChatHistory(String userId);
}
