package com.mysettlement.domain.video.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record VideoUploadRequestDto(@NotBlank(message = "제목은 필수값입니다.") String title,
                                    @NotNull(message = "비디오 파일은 필수값입니다.") MultipartFile videoFile,
                                    @Nullable String desc) {
    public VideoUploadRequestDto {
        if (desc == null) {
            desc = "";
        }
    }
}
