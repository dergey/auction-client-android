package com.sergey.zhuravlev.auction.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageDto<T> {

    @JsonProperty(value = "size")
    private Integer size;

    @JsonProperty(value = "number")
    private Integer number;

    @JsonProperty(value = "total_pages")
    private Integer totalPages;

    @JsonProperty(value = "number_of_elements")
    private Integer numberOfElements;

    @JsonProperty(value = "first")
    private Boolean first;

    @JsonProperty(value = "last")
    private Boolean last;

    @JsonProperty(value = "content")
    private List<T> content;

}
