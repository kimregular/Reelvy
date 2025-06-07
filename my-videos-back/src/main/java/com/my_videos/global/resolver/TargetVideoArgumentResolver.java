package com.my_videos.global.resolver;

import com.my_videos.domain.video.entity.Video;
import com.my_videos.domain.video.exception.NoVideoFoundException;
import com.my_videos.domain.video.repository.VideoRepository;
import com.my_videos.global.annotation.TargetVideo;
import com.my_videos.global.util.PathVariableExtractor;
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
public class TargetVideoArgumentResolver implements HandlerMethodArgumentResolver {

    private final VideoRepository videoRepository;
    private final PathVariableExtractor pathVariableExtractor;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TargetVideo.class) && parameter.getParameterType().equals(Video.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Long videoId = pathVariableExtractor.extractAsLong(webRequest, "videoId");
        return videoRepository.findById(videoId).orElseThrow(NoVideoFoundException::new);
    }
}
