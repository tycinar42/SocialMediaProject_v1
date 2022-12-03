package com.tyc.repository;

import com.tyc.repository.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPostRepository extends MongoRepository<Post, String> {


    List<Post> findAllByUserId(String id);

    List<Post> findAllByUsername(String username);


    Optional<Post> findByIdAndUserId(String id, String userId);

    List<Post> findByUserIdIn(List<String> userIdlist);
}
