package com.mysettlement.domain.user.entity;

public enum UserImageType {

	PROFILE("profile"){
		@Override
		public String getImagePathOf(User user) {
			return user.getProfileImagePath();
		}
	},
	BACKGROUND("background"){
		@Override
		public String getImagePathOf(User user) {
			return user.getBackgroundImagePath();
		}
	};

	private final String folderName;

	public abstract String getImagePathOf(User user);

	UserImageType(String folderName) {
		this.folderName = folderName;
	}

	public String getFolderName() {
		return folderName;
	}
}
