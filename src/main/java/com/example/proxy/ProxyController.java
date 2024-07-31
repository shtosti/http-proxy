/*
REST controller that handles incoming requests
*/



package com.example.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class ProxyController {

    private final UniqueIdService uniqueIdService;

    @Autowired
    public ProxyController(UniqueIdService uniqueIdService) {
        this.uniqueIdService = uniqueIdService;
    }

    @PostMapping("/endpoint")
    public String handleRequest(@RequestParam String id, @RequestParam String url) {
        // uniqueIdService.addId(id);
        uniqueIdService.addId(id, url);
        uniqueIdService.callExternalEndpoint(url);
        return "Request handled successfuly"; // if success
    }
}

