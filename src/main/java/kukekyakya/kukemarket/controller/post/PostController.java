package kukekyakya.kukemarket.controller.post;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kukekyakya.kukemarket.dto.post.PostCreateRequest;
import kukekyakya.kukemarket.dto.response.Response;
import kukekyakya.kukemarket.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Member Controller", tags = "Member")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    @ApiOperation(value = "게시글 생성", notes = "게시글을 생성한다.")
    @PostMapping("/api/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@Valid @ModelAttribute PostCreateRequest req) {
        return Response.success(postService.create(req));
    }

    @ApiOperation(value = "게시글 조회", notes = "게시글을 조회한다.")
    @GetMapping("/api/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@ApiParam(value = "게시글 id", required = true) @PathVariable Long id) {
        return Response.success(postService.read(id));
    }
}
