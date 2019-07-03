package com.alexlee.mybatis.mapper;

import com.alexlee.mybatis.entity.Blog;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface BlogMapper {
    /**
     * 文章列表翻页查询
     * @param rowBounds
     * @return
     */
    public List<Blog> selectBlogList(RowBounds rowBounds);


}
