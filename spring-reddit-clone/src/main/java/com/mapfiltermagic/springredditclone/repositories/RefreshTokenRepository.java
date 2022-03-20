package com.mapfiltermagic.springredditclone.repositories;

import java.util.Optional;

import com.mapfiltermagic.springredditclone.models.RefreshToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteToken(String token);

}
