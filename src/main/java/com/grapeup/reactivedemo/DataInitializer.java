package com.grapeup.reactivedemo;

import com.grapeup.reactivedemo.domain.Post;
import com.grapeup.reactivedemo.domain.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {
  private final PostRepository postRepository;

  public DataInitializer(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Override
  public void run(String... args) {
    log.info("Initializing data...");
    postRepository.deleteAll()
            .thenMany(Flux.just("Post one", "Post two", "Post three")
                    .flatMap(title -> postRepository.save(Post.builder().title(title).build())))
            .log()
            .doOnComplete(() -> log.info("Initialization complete"))
            .subscribe();
  }
}
