package com.log_server.log_server.repository;//package com.log_server.log_server.repository;
//
//
//import com.log_server.log_server.model.LogEntity;
//import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
//import reactor.core.publisher.Flux;
//
//public interface LogRepository extends ReactiveMongoRepository<LogEntity, String> {
//    Flux<LogEntity> findByEventCategory(String eventCategory);
//    Flux<LogEntity> findBySeverity(String severity);
//    Flux<LogEntity> findByUsername(String username);
//    Flux<LogEntity> findByBlacklisted(boolean blacklisted);
//}
