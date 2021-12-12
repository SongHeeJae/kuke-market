package kukekyakya.kukemarket.controller.post;

import kukekyakya.kukemarket.dto.post.PostCreateRequest;
import kukekyakya.kukemarket.dto.sign.SignInResponse;
import kukekyakya.kukemarket.entity.category.Category;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.entity.post.Post;
import kukekyakya.kukemarket.exception.MemberNotFoundException;
import kukekyakya.kukemarket.init.TestInitDB;
import kukekyakya.kukemarket.repository.category.CategoryRepository;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import kukekyakya.kukemarket.repository.post.PostRepository;
import kukekyakya.kukemarket.service.sign.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static kukekyakya.kukemarket.factory.dto.PostCreateRequestFactory.createPostCreateRequest;
import static kukekyakya.kukemarket.factory.dto.SignInRequestFactory.createSignInRequest;
import static kukekyakya.kukemarket.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@Transactional
public class PostControllerIntegrationTest {
    @Autowired WebApplicationContext context;
    @Autowired MockMvc mockMvc;

    @Autowired TestInitDB initDB;
    @Autowired CategoryRepository categoryRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired PostRepository postRepository;
    @Autowired SignService signService;

    Member member;
    Category category;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        initDB.initDB();
        member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);
        category = categoryRepository.findAll().get(0);
    }

    @Test
    void createTest() throws Exception {
        // given
        SignInResponse signInRes = signService.signIn(createSignInRequest(member.getEmail(), initDB.getPassword()));
        PostCreateRequest req = createPostCreateRequest("title", "content", 1000L, member.getId(), category.getId(), List.of());

        // when, then
        mockMvc.perform(
                multipart("/api/posts")
                        .param("title", req.getTitle())
                        .param("content", req.getContent())
                        .param("price", String.valueOf(req.getPrice()))
                        .param("memberId", String.valueOf(req.getMemberId()))
                        .param("categoryId", String.valueOf(req.getCategoryId()))
                        .with(requestPostProcessor -> {
                            requestPostProcessor.setMethod("POST");
                            return requestPostProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", signInRes.getAccessToken()))
                .andExpect(status().isCreated());

        Post post = postRepository.findAll().get(0);
        assertThat(post.getTitle()).isEqualTo("title");
        assertThat(post.getContent()).isEqualTo("content");
    }

    @Test
    void createUnauthorizedByNoneTokenTest() throws Exception {
        // given
        PostCreateRequest req = createPostCreateRequest("title", "content", 1000L, member.getId(), category.getId(), List.of());

        // when, then
        mockMvc.perform(
                multipart("/api/posts")
                        .param("title", req.getTitle())
                        .param("content", req.getContent())
                        .param("price", String.valueOf(req.getPrice()))
                        .param("memberId", String.valueOf(req.getMemberId()))
                        .param("categoryId", String.valueOf(req.getCategoryId()))
                        .with(requestPostProcessor -> {
                            requestPostProcessor.setMethod("POST");
                            return requestPostProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @Test
    void readTest() throws Exception {
        // given
        Post post = postRepository.save(createPost(member, category));

        // when, then
        mockMvc.perform(
                get("/api/posts/{id}", post.getId()))
                .andExpect(status().isOk());
    }
}
