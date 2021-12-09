package kukekyakya.kukemarket.repository.post;

import kukekyakya.kukemarket.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
