package com.tyc.service;

import com.tyc.repository.IOnlineRepository;
import com.tyc.repository.entity.Online;
import com.tyc.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class OnlineService extends ServiceManager<Online, String> {


    private final IOnlineRepository onlineRepository;

    public OnlineService(IOnlineRepository onlineRepository) {
        super(onlineRepository);
        this.onlineRepository = onlineRepository;
    }
}
