package com.mapfiltermagic.springredditclone.dtos;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String authenticationToken;
    private String refreshToken;
    private String username;
    private Instant expiresAt;

}
