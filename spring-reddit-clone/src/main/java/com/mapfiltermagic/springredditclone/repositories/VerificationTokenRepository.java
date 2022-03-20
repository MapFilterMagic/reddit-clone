package com.mapfiltermagic.springredditclone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.mapfiltermagic.springredditclone.models.VerificationToken;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    
    Optional<VerificationToken> findByToken(String token);

}
