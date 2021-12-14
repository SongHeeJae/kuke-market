package kukekyakya.kukemarket.factory.dto;

import kukekyakya.kukemarket.dto.post.PostUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PostUpdateRequestFactory {
    public static PostUpdateRequest createPostUpdateRequest(String title, String content, Long price, List<MultipartFile> addedImages, List<Long> deletedImages) {
        return new PostUpdateRequest(title, content, price, addedImages, deletedImages);
    }
}
