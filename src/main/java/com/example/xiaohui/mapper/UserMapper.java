package com.example.xiaohui.mapper;

import com.example.xiaohui.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;


@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user")
    List<User> findAll();

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);

    //多个参数加@Select("SELECT * FROM user WHERE username = #{username} AND status = #{status}")
    //User findByInfo(@Param("username") String username, @Param("status") int status);
    //所以在这里可以不加@Param
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

}
