package com.log_server.log_server.controller;


import com.log_server.log_server.model.LogEntity;
import com.log_server.log_server.service.QueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/logs/query")
public class QueryController {

    private final QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    public Flux<LogEntity> getLogs(
            @RequestParam(value = "service", required = false) String service,
            @RequestParam(value = "level", required = false) String level,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "is.blacklisted", required = false) Boolean isBlacklisted,
            @RequestParam(value = "limit", required = false) Integer limit
    ) {
        return queryService.queryLogs(service, level, username, isBlacklisted, limit);
    }
}
