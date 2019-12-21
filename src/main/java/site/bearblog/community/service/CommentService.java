package site.bearblog.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bearblog.community.dto.CommentDTO;
import site.bearblog.community.enums.CommentTypeEnum;
import site.bearblog.community.exception.CustomizeErrorCode;
import site.bearblog.community.exception.CustomizeException;
import site.bearblog.community.mapper.CommentMapper;
import site.bearblog.community.mapper.QuestionMapper;
import site.bearblog.community.mapper.UserMapper;
import site.bearblog.community.model.Comment;
import site.bearblog.community.model.Question;
import site.bearblog.community.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){
            // 回复评论
            Comment dbComment = commentMapper.selectById(comment.getParentId());
            if (dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }else {
                commentMapper.insert(comment);
                commentMapper.incCommentCount(comment.getParentId());
            }
        }else {
            // 回复问题
            Question question = questionMapper.getById(comment.getParentId());
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }else {
                commentMapper.insert(comment);
                questionMapper.incCommentCount(question.getId());
            }
        }
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        List<Comment> comments = commentMapper.selectByParentIdAndType(id, type.getType());
        if (comments.size() == 0){
            return new ArrayList<>();
        }

        // 获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        List<User> users = new ArrayList<>();

        // 获取评论人并转换为Map
        for (Long userId : userIds){
            User user = userMapper.finById(userId);
            users.add(user);
        }
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user->user.getId(), user->user));

        // 转换comment为commentDTO
        List<CommentDTO>  commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
