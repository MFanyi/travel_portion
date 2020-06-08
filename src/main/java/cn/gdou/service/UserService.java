package cn.gdou.service;

import cn.gdou.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface UserService {

    boolean register(User user) throws JsonProcessingException, MessagingException, UnsupportedEncodingException;

    int activeUser(String code) throws JsonProcessingException;

    User login(User user);
}
