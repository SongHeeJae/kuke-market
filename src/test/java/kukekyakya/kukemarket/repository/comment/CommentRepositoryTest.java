package kukekyakya.kukemarket.repository.comment;

import kukekyakya.kukemarket.config.QuerydslConfig;
import kukekyakya.kukemarket.entity.category.Category;
import kukekyakya.kukemarket.entity.comment.Comment;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.entity.post.Post;
import kukekyakya.kukemarket.exception.CommentNotFoundException;
import kukekyakya.kukemarket.repository.category.CategoryRepository;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import kukekyakya.kukemarket.repository.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static kukekyakya.kukemarket.factory.entity.CategoryFactory.createCategory;
import static kukekyakya.kukemarket.factory.entity.CommentFactory.createComment;
import static kukekyakya.kukemarket.factory.entity.MemberFactory.createMember;
import static kukekyakya.kukemarket.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class CommentRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired PostRepository postRepository;
    @Autowired CommentRepository commentRepository;
    @PersistenceContext EntityManager em;

    Member member;
    Category category;
    Post post;

    @BeforeEach
    void beforeEach() {
        member = memberRepository.save(createMember());
        category = categoryRepository.save(createCategory());
        post = postRepository.save(createPost(member, category));
    }

    @Test
    void createAndReadTest() {
        // given
        Comment comment = commentRepository.save(createComment(member, post, null));
        clear();

        // when
        Comment foundComment = commentRepository.findById(comment.getId()).orElseThrow(CommentNotFoundException::new);

        // then
        assertThat(foundComment.getId()).isEqualTo(comment.getId());
    }

    @Test
    void deleteTest() {
        // given
        Comment comment = commentRepository.save(createComment(member, post, null));
        clear();

        // when
        commentRepository.deleteById(comment.getId());

        // then
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    void deleteCascadeByMemberTest() {
        // given
        Comment comment = commentRepository.save(createComment(member, post, null));
        clear();

        // when
        memberRepository.deleteById(member.getId());
        clear();

        // then
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    void deleteCascadeByPostTest() {
        // given
        Comment comment = commentRepository.save(createComment(member, post, null));
        clear();

        // when
        postRepository.deleteById(post.getId());
        clear();

        // then
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    void deleteCascadeByParentTest() {
        // given
        Comment parent = commentRepository.save(createComment(member, post, null));
        Comment child = commentRepository.save(createComment(member, post, parent));
        clear();

        // when
        commentRepository.deleteById(parent.getId());
        clear();

        // then
        assertThat(commentRepository.findById(child.getId())).isEmpty();
    }

    @Test
    void getChildrenTest() {
        // given
        Comment parent = commentRepository.save(createComment(member, post, null));
        commentRepository.save(createComment(member, post, parent));
        commentRepository.save(createComment(member, post, parent));
        clear();

        // when
        Comment comment = commentRepository.findById(parent.getId()).orElseThrow(CommentNotFoundException::new);

        // then
        assertThat(comment.getChildren().size()).isEqualTo(2);
    }

    @Test
    void findWithParentByIdTest() {
        // given
        Comment parent = commentRepository.save(createComment(member, post, null));
        Comment child = commentRepository.save(createComment(member, post, parent));
        clear();

        // when
        Comment comment = commentRepository.findWithParentById(child.getId()).orElseThrow(CommentNotFoundException::new);

        // then
        assertThat(comment.getParent()).isNotNull();
    }

    @Test
    void deleteCommentTest() {
        // given

        // root 1
        // 1 -> 2
        // 2(del) -> 3(del)
        // 2(del) -> 4
        // 3(del) -> 5
        Comment comment1 = commentRepository.save(createComment(member, post, null));
        Comment comment2 = commentRepository.save(createComment(member, post, comment1));
        Comment comment3 = commentRepository.save(createComment(member, post, comment2));
        Comment comment4 = commentRepository.save(createComment(member, post, comment2));
        Comment comment5 = commentRepository.save(createComment(member, post, comment3));

        comment2.delete();
        comment3.delete();
        clear();

        // when
        Comment comment = commentRepository.findWithParentById(comment5.getId()).orElseThrow(CommentNotFoundException::new);
        comment.findDeletableComment().ifPresentOrElse(c -> commentRepository.delete(c), () -> comment5.delete());
        clear();

        // then
        List<Comment> comments = commentRepository.findAll();
        List<Long> commentIds = comments.stream().map(c -> c.getId()).collect(toList());
        assertThat(commentIds.size()).isEqualTo(3);
        assertThat(commentIds).contains(comment1.getId(), comment2.getId(), comment4.getId());
    }

    @Test
    void findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAscTest() {
        // given
        // 1		NULL
        // 2		1
        // 3		1
        // 4		2
        // 5		2
        // 6		4
        // 7		3
        // 8		NULL
        Comment c1 = commentRepository.save(createComment(member, post, null));
        Comment c2 = commentRepository.save(createComment(member, post, c1));
        Comment c3 = commentRepository.save(createComment(member, post, c1));
        Comment c4 = commentRepository.save(createComment(member, post, c2));
        Comment c5 = commentRepository.save(createComment(member, post, c2));
        Comment c6 = commentRepository.save(createComment(member, post, c4));
        Comment c7 = commentRepository.save(createComment(member, post, c3));
        Comment c8 = commentRepository.save(createComment(member, post, null));
        clear();

        // when
        List<Comment> result = commentRepository.findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(post.getId());

        // then
        // 1	NULL
        // 8	NULL
        // 2	1
        // 3	1
        // 4	2
        // 5	2
        // 7	3
        // 6	4
        assertThat(result.size()).isEqualTo(8);
        assertThat(result.get(0).getId()).isEqualTo(c1.getId());
        assertThat(result.get(1).getId()).isEqualTo(c8.getId());
        assertThat(result.get(2).getId()).isEqualTo(c2.getId());
        assertThat(result.get(3).getId()).isEqualTo(c3.getId());
        assertThat(result.get(4).getId()).isEqualTo(c4.getId());
        assertThat(result.get(5).getId()).isEqualTo(c5.getId());
        assertThat(result.get(6).getId()).isEqualTo(c7.getId());
        assertThat(result.get(7).getId()).isEqualTo(c6.getId());
    }

    void clear() {
        em.flush();
        em.clear();
    }

}