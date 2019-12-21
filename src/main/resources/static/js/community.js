/**
* �ύ�ظ�
*/
function post(){
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2Target(questionId, 1, content);
}
function comment(e){
    var commentId = e.getAttribute("data-id")
    var content = $("#input-"+commentId).val();
    comment2Target(commentId, 2, content);
}

function comment2Target(targetId, type, content){
    if (!content){
        alert("���ܻظ�������...");
        return;
    }
    $.ajax({
      type: "POST",
      url: "/comment",
      contentType: "application/json",
      data: JSON.stringify({
        "parentId": targetId,
        "content": content,
        "type": type
      }),
      success: function (response){
        if (response.code == 200){
            window.location.reload();
        }else{
            if (response.code == 2003){
                var isAccepted = confirm(response.message);
                if (isAccepted){
                    window.open("https://github.com/login/oauth/authorize?client_id=c687cdca563b4b39c9b9&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                    window.localStorage.setItem("closable", true);
                }
            }else{
                alert(response.message);
            }
        }
      },
      dataType: "json"
    });
}

/**
*չ����������
*/
function collapseComments(e){
    var id = e.getAttribute("data-id");
    var comments = $("#comment-"+id);
    // ��ȡ��������չ��״̬
    var collapse = e.getAttribute("data-collapse");
    if (collapse){
        // �۵���������
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    }else{
        $.getJSON( "/comment/"+id, function( data ) {
           var subCommentContainer = comments;
           if (subCommentContainer.children().length == 1){
               $.each( data.data.reverse(), function(index, comment) {
                    var mediaLeftElement = $("<div/>",{
                        "class": "media-left"
                    }).append($("<img/>",{
                          "class": "media-object media-middle img-rounded",
                          "src": comment.user.avatarUrl
                    }));
                    var mediaBodyElement = $("<div/>",{
                       "class": "media-body"
                    }).append($("<h5/>",{
                         "class": "media-heading",
                         "html": comment.user.name
                    })).append($("<div/>",{
                        "html": comment.content
                    })).append($("<div/>",{
                        "class": "menu",
                    }).append($("<span/>",{
                       "class": "pull-right",
                       "html": moment(comment.gmtCreate).format('YYYY/MM/DD')
                    })));
                    var mediaElement = $("<div/>",{
                        "class": "media"
                    }).append(mediaLeftElement)
                        .append(mediaBodyElement);
                    var commentElement = $("<div/>",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);
                    subCommentContainer.prepend(commentElement);
                });
           }

           // չ����������
           comments.addClass("in");
           // ��Ƕ�������չ��״̬
           e.setAttribute("data-collapse", "in");
           e.classList.add("active");
        });
    }
}