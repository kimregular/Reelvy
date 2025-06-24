package com.reelvy.domain.video.handler;

import com.reelvy.domain.user.entity.User;
import com.reelvy.domain.video.dto.request.VideoUploadRequest;
import com.reelvy.domain.video.entity.Video;
import com.reelvy.domain.video.entity.VideoStatus;
import com.reelvy.domain.video.resolver.VideoSaveResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VideoBuildHandlerTest {

	VideoSaveResolver videoSaveResolver;
	VideoBuildHandler videoBuildHandler;

	@BeforeEach
	void setUp() {
		this.videoSaveResolver = mock(VideoSaveResolver.class);
		this.videoBuildHandler = new VideoBuildHandler(videoSaveResolver);
	}

	@Test
	@DisplayName("업로드 요청과 유저 정보로 video 엔티티를 생성한다.")
	void test1() {
		// given
		MockMultipartFile file = new MockMultipartFile("video", "video.mp4", "video/mp4", new byte[1024]);
		VideoUploadRequest request = new VideoUploadRequest("title", file, "desc");
		User user = mock(User.class);
		when(user.getUsername()).thenReturn("tester");
		when(videoSaveResolver.saveVideoFileInLocal(file, user.getUsername())).thenReturn("save/path/video.mp4");
		// when
		Video video = videoBuildHandler.buildVideoWith(request, user);
		// then
		assertThat(video.getVideoTitle()).isEqualTo("title");
		assertThat(video.getVideoDesc()).isEqualTo("desc");
		assertThat(video.getVideoPath()).isEqualTo("save/path/video.mp4");
		assertThat(video.getVideoStatus()).isEqualTo(VideoStatus.AVAILABLE);
		assertThat(video.getUser()).isEqualTo(user);
		assertThat(video.getVideoView()).isZero();
	}
}