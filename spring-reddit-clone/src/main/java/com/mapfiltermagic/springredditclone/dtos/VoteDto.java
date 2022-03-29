package com.mapfiltermagic.springredditclone.dto;

import com.mapfiltermagic.springredditclone.models.VoteType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
    
    private VoteType voteType;
    private Long voteId;

}
