package com.tyc.repository;

import com.tyc.repository.entity.Follower;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFollowerRepository extends MongoRepository<Follower, String> {


}
