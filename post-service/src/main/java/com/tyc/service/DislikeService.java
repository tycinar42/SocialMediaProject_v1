package com.tyc.service;

import com.tyc.repository.IDislikeRepository;
import com.tyc.repository.entity.Dislike;
import com.tyc.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class DislikeService extends ServiceManager<Dislike, String> {


    private final IDislikeRepository dislikeRepository;


    public DislikeService(IDislikeRepository dislikeRepository) {
        super(dislikeRepository);
        this.dislikeRepository = dislikeRepository;
    }
}
