package com.sergey.zhuravlev.auction.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestBidDto {

    @JsonProperty(value = "lot_id")
    private Long lotId;

    @JsonProperty(value = "amount")
    private Long amount;

    @JsonProperty(value = "currency")
    private String currency;

}
