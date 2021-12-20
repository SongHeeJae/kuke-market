package kukekyakya.kukemarket.controller.comment;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kukekyakya.kukemarket.aop.AssignMemberId;
import kukekyakya.kukemarket.dto.comment.CommentCreateRequest;
import kukekyakya.kukemarket.dto.comment.CommentReadCondition;
import kukekyakya.kukemarket.dto.response.Response;
import kukekyakya.kukemarket.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Comment Controller", tags = "Comment")
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @ApiOperation(value = "댓글 목록 조회", notes = "댓글 목록을 조회한다.")
    @GetMapping("/api/comments")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid CommentReadCondition cond) {
        return Response.success(commentService.readAll(cond));
    }

    @ApiOperation(value = "댓글 생성", notes = "댓글을 생성한다.")
    @PostMapping("/api/comments")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response create(@Valid @RequestBody CommentCreateRequest req) {
        commentService.create(req);
        return Response.success();
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글을 삭제한다.")
    @DeleteMapping("/api/comments/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@ApiParam(value = "댓글 id", required = true) @PathVariable Long id) {
        commentService.delete(id);
        return Response.success();
    }
}
