package com.maisprati.forum.dto.request;

import lombok.Data;

@Data
public class ConnectionDto {
    private Long followerId;
    private Long followedId;
}
