package com.mysettlement.global.util;

import com.mysettlement.global.exception.FileSaveFailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class FileSaveUtilTest {

    private FileSaveUtil fileSaveUtil;

    @BeforeEach
    void setUp() {
        fileSaveUtil = new FileSaveUtil();
    }

    @Test
    void testSave_validFile_shouldSaveSuccessfully() throws IOException {
        // given
        File tempDir = Files.createTempDirectory("test-dir").toFile();
        String filePath = tempDir.getAbsolutePath() + "/test.txt";
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());

        // when
        String savedPath = fileSaveUtil.save(filePath, mockFile);

        // then
        assertTrue(Files.exists(new File(savedPath).toPath()));
        assertEquals("hello", Files.readString(new File(savedPath).toPath()));
    }

    @Test
    void testSave_whenIOException_shouldThrowFileSaveFailException() {
        // given
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());

        // when & then
        assertThrows(FileSaveFailException.class, () -> {
            fileSaveUtil.save("/root/invalid/path/test.txt", mockFile); // likely to fail due to permission
        });
    }
}