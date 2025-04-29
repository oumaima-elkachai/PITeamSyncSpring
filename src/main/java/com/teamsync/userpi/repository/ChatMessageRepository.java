package com.teamsync.userpi.repository;

import com.teamsync.userpi.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByUserIdOrderByTimestampAsc(String userId);

    List<String> findDistinctConversationIdByUserId(String id);

    List<ChatMessage> findByConversationIdOrderByTimestampAsc(String conversationId);
    @Query(value = "{ 'userId': ?0 }", fields = "{ 'conversationId': 1 }")
    List<ChatMessage> findAllConversationIdsByUserId(String userId);
}
