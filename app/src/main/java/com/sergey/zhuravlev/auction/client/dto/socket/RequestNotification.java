package com.sergey.zhuravlev.auction.client.dto.socket;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class RequestNotification {

    private Date timestamp;
    private Long lastReadMessage;

}
