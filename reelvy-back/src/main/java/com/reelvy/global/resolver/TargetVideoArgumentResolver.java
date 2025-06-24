package com.reelvy.global.resolver;

import com.reelvy.domain.video.entity.Video;
import com.reelvy.domain.video.exception.NoVideoFoundException;
import com.reelvy.domain.video.repository.VideoRepository;
import com.reelvy.global.annotation.TargetVideo;
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
