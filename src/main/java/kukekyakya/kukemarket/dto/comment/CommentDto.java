package kukekyakya.kukemarket.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import kukekyakya.kukemarket.dto.member.MemberDto;
import kukekyakya.kukemarket.entity.comment.Comment;
import kukekyakya.kukemarket.helper.NestedConvertHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private MemberDto member;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private List<CommentDto> children;

    public static List<CommentDto> toDtoList(List<Comment> comments) {
        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                comments,
                c -> new CommentDto(c.getId(), c.isDeleted() ? null : c.getContent(), c.isDeleted() ? null : MemberDto.toDto(c.getMember()), c.getCreatedAt(), new ArrayList<>()),
                c -> c.getParent(),
                c -> c.getId(),
                d -> d.getChildren());
        return helper.convert();
    }
}
