package com.mysettlement.domain.ad.entity;

import com.mysettlement.domain.ad.dto.request.AdStatusUpdateReqeustDto;
import com.mysettlement.domain.ad.dto.request.AdUpdateReqeustDto;
import com.mysettlement.domain.ad.dto.request.AdUploadRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "AD")
@NoArgsConstructor(access = PROTECTED)
public class Ad {

    @Id
    @Column(name = "ad_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "ad_title")
    private String adTitle;

    @Column(name = "ad_desc")
    private String adDesc;

    @Column(name = "ad_view")
    private long adView;

    @Column(name = "ad_status")
    @Enumerated(value = STRING)
    private AdStatus adStatus;

    @Column(name = "ad_price_per_view")
    private double adPricePerView;

    @Builder
    private Ad(String adTitle, String adDesc, long adView, AdStatus adStatus, double adPricePerView) {
        this.adTitle = adTitle;
        this.adDesc = adDesc;
        this.adView = adView;
        this.adStatus = adStatus;
        this.adPricePerView = adPricePerView;
    }

    public static Ad of(AdUploadRequestDto adUploadRequestDto) {
        return Ad.builder()
                .adTitle(adUploadRequestDto.title())
                .adDesc(adUploadRequestDto.desc())
                .adView(0)
                .adStatus(AdStatus.AVAILABLE)
                .adPricePerView(1.5)
                .build();
    }

    public void update(AdStatusUpdateReqeustDto adStatusUpdateReqeustDto) {
        this.adStatus = adStatusUpdateReqeustDto.adStatus();
    }

    public void update(AdUpdateReqeustDto adUpdateReqeustDto) {
        this.adTitle = adUpdateReqeustDto.title();
        this.adDesc = adUpdateReqeustDto.desc();
    }

    public void viewUpdate() {
        this.adView++;
    }
}
