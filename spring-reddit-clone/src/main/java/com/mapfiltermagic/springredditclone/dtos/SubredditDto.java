package com.mapfiltermagic.springredditclone.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubredditDto {
    
    private Long id;
    private Integer numberOfPosts;
    private String name;
    private String description;
    
}
