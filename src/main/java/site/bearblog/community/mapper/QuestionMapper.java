package site.bearblog.community.mapper;

import org.apache.ibatis.annotations.*;
import site.bearblog.community.model.Question;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title, description, gmt_create, gmt_modified, creator, tag) values" +
            "(#{title}, #{description}, #{gmtCreate}, #{gmtModified}, #{creator}, #{tag} )")
    void create(Question question);

    @Select("select * from question order by gmt_create desc limit #{offset},#{size}")
    List<Question> list(@Param("offset") Integer offset, @Param("size")Integer size);

    @Select("select * from question where title REGEXP #{search} order by gmt_create desc limit #{offset},#{size}")
    List<Question> listBySearch(@Param("search") String search, @Param("offset") Integer offset, @Param("size")Integer size);

    @Select("select count(*) from question")
    Integer count();

    @Select("select count(*) from question where title REGEXP #{search}")
    Integer countBySearch(@Param("search")String search);

    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param("userId")Long userId, @Param(value = "offset")Integer offset, @Param(value = "size")Integer size);

    @Select("select count(1) from question where creator = #{userId}")
    Integer countByUserId(@Param("userId")Long userId);

    @Select("select * from question where id = #{id}")
    Question getById(@Param("id")Long id);

    @Select("select * from question where id != #{id} and tag REGEXP #{tag}")
    List<Question> selectRelated(@Param("id")Long id, @Param("tag")String tag);

    @Update("update question set title= #{title}, description = #{description}, gmt_modified = #{gmtModified}, tag = #{tag} where id = #{id}")
    int update(Question question);

    @Update("update question set view_count = view_count+1 where id = #{id}")
    void incViewCount(@Param("id")Long id);

    @Update("update question set comment_count = comment_count+1 where id = #{id}")
    void incCommentCount(@Param("id")Long id);
}
