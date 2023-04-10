package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
//import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.User;

import org.junit.jupiter.api.Test;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;


@SpringBootTest
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

//    @Autowired
//    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(101);
        System.out.println("user = " + user);
    }

    @Test
    public void testSelectPost() {

        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(0, 0, 10);
        for (DiscussPost post : list) {
            System.out.println(post);
        }

        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println("rows = " + rows);

    }

    @Test
    public void testInsertPost() {
        DiscussPost p1 = new DiscussPost();
        p1.setTitle("title");
        p1.setContent("content");
        int i = discussPostMapper.insertDiscussPost(p1);
        System.out.println("i = " + i);
    }

//    @Test
//    public void testInsertLoginTest() {
//        LoginTicket loginTicket = new LoginTicket();
//        loginTicket.setUserId(101);
//        loginTicket.setTicket("abc");
//        loginTicket.setStatus(0);
//        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
//        loginTicketMapper.insertLoginTicket(loginTicket);
//    }
//
//    @Test
//    public void testSelectLoginTicket() {
//        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
//        System.out.println(loginTicket);
//
//        loginTicketMapper.updateStatus("abc", 1);
//        loginTicket = loginTicketMapper.selectByTicket("abc");
//        System.out.println(loginTicket);
//    }

    @Test
    public void testInsertUser() {

        User user = new User();
        user.setUsername("ZHU");
        user.setPassword("123");
        user.setStatus(1);
        userMapper.insertUser(user);
        System.out.println("user = " + user);
    }

    @Test
    public void testSelectLetters() {
        List<Message> list = messageMapper.selectConversations(111, 0, 20);
        for (Message message : list) {
            System.out.println("message = " + message);
        }
        int count = messageMapper.selectConversationCount(111);
        System.out.println("count = " + count);
        List<Message> list1 = messageMapper.selectLetters("111_112", 0, 10);
        for (Message message : list1) {
            System.out.println("message = " + message);
        }
        int i1 = messageMapper.selectLetterCount("111_112");
        System.out.println("i1 = " + i1);
        int i = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println("i = " + i);
    }
}
