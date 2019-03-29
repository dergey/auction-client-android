package com.sergey.zhuravlev.auction.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestLotDto {

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "images")
    private Collection<String> images;

    @JsonProperty(value = "expires_at")
    private Date expiresAt;

    @JsonProperty(value = "starting_amount")
    private BigDecimal startingAmount;

    @JsonProperty(value = "currency_code")
    private String currencyCode;

    @JsonProperty(value = "auction_step")
    private Long auctionStep;

    @JsonProperty(value = "category_id")
    private Long categoryId;

}
