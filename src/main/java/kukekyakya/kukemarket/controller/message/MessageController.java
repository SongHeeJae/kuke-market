package kukekyakya.kukemarket.controller.message;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kukekyakya.kukemarket.aop.AssignMemberId;
import kukekyakya.kukemarket.dto.message.MessageCreateRequest;
import kukekyakya.kukemarket.dto.message.MessageReadCondition;
import kukekyakya.kukemarket.dto.response.Response;
import kukekyakya.kukemarket.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Message Controller", tags = "Message")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final MessageService messageService;

    @ApiOperation(value = "송신자의 쪽지 목록 조회", notes = "송신자의 쪽지 목록을 조회한다.")
    @GetMapping("/api/messages/sender")
    @ResponseStatus(HttpStatus.OK)
    @AssignMemberId
    public Response readAllBySender(@Valid MessageReadCondition cond) {
        return Response.success(messageService.readAllBySender(cond));
    }

    @ApiOperation(value = "수신자의 쪽지 목록 조회", notes = "수신자의 쪽지 목록을 조회한다.")
    @GetMapping("/api/messages/receiver")
    @ResponseStatus(HttpStatus.OK)
    @AssignMemberId
    public Response readAllByReceiver(@Valid MessageReadCondition cond) {
        return Response.success(messageService.readAllByReceiver(cond));
    }

    @ApiOperation(value = "쪽지 조회", notes = "쪽지를 조회한다.")
    @GetMapping("/api/messages/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@ApiParam(value = "쪽지 id", required = true) @PathVariable Long id) {
        return Response.success(messageService.read(id));
    }

    @ApiOperation(value = "쪽지 생성", notes = "쪽지를 생성한다.")
    @PostMapping("/api/messages")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response create(@Valid @RequestBody MessageCreateRequest req) {
        messageService.create(req);
        return Response.success();
    }

    @ApiOperation(value = "송신자의 쪽지 삭제", notes = "송신자의 쪽지를 삭제한다.")
    @DeleteMapping("/api/messages/sender/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteBySender(@ApiParam(value = "쪽지 id", required = true) @PathVariable Long id) {
        messageService.deleteBySender(id);
        return Response.success();
    }

    @ApiOperation(value = "수신자의 쪽지 삭제", notes = "수신자의 쪽지를 삭제한다.")
    @DeleteMapping("/api/messages/receiver/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteByReceiver(@ApiParam(value = "쪽지 id", required = true) @PathVariable Long id) {
        messageService.deleteByReceiver(id);
        return Response.success();
    }
}
