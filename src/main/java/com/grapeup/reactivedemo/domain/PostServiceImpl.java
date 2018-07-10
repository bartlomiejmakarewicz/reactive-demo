package com.grapeup.reactivedemo.domain;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostServiceImpl implements PostService {
  private final PostRepository postRepository;

  public PostServiceImpl(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Override
  public Flux<Post> all() {
    return postRepository.findAll();
  }

  @Override
  public Mono<Post> get(String id) {
    return postRepository.findById(id);
  }

  @Override
  public Mono<Post> create(Mono<Post> dto) {
    return dto.flatMap(postRepository::save);
  }

  @Override
  public Mono<Post> update(String id, Mono<Post> dto) {
    return postRepository.findById(id).zipWith(dto, (persistedPost, postUpdate) -> {
      persistedPost.setTitle(postUpdate.getTitle());
      persistedPost.setContent(postUpdate.getContent());
      return persistedPost;
    }).flatMap(postRepository::save);
  }

  @Override
  public Mono<Void> delete(String id) {
    return postRepository.deleteById(id);
  }
}
