package com.example.xiaohui.mapper;

import com.example.xiaohui.entity.NameUpdateDTO;
import com.example.xiaohui.entity.VipUserLoginDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface VipMapper {

    @Select("SELECT * FROM vip WHERE name = #{name}")
    VipUserLoginDTO findByVipname(@Param("name") String name);

    @Update("UPDATE vip SET name = #{newName} WHERE name = #{oldName}")
    int updateName(@Param("oldName") String oldName, @Param("newName") String newName);
}
