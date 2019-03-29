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
public class CategoryDto {

    @JsonProperty(value = "id")
    private Long id;
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "image")
    private String image;

}
