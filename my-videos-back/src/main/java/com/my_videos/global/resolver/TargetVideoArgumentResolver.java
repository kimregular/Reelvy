package com.my_videos.global.resolver;

import com.my_videos.domain.video.entity.Video;
import com.my_videos.domain.video.exception.NoVideoFoundException;
import com.my_videos.domain.video.repository.VideoRepository;
import com.my_videos.global.annotation.TargetVideo;
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
public class TargetVideoArgumentResolver implements HandlerMethodArgumentResolver {

    private final VideoRepository videoRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TargetVideo.class) && parameter.getParameterType().equals(Video.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Map<String, String> uriTemplates = (Map<String, String>) webRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        String videoIdStr = uriTemplates.get("videoId");
        if(videoIdStr == null){
            throw new NoVideoFoundException();
        }
        Long videoId = Long.valueOf(videoIdStr);
        return videoRepository.findById(videoId).orElseThrow(NoVideoFoundException::new);
    }
}
