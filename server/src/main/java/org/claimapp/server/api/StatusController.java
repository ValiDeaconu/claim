package org.claimapp.server.api;

import org.claimapp.server.dto.SingletonDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
// @CrossOrigin(origins = "*")
public class StatusController {

    @GetMapping("/status")
    public SingletonDTO<Boolean> checkServerStatus() {
        return new SingletonDTO<>(true);
    }
}
