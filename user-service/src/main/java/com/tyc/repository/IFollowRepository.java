package com.tyc.repository;

import com.tyc.repository.entity.Follow;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IFollowRepository extends MongoRepository<Follow, String> {


    Optional<Follow> findOptionalByFollowIdAndUserId(String followId, String userId);

    Optional<List<Follow>> findOptionalByUserId(String userId);

    Optional<List<Follow>> findOptionalByUserIdIn(List<String> userIdList);

}
