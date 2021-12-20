package kukekyakya.kukemarket.repository.post;

import kukekyakya.kukemarket.dto.post.PostReadCondition;
import kukekyakya.kukemarket.dto.post.PostSimpleDto;
import org.springframework.data.domain.Page;

public interface CustomPostRepository {
    Page<PostSimpleDto> findAllByCondition(PostReadCondition cond);
}
