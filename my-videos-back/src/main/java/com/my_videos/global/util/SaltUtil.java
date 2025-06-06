package com.my_videos.global.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SaltUtil {

	public String salt() {
		return UUID.randomUUID().toString();
	}
}
