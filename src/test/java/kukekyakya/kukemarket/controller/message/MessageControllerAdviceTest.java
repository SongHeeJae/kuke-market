package kukekyakya.kukemarket.controller.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import kukekyakya.kukemarket.advice.ExceptionAdvice;
import kukekyakya.kukemarket.dto.message.MessageCreateRequest;
import kukekyakya.kukemarket.exception.MessageNotFoundException;
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

import static kukekyakya.kukemarket.factory.dto.MessageCreateRequestFactory.createMessageCreateRequest;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MessageControllerAdviceTest {
    @InjectMocks MessageController messageController;
    @Mock MessageService messageService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).setControllerAdvice(new ExceptionAdvice()).build();
    }

    @Test
    void readTest() throws Exception {
        // given
        Long id = 1L;
        given(messageService.read(id)).willThrow(MessageNotFoundException.class);

        // when, then
        mockMvc.perform(
                get("/api/messages/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1016));
    }

    @Test
    void createTest() throws Exception {
        // given
        MessageCreateRequest req = createMessageCreateRequest("content", null, 2L);
        doThrow(MessageNotFoundException.class).when(messageService).create(req);

        // when, then
        mockMvc.perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1016));
    }

    @Test
    void deleteBySenderTest() throws Exception {
        // given
        Long id = 1L;
        doThrow(MessageNotFoundException.class).when(messageService).deleteBySender(id);

        // when, then
        mockMvc.perform(
                delete("/api/messages/sender/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1016));
    }

    @Test
    void deleteByReceiverTest() throws Exception {
        // given
        Long id = 1L;
        doThrow(MessageNotFoundException.class).when(messageService).deleteByReceiver(id);

        // when, then
        mockMvc.perform(
                delete("/api/messages/receiver/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1016));
    }
}