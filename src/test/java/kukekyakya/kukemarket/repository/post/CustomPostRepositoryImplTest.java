package kukekyakya.kukemarket.repository.post;

import kukekyakya.kukemarket.config.QuerydslConfig;
import kukekyakya.kukemarket.dto.post.PostReadCondition;
import kukekyakya.kukemarket.dto.post.PostSimpleDto;
import kukekyakya.kukemarket.entity.category.Category;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.entity.post.Post;
import kukekyakya.kukemarket.repository.category.CategoryRepository;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static kukekyakya.kukemarket.factory.dto.PostReadConditionFactory.createPostReadCondition;
import static kukekyakya.kukemarket.factory.entity.CategoryFactory.createCategoryWithName;
import static kukekyakya.kukemarket.factory.entity.MemberFactory.createMember;
import static kukekyakya.kukemarket.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class CustomPostRepositoryImplTest {

    @Autowired PostRepository postRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired CategoryRepository categoryRepository;
    @PersistenceContext EntityManager em;

    @Test
    void findAllByConditionTest() {
        // given
        List<Member> members = saveMember(3);
        List<Category> categories = saveCategory(2);

        // 0 - (m0, c0)
        // 1 - (m1, c1)
        // 2 - (m2, c0)
        // 3 - (m0, c1)
        // 4 - (m1, c0)
        // 5 - (m2, c1)
        // 6 - (m0, c0)
        // 7 - (m1, c1)
        // 8 - (m2, c0)
        // 9 - (m0, c1)
        List<Post> posts = IntStream.range(0, 10)
                .mapToObj(i -> postRepository.save(createPost(members.get(i % 3), categories.get(i % 2))))
                .collect(toList());
        clear();

        List<Long> categoryIds = List.of(categories.get(1).getId());
        List<Long> memberIds = List.of(members.get(0).getId(), members.get(2).getId());
        int sizePerPage = 2;
        long expectedTotalElements = 3;

        PostReadCondition page0Cond = createPostReadCondition(0, sizePerPage, categoryIds, memberIds);
        PostReadCondition page1Cond = createPostReadCondition(1, sizePerPage, categoryIds, memberIds);

        // when
        Page<PostSimpleDto> page0 = postRepository.findAllByCondition(page0Cond);
        Page<PostSimpleDto> page1 = postRepository.findAllByCondition(page1Cond);

        // then
        assertThat(page0.getTotalElements()).isEqualTo(expectedTotalElements);
        assertThat(page0.getTotalPages()).isEqualTo((expectedTotalElements + 1) / sizePerPage);

        assertThat(page0.getContent().size()).isEqualTo(2);
        assertThat(page1.getContent().size()).isEqualTo(1);

        // 9 - (m0, c1)
        // 5 - (m2, c1)
        assertThat(page0.getContent().get(0).getId()).isEqualTo(posts.get(9).getId());
        assertThat(page0.getContent().get(1).getId()).isEqualTo(posts.get(5).getId());
        assertThat(page0.hasNext()).isTrue();

        // 3 - (m0, c1)
        assertThat(page1.getContent().get(0).getId()).isEqualTo(posts.get(3).getId());
        assertThat(page1.hasNext()).isFalse();
    }

    private List<Member> saveMember(int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> memberRepository.save(createMember("member" + i, "member" + i, "member" + i, "member" + i)))
                .collect(toList());
    }

    private List<Category> saveCategory(int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> categoryRepository.save(createCategoryWithName("category" + i))).collect(toList());
    }

    private void clear() {
        em.flush();
        em.clear();
    }
}