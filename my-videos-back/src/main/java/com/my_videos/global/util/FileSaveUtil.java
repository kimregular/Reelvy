package com.my_videos.global.util;

import com.my_videos.global.exception.FileSaveFailException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileSaveUtil {

	public String save(String targetPath, MultipartFile file) {
		Path filePath = Paths.get(targetPath);
		try {
			Files.createDirectories(filePath.getParent());
			Files.write(filePath, file.getBytes());
		} catch (IOException e) {
			throw new FileSaveFailException();
		}

		return filePath.toString();
	}
}
