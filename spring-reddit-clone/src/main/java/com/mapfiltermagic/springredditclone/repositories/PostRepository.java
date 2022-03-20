package com.mapfiltermagic.springredditclone.repositories;

import java.util.List;

import com.mapfiltermagic.springredditclone.models.Post;
import com.mapfiltermagic.springredditclone.models.Subreddit;
import com.mapfiltermagic.springredditclone.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUser(User user);

    List<Post> findAllBySubreddit(Subreddit subreddit);

}
