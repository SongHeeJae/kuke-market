package kukekyakya.kukemarket.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class PostListDto {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<PostSimpleDto> postList;

    public static PostListDto toDto(Page<PostSimpleDto> page) {
        return new PostListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}