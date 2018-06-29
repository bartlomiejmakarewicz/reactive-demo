package com.grapeup.reactivedemo.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class PostHandler {

  private final PostRepository postRepository;

  @Autowired
  public PostHandler(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public Mono<ServerResponse> all(ServerRequest serverRequest) {
    return ok().body(postRepository.findAll(), Post.class);
  }

  public Mono<ServerResponse> create(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(Post.class)
            .flatMap(postRepository::save)
            .flatMap(post -> created(URI.create("/posts/" + post.getId())).build());
  }

  public Mono<ServerResponse> get(ServerRequest serverRequest) {
    return postRepository.findById(serverRequest.pathVariable("id"))
            .flatMap(post -> ok().body(Mono.just(post), Post.class))
            .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> update(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(Post.class).flatMap(post ->
      postRepository.findById(post.getId()).flatMap(persistedPost -> {
        persistedPost.setTitle(post.getTitle());
        persistedPost.setContent(post.getContent());
        return postRepository.save(persistedPost);
      })
    ).flatMap(post -> ServerResponse.noContent().build());
  }

  public Mono<ServerResponse> delete(ServerRequest serverRequest) {
    return ServerResponse.noContent().build(postRepository.deleteById(serverRequest.pathVariable("id")));
  }
}
