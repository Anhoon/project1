package kr.co.smartcube.xcube.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.smartcube.xcube.mybatis.dao.SchedulerDao;

@Service
public class SchedulerService {

    @Autowired 
    private SchedulerDao schedulerDao;

    @Transactional
    public int updateEventState(){
        int i = schedulerDao.updateEventStatus();
        i = i + schedulerDao.updateParticipateStatus();
        return i;
    }
}