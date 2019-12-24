package site.bearblog.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.bearblog.community.dto.NotificationDTO;
import site.bearblog.community.dto.PaginationDTO;
import site.bearblog.community.enums.NotificationStatusEnum;
import site.bearblog.community.enums.NotificationTypeEnum;
import site.bearblog.community.exception.CustomizeErrorCode;
import site.bearblog.community.exception.CustomizeException;
import site.bearblog.community.mapper.NotificationMapper;
import site.bearblog.community.model.Notification;
import site.bearblog.community.model.User;

import java.util.ArrayList;
import java.util.List;
@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    public PaginationDTO list(Long userId, Integer page, Integer size){
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalPage;
        Integer totalCount = notificationMapper.countByUserId(userId);
        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }
        // 容错处理
        if (page < 1){
            page = 1;
        }
        if (page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage, page);

        Integer offset = size * (page - 1);
        if (offset < 0) offset = 0;
        List<Notification> notifications = notificationMapper.listByUserId(userId, offset, size);

        if (notifications.size() == 0){
            return paginationDTO;
        }
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications){
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }

    public Integer unreadCount(Long id) {
        Integer unreadCount = notificationMapper.countUnread(id, NotificationStatusEnum.UNREAD.getStatus());
        return unreadCount;
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectById(id);
        if (notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (notification.getReceiver() != user.getId()){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        // 更新为已读
        notificationMapper.updateRead(NotificationStatusEnum.READ.getStatus(), id);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
