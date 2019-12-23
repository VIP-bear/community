package site.bearblog.community.cache;

import org.apache.commons.lang3.StringUtils;
import site.bearblog.community.dto.TagDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagCache {

    public static List<TagDTO> get(){
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("javascript","php","css","html","html5","java","node.js","python","c",
                "c++","shell","golang","object-c","swift","c#","sass","ruby","bash","asp.net",
                "scala","rust","perl","erlang"));
        tagDTOS.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台架构");
        framework.setTags(Arrays.asList("spring","express","flask","Android",
                "yii","struts","laravel"));
        tagDTOS.add(framework);

        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("Linux","nginx","apache","docker","ubuntu",
                "centos","unix","hadoop","windows-server"));
        tagDTOS.add(server);

        TagDTO db = new TagDTO();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("mysql","sqlserver","redis","mongodb","nosql",
                "sqlite","memcached"));
        tagDTOS.add(db);

        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("idea","eclipse","android studio","vs code","git",
                "github","sublime-text","svn","maven","glide","vim","xcode"));
        tagDTOS.add(tool);

        return tagDTOS;
    }

    public static String filterInvalid(String tags){
        String[] split = StringUtils.split(tags, ",");
        List<TagDTO> tagDTOS = get();
        List<String> tagList = tagDTOS.stream().flatMap(tag-> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }

}
