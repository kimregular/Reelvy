package com.reelvy.global.resolver;

import com.reelvy.domain.user.entity.User;
import com.reelvy.domain.user.exception.NoUserFoundException;
import com.reelvy.domain.user.repository.UserRepository;
import com.reelvy.global.annotation.TargetUser;
import com.reelvy.global.util.PathVariableExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class TargetUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;
    private final PathVariableExtractor pathVariableExtractor;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(TargetUser.class) != null && parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Long userId = pathVariableExtractor.extractAsLong(webRequest, "userId");
        return userRepository.findById(userId).orElseThrow(NoUserFoundException::new);
    }
}
