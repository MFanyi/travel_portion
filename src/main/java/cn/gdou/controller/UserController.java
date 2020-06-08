package cn.gdou.controller;

import cn.gdou.domain.ResultInfo;
import cn.gdou.domain.User;
import cn.gdou.service.UserService;
import cn.gdou.util.CheckUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@ResponseBody
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/registerUserServlet")
    public void registerUserServlet(User user, HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException {
        //校验验证码
        String check = request.getParameter("check");
        //从session中获取验证码
        HttpSession session=request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        //为了保证验证码只能使用一次
        session.removeAttribute("CHECKCODE_SERVER");
        ObjectMapper mapper=new ObjectMapper();
        ResultInfo info=new ResultInfo();
        //比较
        if(checkcode_server==null||!checkcode_server.equalsIgnoreCase(check)){
            //验证码错误
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将info对象序列化为json
            String jsonInfo=mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(jsonInfo);
            return;
        }

        //1、获取数据
        //2、封装对象
        //【Spring自动将数据进行封装】

        //3、调用service完成注册
        boolean flag=userService.register(user);
        //4、响应结果
        if (flag){
            //注册成功
            info.setFlag(true);
        }else {
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败,用户名已被注册！");
        }

        //将info对象序列化为json
        //ObjectMapper mapper=new ObjectMapper();
        String jsonInfo=mapper.writeValueAsString(info);
        //将json数据写回客户端
        //设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(jsonInfo);
    }

    /*
    * 验证码生成
    * */
    @GetMapping("checkCode")
    public void CheckCodeServlet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //服务器通知浏览器不要缓存
        response.setHeader("pragma","no-cache");
        response.setHeader("cache-control","no-cache");
        response.setHeader("expires","0");

        //在内存中创建一个长80，宽30的图片，默认黑色背景
        //参数一：长
        //参数二：宽
        //参数三：颜色
        int width = 80;
        int height = 30;
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        //获取画笔
        Graphics g = image.getGraphics();
        //设置画笔颜色为灰色
        g.setColor(Color.GRAY);
        //填充图片
        g.fillRect(0,0, width,height);

        //产生4个随机验证码，12Ey
        String checkCode = CheckUtils.getCheckCode();
        //将验证码放入HttpSession中
        request.getSession().setAttribute("CHECKCODE_SERVER",checkCode);
        //设置画笔颜色为黄色
        g.setColor(Color.YELLOW);
        //设置字体的小大
        g.setFont(new Font("黑体",Font.BOLD,24));
        //向图片上写入验证码
        g.drawString(checkCode,15,25);
        //将内存中的图片输出到浏览器
        //参数一：图片对象
        //参数二：图片的格式，如PNG,JPG,GIF
        //参数三：图片输出到哪里去
        ImageIO.write(image,"PNG",response.getOutputStream());
    }

    @GetMapping("activeUserServlet")
    public void ActiveUserServlet(@Param("code") String code, HttpServletResponse response) throws IOException {

        /*else if (flag==2){
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().write("您已经激活了，无需重复激活哦~&nbsp;<span style=\"color:pink;\">O(∩_∩)O</span>");
            }*/

        //1、获取激活码
        //String code=request.getParameter("code");
        if (StringUtils.isNotBlank(code)){
            //2、调用Service完成激活
            int flag=userService.activeUser(code);
            //3、判断
            if (flag==1){
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().write("恭喜，激活成功！接下来可以尽情访问啦~&nbsp;<span style=\"color:pink;\">O(∩_∩)O</span>");
            }else{
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().write("<span style=\"color:red\">激活过程出现了问题，或许您已经激活了呢~。</span>");
            }
        }
    }


    @PostMapping("loginServlet")
    public void LoginServlet(User user, HttpServletRequest request, HttpServletResponse response) throws IOException {

        //调用service查询
        User existUser=userService.login(user);

        ResultInfo info=new ResultInfo();

        //判断用户对象是否为null
        if (existUser==null){
            //用户名密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }else if ("N".equals(existUser.getStatus())){
            //用户未激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请进入邮箱激活");
        }else {
            //登录成功
            request.getSession().setAttribute("user",existUser);
            info.setFlag(true);
        }
        /*ObjectMapper mapper=new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),info);*/
        myWriteValue(response,info);
    }

    @GetMapping("findUserServlet")
    public void FindUserServlet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user=(User) request.getSession().getAttribute("user");
        if (user==null){
            user=new User();
            user.setUid(0);
        }

        ObjectMapper mapper=new ObjectMapper();
        response.setContentType("application/json;charset=utf-8 ");
        mapper.writeValue(response.getOutputStream(),user);
    }

    @GetMapping("quitServlet")
    public void QuitServlet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();

        response.sendRedirect(request.getContextPath()+"/login.html");
    }


    private void myWriteValue(HttpServletResponse response, Object object) throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        response.setContentType("application/json;charset=UTF-8");
        mapper.writeValue(response.getOutputStream(),object);
    }
}
