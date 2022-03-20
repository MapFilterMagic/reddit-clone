package com.mapfiltermagic.springredditclone.repositories;

import java.util.Optional;

import com.mapfiltermagic.springredditclone.models.Post;
import com.mapfiltermagic.springredditclone.models.User;
import com.mapfiltermagic.springredditclone.models.Vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);

}
