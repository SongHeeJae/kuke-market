package kukekyakya.kukemarket.repository.post;

import kukekyakya.kukemarket.dto.post.PostSimpleDto;
import kukekyakya.kukemarket.dto.post.PostUpdateRequest;
import kukekyakya.kukemarket.entity.category.Category;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.entity.post.Image;
import kukekyakya.kukemarket.entity.post.Post;
import kukekyakya.kukemarket.exception.PostNotFoundException;
import kukekyakya.kukemarket.repository.category.CategoryRepository;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static kukekyakya.kukemarket.factory.dto.PostUpdateRequestFactory.createPostUpdateRequest;
import static kukekyakya.kukemarket.factory.entity.CategoryFactory.createCategory;
import static kukekyakya.kukemarket.factory.entity.ImageFactory.createImage;
import static kukekyakya.kukemarket.factory.entity.ImageFactory.createImageWithOriginName;
import static kukekyakya.kukemarket.factory.entity.MemberFactory.createMember;
import static kukekyakya.kukemarket.factory.entity.PostFactory.createPost;
import static kukekyakya.kukemarket.factory.entity.PostFactory.createPostWithImages;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class PostRepositoryTest {
    @Autowired PostRepository postRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired ImageRepository imageRepository;
    @PersistenceContext EntityManager em;

    Member member;
    Category category;

    @BeforeEach
    void beforeEach() {
        member = memberRepository.save(createMember());
        category = categoryRepository.save(createCategory());
    }

    @Test
    void createAndReadTest() {
        // given
        Post post = postRepository.save(createPost(member, category));
        clear();

        // when
        Post foundPost = postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);

        // then
        assertThat(foundPost.getId()).isEqualTo(post.getId());
        assertThat(foundPost.getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    void deleteTest() {
        // given
        Post post = postRepository.save(createPost(member, category));
        clear();

        // when
        postRepository.deleteById(post.getId());
        clear();

        // then
        assertThatThrownBy(() -> postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void createCascadeImageTest() {
        // given
        Post post = postRepository.save(createPostWithImages(member, category, List.of(createImage(), createImage())));
        clear();

        // when
        Post foundPost = postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);

        // then
        List<Image> images = foundPost.getImages();
        assertThat(images.size()).isEqualTo(2);
    }

    @Test
    void deleteCascadeImageTest() {
        // given
        Post post = postRepository.save(createPostWithImages(member, category, List.of(createImage(), createImage())));
        clear();

        // when
        postRepository.deleteById(post.getId());
        clear();

        // then
        List<Image> images = imageRepository.findAll();
        assertThat(images.size()).isZero();
    }

    @Test
    void deleteCascadeByMemberTest() {
        // given
        postRepository.save(createPostWithImages(member, category, List.of(createImage(), createImage())));
        clear();

        // when
        memberRepository.deleteById(member.getId());
        clear();

        // then
        List<Post> result = postRepository.findAll();
        Assertions.assertThat(result.size()).isZero();
    }

    @Test
    void deleteCascadeByCategoryTest() {
        // given
        postRepository.save(createPostWithImages(member, category, List.of(createImage(), createImage())));
        clear();

        // when
        categoryRepository.deleteById(category.getId());
        clear();

        // then
        List<Post> result = postRepository.findAll();
        assertThat(result.size()).isZero();
    }

    @Test
    void updateTest() {
        // given
        Image a = createImageWithOriginName("a.jpg");
        Image b = createImageWithOriginName("b.png");
        Post post = postRepository.save(createPostWithImages(member, category, List.of(a, b)));
        clear();

        // when
        MockMultipartFile cFile = new MockMultipartFile("c", "c.png", MediaType.IMAGE_PNG_VALUE, "cFile".getBytes());
        PostUpdateRequest postUpdateRequest = createPostUpdateRequest("update title", "update content", 1234L, List.of(cFile), List.of(a.getId()));
        Post foundPost = postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);
        foundPost.update(postUpdateRequest);
        clear();

        // then
        Post result = postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);
        assertThat(result.getTitle()).isEqualTo(postUpdateRequest.getTitle());
        assertThat(result.getContent()).isEqualTo(postUpdateRequest.getContent());
        assertThat(result.getPrice()).isEqualTo(postUpdateRequest.getPrice());

        List<Image> images = result.getImages();
        List<String> originNames = images.stream().map(i -> i.getOriginName()).collect(toList());
        assertThat(images.size()).isEqualTo(2);
        assertThat(originNames).contains(b.getOriginName(), cFile.getOriginalFilename());

        List<Image> resultImages = imageRepository.findAll();
        assertThat(resultImages.size()).isEqualTo(2);
    }

    @Test
    void findByIdWithMemberTest() {
        // given
        Post post = postRepository.save(createPost(member, category));

        // when
        Post foundPost = postRepository.findByIdWithMember(post.getId()).orElseThrow(PostNotFoundException::new);

        // then
        Member foundMember = foundPost.getMember();
        assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void findAllWithMemberOrderByIdDescTest() {
        // given
        List<Post> savedPost = new ArrayList<>();
        IntStream.range(0, 5).forEach(i -> {
            Post post = postRepository.save(createPost(member, category));
            savedPost.add(post);
        });
        clear();

        // when
        Page<PostSimpleDto> page1 = postRepository.findAllWithMemberOrderByIdDesc(PageRequest.of(0, 2));
        Page<PostSimpleDto> page2 = postRepository.findAllWithMemberOrderByIdDesc(PageRequest.of(1, 2));
        Page<PostSimpleDto> page3 = postRepository.findAllWithMemberOrderByIdDesc(PageRequest.of(2, 2));

        // then
        assertThat(page1.getTotalElements()).isEqualTo(savedPost.size());
        assertThat(page1.getTotalPages()).isEqualTo((savedPost.size() + 1) / 2);

        assertThat(page1.getContent().size()).isEqualTo(2);
        assertThat(page2.getContent().size()).isEqualTo(2);
        assertThat(page3.getContent().size()).isEqualTo(1);

        assertThat(page1.getContent().get(0).getId()).isEqualTo(savedPost.get(4).getId());
        assertThat(page1.getContent().get(1).getId()).isEqualTo(savedPost.get(3).getId());
        assertThat(page1.hasNext()).isTrue();

        assertThat(page2.getContent().get(0).getId()).isEqualTo(savedPost.get(2).getId());
        assertThat(page2.getContent().get(1).getId()).isEqualTo(savedPost.get(1).getId());
        assertThat(page2.hasNext()).isTrue();

        assertThat(page3.getContent().get(0).getId()).isEqualTo(savedPost.get(0).getId());
        assertThat(page3.hasNext()).isFalse();
    }

    void clear() {
        em.flush();
        em.clear();
    }
}