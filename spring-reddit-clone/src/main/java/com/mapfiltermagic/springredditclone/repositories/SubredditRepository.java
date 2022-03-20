package com.mapfiltermagic.springredditclone.repositories;

import java.util.Optional;

import com.mapfiltermagic.springredditclone.models.Subreddit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
    
    Optional<Subreddit> findByName(String subredditName);

}
