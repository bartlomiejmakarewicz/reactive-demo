package com.grapeup.reactivedemo.domain;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PostRepository extends ReactiveMongoRepository<Post, String> {
}
