package com.yuwubao.services.impl;

import com.yuwubao.entities.FluxEntity;
import com.yuwubao.entities.repository.FluxRepo;
import com.yuwubao.services.FluxService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FluxServiceImpl implements FluxService {
    @Resource
    private FluxRepo fluxRepo;


    @Override
    public FluxEntity getFlux() {
        FluxEntity entity = fluxRepo.findOne(1);
        int num = entity.getFluxnum();
        entity.setFluxnum(num + 1);
        fluxRepo.save(entity);
        return entity;
    }
}
