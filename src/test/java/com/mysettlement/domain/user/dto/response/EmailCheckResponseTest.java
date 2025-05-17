package com.mysettlement.domain.user.dto.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailCheckResponseTest {

    @Test
    void whenConstructedWithTrue_thenIsDuplicateEmailReturnsTrue() {
        EmailCheckResponse response = new EmailCheckResponse(true);
        assertThat(response.isDuplicateEmail()).isTrue();
    }

    @Test
    void whenConstructedWithFalse_thenIsDuplicateEmailReturnsFalse() {
        EmailCheckResponse response = new EmailCheckResponse(false);
        assertThat(response.isDuplicateEmail()).isFalse();
    }
}