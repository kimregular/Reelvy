package com.reelvy.global.resolver;

import com.reelvy.domain.comment.entity.Comment;
import com.reelvy.domain.comment.exception.NoCommentFoundException;
import com.reelvy.domain.comment.repository.CommentRepository;
import com.reelvy.global.annotation.TargetComment;
import com.reelvy.global.util.PathVariableExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class TargetCommentArgumentResolver implements HandlerMethodArgumentResolver {

    private final CommentRepository commentRepository;
    private final PathVariableExtractor pathVariableExtractor;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TargetComment.class) && parameter.getParameterType().equals(Comment.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Long commentId = pathVariableExtractor.extractAsLong(webRequest, "commentId");
        return commentRepository.findById(commentId).orElseThrow(NoCommentFoundException::new);
    }
}
