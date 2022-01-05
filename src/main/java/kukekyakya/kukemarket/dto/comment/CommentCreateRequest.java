package kukekyakya.kukemarket.dto.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

@ApiModel(value = "댓글 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequest {

    @ApiModelProperty(value = "댓글", notes = "댓글을 입력해주세요", required = true, example = "my comment")
    @NotBlank(message = "{commentCreateRequest.content.notBlank}")
    private String content;

    @ApiModelProperty(value = "게시글 아이디", notes = "게시글 아이디를 입력해주세요", example = "7")
    @NotNull(message = "{commentCreateRequest.postId.notNull}")
    @Positive(message = "{commentCreateRequest.postId.positive}")
    private Long postId;

    @ApiModelProperty(hidden = true)
    @Null
    private Long memberId;

    @ApiModelProperty(value = "부모 댓글 아이디", notes = "부모 댓글 아이디를 입력해주세요", example = "7")
    private Long parentId;
}