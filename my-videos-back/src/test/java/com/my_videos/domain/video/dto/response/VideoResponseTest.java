package com.my_videos.domain.video.dto.response;

import com.my_videos.domain.user.dto.response.UserResponse;
import com.my_videos.domain.user.entity.User;
import com.my_videos.domain.video.entity.Video;
import com.my_videos.domain.video.entity.VideoStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VideoResponseTest {

	@Test
	@DisplayName("비디오와 dto가 매핑된다.")
	void test1() {
		// given
		User user = mock(User.class);
		when(user.getUsername()).thenReturn("tester");

		Video video = mock(Video.class);
		when((video.getId())).thenReturn(1L);
		when(video.getVideoTitle()).thenReturn("videoTitle");
		when(video.getUser()).thenReturn(user);
		when(video.getVideoDesc()).thenReturn("description for test video");
		when(video.getVideoView()).thenReturn(1000L);
		when(video.getVideoStatus()).thenReturn(VideoStatus.AVAILABLE);

		// when
		VideoResponse response = VideoResponse.of(video, UserResponse.builder().username(user.getUsername()).build());

		// then
		assertThat(response.getId()).isEqualTo(1L);
		assertThat(response.getTitle()).isEqualTo("videoTitle");
		assertThat(response.getUser().getUsername()).isEqualTo("tester");
		assertThat(response.getDesc()).isEqualTo("description for test video");
		assertThat(response.getVideoView()).isEqualTo(1000L);
		assertThat(response.getVideoStatus()).isEqualTo(VideoStatus.AVAILABLE);
	}
}