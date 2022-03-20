package com.mapfiltermagic.springredditclone.repositories;

import java.util.List;

import com.mapfiltermagic.springredditclone.models.Comment;
import com.mapfiltermagic.springredditclone.models.Post;
import com.mapfiltermagic.springredditclone.models.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);

}
