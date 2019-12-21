package site.bearblog.community.mapper;

import org.apache.ibatis.annotations.*;
import site.bearblog.community.model.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("insert into comment (parent_id, type, commentator, gmt_create, gmt_modified, like_count, content) values" +
            "(#{parentId}, #{type}, #{commentator}, #{gmtCreate}, #{gmtModified}, #{likeCount}, #{content} )")
    void insert(Comment comment);

    @Select("select * from comment where id = #{parentId}")
    Comment selectById(@Param("parentId")Long parentId);

    @Select("select * from comment where parent_id = #{id} and type = #{type} order by gmt_create desc")
    List<Comment> selectByParentIdAndType(@Param("id")Long id, @Param("type")Integer type);

    @Update("update comment set comment_count = comment_count+1 where id = #{id}")
    void incCommentCount(@Param("id") Long id);
}
