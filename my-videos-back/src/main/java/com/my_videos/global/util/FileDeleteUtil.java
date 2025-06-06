package com.my_videos.global.util;

import com.my_videos.global.exception.FileDeleteFailException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileDeleteUtil {

	public void delete(String filePath) {
		Path path = Paths.get(filePath);
		try {
			Files.delete(path);
		} catch (IOException e) {
			throw new FileDeleteFailException();
		}
	}
}
