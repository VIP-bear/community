package site.bearblog.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.bearblog.community.dto.PaginationDTO;
import site.bearblog.community.dto.QuestionDTO;
import site.bearblog.community.mapper.QuestionMapper;
import site.bearblog.community.mapper.UserMapper;
import site.bearblog.community.model.Question;
import site.bearblog.community.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size){
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount, page, size);
        // 容错处理
        if (page < 1){
            page = 1;
        }
        if (page > paginationDTO.getTotalPage()){
            page = paginationDTO.getTotalPage();
        }

        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions){
            User user = userMapper.finById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }
}
