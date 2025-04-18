package com.example.xiaohui.mapper;

import com.example.xiaohui.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;


@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user")
    List<User> findAll();

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);
}
