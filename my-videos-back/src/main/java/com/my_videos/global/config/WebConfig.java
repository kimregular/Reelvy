package com.my_videos.global.config;

import com.my_videos.global.resolver.LoginUserArgumentResolver;
import com.my_videos.global.resolver.TargetVideoArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final LoginUserArgumentResolver loginUserArgumentResolver;
	private final TargetVideoArgumentResolver targetVideoArgumentResolver;

	@Value("${app.upload-dir}")
	private String uploadDir;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/app/uploads/**") // 응답의 "app/uploads/..."와 매핑
				.addResourceLocations("file:" + uploadDir + "/"); // 실제 저장 경로: app/uploads/
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(loginUserArgumentResolver);
		resolvers.add(targetVideoArgumentResolver);
	}
}
