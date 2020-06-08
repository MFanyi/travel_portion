package cn.gdou.service.impl;

import cn.gdou.mapper.UserMapper;
import cn.gdou.domain.User;
import cn.gdou.service.UserService;
import cn.gdou.util.UuidUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

@Service("userService")
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JavaMailSender mailSender;

    /*
    * 用户注册
    * */
    @Override
    public boolean register(User user) throws JsonProcessingException, MessagingException, UnsupportedEncodingException {


        //根据用户名查询对象
        User findUser = userMapper.findByUserName(user.getUsername());
        if (findUser!=null){
            //用户名存在,注册失败
            return false;
        }


        String code=UuidUtil.getUuid();
        //设置激活码
        user.setCode(code);
        //设置激活状态码
        user.setStatus("N");

        String userStr = objectMapper.writeValueAsString(user);
        //将未激活的用户信息存储到redis中，60分钟内有效
        this.redisTemplate.opsForValue().set("usercode:"+code,userStr,60,TimeUnit.MINUTES);

//        long starTime=System.currentTimeMillis();
        //3、激活邮件发送
        String context="<a href='http://127.0.0.1:8080/active_success.html?"+code+"'>点击激活【Xu旅游网】</a>";
//        MimeMessage mimeMessage = mailSender.createMimeMessage();
//        mimeMessage.setSubject("徐先生旅游网激活邮件（1小时内有效）");
//        mimeMessage.setFrom("qq741082214@163.com");
//        mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress(user.getEmail()));
//        mimeMessage.setContent(context,"text/html;charset=UTF-8");
        MimeMessage msg=mailSender.createMimeMessage();
        MimeMessageHelper messageHelper=new MimeMessageHelper(msg,true,"utf-8");
        messageHelper.setTo(user.getEmail());
        messageHelper.setFrom("qq741082214@163.com","徐先生");
        messageHelper.setSubject("徐先生旅游网激活邮件（1小时内有效）");
        // 设置邮件内容，设为 true，表示启用html格式
        messageHelper.setText(context,true);

        mailSender.send(msg);

//        long endTime=System.currentTimeMillis();
//        System.out.println(endTime-starTime);//总共花费的时间需要 5s，时间太长了，应该跟邮箱的配置有关系吧


        return true;
    }


    /*
    * 用户激活
    * */
    @Override
    public int activeUser(String code) throws JsonProcessingException {
        //1、根据激活码查询用户
        String userStr=this.redisTemplate.opsForValue().get("usercode:"+code);
        if (StringUtils.isNotBlank(userStr)){
            User user=objectMapper.readValue(userStr,User.class);
            //激活之后从redis删除用户信息并存储到关系数据库中
            user.setStatus("Y");
            userMapper.saveUser(user);
            this.redisTemplate.delete("usercode:"+code);
            return 1;
        }else{
            return 0;
        }

    }

    /*
    * 登录方法
    * */
    @Override
    public User login(User user) {
        return userMapper.findByUserNameAndPassword(user.getUsername(),user.getPassword());
    }
}
