package com.my_videos.global.resolver;

import com.my_videos.domain.comment.entity.Comment;
import com.my_videos.domain.comment.exception.NoCommentFoundException;
import com.my_videos.domain.comment.repository.CommentRepository;
import com.my_videos.domain.video.exception.NoVideoFoundException;
import com.my_videos.global.annotation.TargetComment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TargetCommentArgumentResolver implements HandlerMethodArgumentResolver {

    private final CommentRepository commentRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TargetComment.class) && parameter.getParameterType().equals(Comment.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Map<String, String> uriTemplates = (Map<String, String>) webRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        String commentIdStr = uriTemplates.get("commentId");
        if(commentIdStr == null){
            throw new NoVideoFoundException();
        }
        Long videoId = Long.valueOf(commentIdStr);
        return commentRepository.findById(videoId).orElseThrow(NoCommentFoundException::new);
    }
}
