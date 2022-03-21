package com.mapfiltermagic.springredditclone.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    
    private Long id;
    private Long postId;
    private String text;
    private String userName;
    private Instant createdDate;

}