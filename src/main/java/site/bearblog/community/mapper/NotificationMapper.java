package site.bearblog.community.mapper;

import org.apache.ibatis.annotations.*;
import site.bearblog.community.enums.NotificationStatusEnum;
import site.bearblog.community.model.Notification;

import java.util.List;

@Mapper
public interface NotificationMapper {

    @Insert("insert into notification (notifier, receiver, outer_id, type, gmt_create, status, notifier_name, outer_title) values" +
            "(#{notifier}, #{receiver}, #{outerId}, #{type}, #{gmtCreate}, #{status}, #{notifierName}, #{outerTitle} )")
    void insetNotification(Notification notification);

    @Select("select count(1) from notification where receiver = #{userId}")
    Integer countByUserId(@Param("userId")Long userId);

    @Select("select count(1) from notification where receiver = #{id} and status = #{status}")
    Integer countUnread(@Param("id")Long id, @Param("status")int status);

    @Select("select * from notification where receiver = #{userId} order by gmt_create desc limit #{offset},#{size}")
    List<Notification> listByUserId(@Param("userId")Long userId, @Param(value = "offset")Integer offset, @Param(value = "size")Integer size);

    @Select("select * from notification where id = #{id}")
    Notification selectById(@Param("id")Long id);

    @Update("update notification set status = #{status} where id = #{id}")
    void updateRead(@Param("status")Integer status, @Param("id")Long id);

}
