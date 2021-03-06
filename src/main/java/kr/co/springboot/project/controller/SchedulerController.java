package kr.co.springboot.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.springboot.project.services.SchedulerService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SchedulerController {

  @Autowired
  private SchedulerService schedulerService;

  @Scheduled(cron = "0 0 1 * * ?")
   public void eventStateUpdate() {
      int i = schedulerService.updateEventState();
      log.info("변경완료 :::" + i);
   }
}