package com.mysettlement.domain.user.entity;

public enum UserImageType {

	PROFILE("profile.jpg"){
		@Override
		public String getImagePathOf(User user) {
			return user.getProfileImagePath();
		}
	},
	BACKGROUND("background.jpg"){
		@Override
		public String getImagePathOf(User user) {
			return user.getBackgroundImagePath();
		}
	};

	private final String fileName;

	public abstract String getImagePathOf(User user);

	UserImageType(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
}
