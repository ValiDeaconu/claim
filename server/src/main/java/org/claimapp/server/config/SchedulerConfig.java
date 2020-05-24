package org.claimapp.server.config;

import org.claimapp.common.dto.SingletonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class SchedulerConfig {

    private final SimpMessagingTemplate template;

    @Autowired
    public SchedulerConfig(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Scheduled(fixedDelay = 3000)
    public void sendMessages() {
        template.convertAndSend("/topic/status", new SingletonDTO<>(true));
    }
}
