package com.grapeup.reactivedemo.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class PostServiceImplTest {

  @Mock
  private PostRepository postRepository;

  private PostServiceImpl postService;

  @Before
  public void setUp() {
    postService = new PostServiceImpl(postRepository);
  }

  @Test
  public void all() {
    Post post = Post.builder().id("id").title("title").content("content").build();
    given(postRepository.findAll()).willReturn(Flux.just(post));

    StepVerifier.create(postService.all()).expectNext(post).expectComplete().verify();
    verify(postRepository, times(1)).findAll();
  }

  @Test
  public void get() {
    Post post = Post.builder().id("id").title("title").content("content").build();
    given(postRepository.findById(anyString())).willReturn(Mono.just(post));

    StepVerifier.create(postService.get("id")).expectNext(post).expectComplete().verify();
    verify(postRepository, times(1)).findById(anyString());
  }

  @Test
  public void create() {
    Post post = Post.builder().id("id").title("title").content("content").build();
    given(postRepository.save(any(Post.class))).willReturn(Mono.just(post));

    StepVerifier.create(postService.create(Mono.just(post))).expectNext(post).expectComplete().verify();
    verify(postRepository, times(1)).save(any(Post.class));
  }

  @Test
  public void update() {
    Post post = Post.builder().id("id").title("title").content("content").build();
    given(postRepository.findById(anyString())).willReturn(Mono.just(post));
    given(postRepository.save(any(Post.class))).willReturn(Mono.just(post));

    StepVerifier.create(postService.update("id", Mono.just(post))).expectNext(post).expectComplete().verify();
    verify(postRepository, times(1)).save(any(Post.class));
  }

  @Test
  public void delete() {
    postService.delete("id");
    verify(postRepository, times(1)).deleteById("id");
  }
}