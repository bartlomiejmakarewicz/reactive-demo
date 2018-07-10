package com.grapeup.reactivedemo.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostService {
  Flux<Post> all();
  Mono<Post> get(String id);
  Mono<Post> create(Mono<Post> dto);
  Mono<Post> update(String id, Mono<Post> dto);
  Mono<Void> delete(String id);
}
