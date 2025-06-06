package com.my_videos.global.util;

import org.springframework.stereotype.Component;

@Component
public class SanitizeUtil {

	public String sanitizeFileName(String fileName) {
		String cleaned = fileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
		if(cleaned.equals(".") || cleaned.equals("..")) throw new IllegalArgumentException("Invalid file name");
		return cleaned;
	}
}
