package cn.gdou.mapper;

import cn.gdou.domain.Category;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CategoryMapper {


    /*
     * 查询所有分类
     * */
    @Select("select * from tab_category")
    List<Category> findAll();

}
