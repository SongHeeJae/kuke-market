package kukekyakya.kukemarket.controller.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import kukekyakya.kukemarket.dto.comment.CommentCreateRequest;
import kukekyakya.kukemarket.dto.comment.CommentReadCondition;
import kukekyakya.kukemarket.dto.message.MessageCreateRequest;
import kukekyakya.kukemarket.dto.message.MessageReadCondition;
import kukekyakya.kukemarket.factory.dto.MessageCreateRequestFactory;
import kukekyakya.kukemarket.factory.dto.MessageReadConditionFactory;
import kukekyakya.kukemarket.service.message.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static kukekyakya.kukemarket.factory.dto.CommentCreateRequestFactory.createCommentCreateRequestWithMemberId;
import static kukekyakya.kukemarket.factory.dto.CommentReadConditionFactory.createCommentReadCondition;
import static kukekyakya.kukemarket.factory.dto.MessageCreateRequestFactory.*;
import static kukekyakya.kukemarket.factory.dto.MessageReadConditionFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {
    @InjectMocks MessageController messageController;
    @Mock MessageService messageService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
    }

    @Test
    void readAllBySenderTest() throws Exception {
        // given
        Long lastMessageId = 1L;
        Integer size = 2;

        // when, then
        mockMvc.perform(
                get("/api/messages/sender")
                        .param("lastMessageId", String.valueOf(lastMessageId))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());

        verify(messageService).readAllBySender(any(MessageReadCondition.class));
    }

    @Test
    void readAllByReceiverTest() throws Exception {
        // given
        Long lastMessageId = 1L;
        Integer size = 2;

        // when, then
        mockMvc.perform(
                get("/api/messages/receiver")
                        .param("lastMessageId", String.valueOf(lastMessageId))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());

        verify(messageService).readAllByReceiver(any(MessageReadCondition.class));
    }

    @Test
    void readTest() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                get("/api/messages/{id}", id))
                .andExpect(status().isOk());
        verify(messageService).read(id);
    }

    @Test
    void createTest() throws Exception {
        // given
        MessageCreateRequest req = createMessageCreateRequest("content", null, 2L);

        // when, then
        mockMvc.perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        verify(messageService).create(req);
    }

    @Test
    void deleteBySenderTest() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                delete("/api/messages/sender/{id}", id))
                .andExpect(status().isOk());
        verify(messageService).deleteBySender(id);
    }

    @Test
    void deleteByReceiverTest() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                delete("/api/messages/receiver/{id}", id))
                .andExpect(status().isOk());
        verify(messageService).deleteByReceiver(id);
    }

}