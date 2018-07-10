package com.grapeup.reactivedemo.domain;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Component
public class PostHandler {
  private final PostService postService;

  public PostHandler(PostService postService) {
    this.postService = postService;
  }

  public Mono<ServerResponse> all(ServerRequest serverRequest) {
    return ok().body(postService.all(), Post.class);
  }

  public Mono<ServerResponse> create(ServerRequest serverRequest) {
    return postService.create(serverRequest.bodyToMono(Post.class)).flatMap(post -> ok().body(Mono.just(post), Post.class)).switchIfEmpty(noContent().build());
  }

  public Mono<ServerResponse> get(ServerRequest serverRequest) {
    return ok().body(postService.get(serverRequest.pathVariable("id")), Post.class)
            .switchIfEmpty(notFound().build());
  }

  public Mono<ServerResponse> update(ServerRequest serverRequest) {
    return ok().body(postService.update(serverRequest.pathVariable("id"), serverRequest.bodyToMono(Post.class)), Post.class)
            .switchIfEmpty(noContent().build());
  }

  public Mono<ServerResponse> delete(ServerRequest serverRequest) {
    return noContent().build(postService.delete(serverRequest.pathVariable("id")));
  }
}
