package kukekyakya.kukemarket.dto.post;

import kukekyakya.kukemarket.dto.member.MemberDto;
import kukekyakya.kukemarket.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private Long price;
    private MemberDto member;
    private List<ImageDto> images;

    public static PostDto toDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getPrice(),
                MemberDto.toDto(post.getMember()),
                post.getImages().stream().map(i -> ImageDto.toDto(i)).collect(toList())
        );
    }
}
