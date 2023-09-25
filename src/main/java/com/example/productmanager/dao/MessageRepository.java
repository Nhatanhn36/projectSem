package com.example.productmanager.dao;

import com.example.productmanager.entity.Message;
import com.example.productmanager.entity.Notification;
import com.example.productmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findMessagesByUser1UserIdAndUser2UserIdOrUser1UserIdAndUser2UserIdOrderByTimeChatAsc(
            @Param("userId1") Long userId1,
            @Param("userId2") Long userId2,
            @Param("userId3") Long userId3,
            @Param("userId4") Long userId4);
    List<Message> findUser2ByUser1OrderByTimeChatDesc(User loggedInUser);

//    @Query("SELECT m FROM Message m WHERE (m.user1 = :user OR m.user2 = :user) " +
//            "AND m.timeChat = (SELECT MAX(mm.timeChat) " +
//            "FROM Message mm WHERE (mm.user1 = :user OR mm.user2 = :user))")
//    List<Message> findLatestMessagesByUser(@Param("user") User user);


}


