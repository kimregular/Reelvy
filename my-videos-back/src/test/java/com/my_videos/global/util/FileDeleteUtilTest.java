package com.my_videos.global.util;

import com.my_videos.global.exception.FileDeleteFailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileDeleteUtilTest {

	private FileDeleteUtil fileDeleteUtil;

	@BeforeEach
	void beforeEach() {
		fileDeleteUtil = new FileDeleteUtil();
	}

	@Test
	@DisplayName("저장된 파일을 삭제한다.")
	void test1() throws IOException {
		// given
		File temp = Files.createTempFile("test", ".txt").toFile();
		assertTrue(temp.exists());
		// when
		fileDeleteUtil.delete(temp.getAbsolutePath());

		// then
		assertThat(temp).doesNotExist();
	}

	@Test
	@DisplayName("없는 파일을 삭제하면 예외발생")
	void test2() {
		// given
		String invalidFilePath = "invalidFilePath";
		// when
		// then
		assertThatThrownBy(() -> fileDeleteUtil.delete(invalidFilePath))
				.isInstanceOf(FileDeleteFailException.class);
	}
}