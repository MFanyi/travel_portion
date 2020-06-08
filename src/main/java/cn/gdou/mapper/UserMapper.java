package cn.gdou.mapper;

import cn.gdou.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {


    //根据用户名查询用户信息
    @Select("select * from tab_user where username=#{username}")
    User findByUserName(@Param("username") String username);


    //保存用户信息
    @Insert("insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) " +
            "values(#{username},#{password},#{name},#{birthday},#{sex},#{telephone},#{email},#{status},#{code})")
    void saveUser(User user);

    @Select("select * from tab_user where code=#{code}")
    User findByCode(String code);

    @Update("update tab_user set status='Y' where uid=#{uid}")
    void updateStatus(int uid);

    @Select("select status from tab_user where uid=#{uid}")
    String findStatus(int uid);

    @Select("select * from tab_user where username=#{username} and password=#{password}")
    User findByUserNameAndPassword(@Param("username") String username, @Param("password") String password);

}
