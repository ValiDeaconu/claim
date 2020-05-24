package org.claimapp.server.api;

import org.claimapp.common.dto.SingletonDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class StatusController {

    @GetMapping("/status")
    public SingletonDTO<Boolean> checkServerStatus() {
        return new SingletonDTO<>(true);
    }
}
