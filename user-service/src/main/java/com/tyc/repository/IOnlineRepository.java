package com.tyc.repository;

import com.tyc.repository.entity.Online;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOnlineRepository extends MongoRepository<Online, String> {


}
