package com.mysettlement.global.util;

import org.springframework.stereotype.Component;

@Component
public class AdSelectUtil {

    private static final Long FIVE_MINUTES_IN_SECONDS = 300L;

    public long getNumOfAds(Long videoLength) {
        long numOfAds = videoLength / FIVE_MINUTES_IN_SECONDS;
        return numOfAds == 0 ? 1 : numOfAds;
    }
}
