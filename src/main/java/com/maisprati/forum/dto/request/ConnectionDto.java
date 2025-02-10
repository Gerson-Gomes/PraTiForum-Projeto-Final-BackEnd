package com.maisprati.forum.dto.request;

import lombok.Data;

@Data
public class ConnectionDto {
    private Long followerId;  // ID do usuario seguidor
    private Long followedId;  // ID do usuario seguido
}
