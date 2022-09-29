package dev.danvega.dcc.repository;

import dev.danvega.dcc.model.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post,Integer> {
}
