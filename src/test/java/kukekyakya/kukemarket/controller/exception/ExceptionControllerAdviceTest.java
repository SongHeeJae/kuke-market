package kukekyakya.kukemarket.controller.exception;

import kukekyakya.kukemarket.advice.ExceptionAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ExceptionControllerAdviceTest {
    @InjectMocks ExceptionController exceptionController;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(exceptionController).setControllerAdvice(new ExceptionAdvice()).build();
    }

    @Test
    void entryPointTest() throws Exception{
        // given, when, then
        mockMvc.perform(
                get("/exception/entry-point"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(-1001));
    }

    @Test
    void accessDeniedTest() throws Exception {
        // given, when, then
        mockMvc.perform(
                get("/exception/access-denied"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(-1002));
    }

}

