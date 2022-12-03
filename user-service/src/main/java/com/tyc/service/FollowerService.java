package com.tyc.service;

import com.tyc.repository.IFollowerRepository;
import com.tyc.repository.entity.Follower;
import com.tyc.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class FollowerService extends ServiceManager<Follower, String> {


    private final IFollowerRepository followerRepository;

    public FollowerService(IFollowerRepository followerRepository) {
        super(followerRepository);
        this.followerRepository = followerRepository;
    }
}
