package com.example.productmanager.service;

import com.example.productmanager.dao.MessageRepository;
import com.example.productmanager.entity.Message;
import com.example.productmanager.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> findMessagesBetweenUsers(Long userId1, Long userId2) {
        return messageRepository.findMessagesByUser1UserIdAndUser2UserIdOrUser1UserIdAndUser2UserIdOrderByTimeChatAsc(userId1, userId2, userId2, userId1);
    }


    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    public List<Message> findMessagesByUser1(User loggedInUser) {
        return messageRepository.findUser2ByUser1OrderByTimeChatDesc(loggedInUser);
    }

}
