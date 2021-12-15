package kukekyakya.kukemarket.factory.dto;

import kukekyakya.kukemarket.dto.post.PostReadCondition;

public class PostReadConditionFactory {
    public static PostReadCondition createPostReadCondition(Integer page, Integer size) {
        return new PostReadCondition(page, size);
    }
}
