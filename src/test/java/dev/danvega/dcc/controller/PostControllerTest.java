package dev.danvega.dcc.controller;

import dev.danvega.dcc.model.Post;
import dev.danvega.dcc.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    PostRepository posts;

    @BeforeEach
    void setUp() {
        posts.save(new Post(1,"Hello, World","This is my first post"));
    }

    @Test
    void shouldReturnAllPosts() {
        ResponseEntity<List<Post>> exchange = restTemplate.exchange("/api/posts", HttpMethod.GET, null, new ParameterizedTypeReference<List<Post>>() {
        });
        assertEquals(1,exchange.getBody().size());
    }

    @Test
    void shouldReturnPostWithValidId() {
        Post post = restTemplate.getForObject("/api/posts/1", Post.class);
        assertNotNull(post);
        assertEquals("Hello, World",post.getTitle());
        assertEquals("This is my first post",post.getContent());
        assertTrue(post.getPublishedOn().isBefore(ChronoLocalDateTime.from(LocalDateTime.now())));
        assertTrue(post.getUpdatedOn().isBefore(ChronoLocalDateTime.from(LocalDateTime.now())));
    }

    @Test
    void shouldNotReturnPostWithInvalidId() {
        Post post = restTemplate.getForObject("/api/posts/99", Post.class);
        assertNull(post.getId());
    }

}