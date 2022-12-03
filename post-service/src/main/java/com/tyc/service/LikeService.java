package com.tyc.service;

import com.tyc.repository.ILikeRepository;
import com.tyc.repository.entity.Like;
import com.tyc.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class LikeService extends ServiceManager<Like, String> {


    private final ILikeRepository likeRepository;


    public LikeService(ILikeRepository likeRepository) {
        super(likeRepository);
        this.likeRepository = likeRepository;
    }
}
