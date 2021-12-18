package kukekyakya.kukemarket.entity.comment;

import kukekyakya.kukemarket.entity.common.EntityDate;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.entity.post.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Lob
    private String content;

    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    public Comment(String content, Member member, Post post, Comment parent) {
        this.content = content;
        this.member = member;
        this.post = post;
        this.parent = parent;
        this.deleted = false;
    }

    // 자식이 없다면 제거
    // 자식이 없어서 제거될 때 삭제 표시된 부모도 모두 제거
    // 자식이 있다면 삭제 표시
    // 다 제거된 자식이라면 그냥 삭제해버림

    public Optional<Comment> findDeletableComment() {
        return hasChildren() ? Optional.empty() : Optional.of(findDeletableCommentByParent());
    }

    public void delete() {
        this.deleted = true;
    }

    private Comment findDeletableCommentByParent() {
        return isDeletableParent() ? parent.findDeletableCommentByParent() : this;
    }

    private boolean hasChildren() {
        return children.size() != 0;
    }

    private boolean isDeletableParent() {
        return parent != null && parent.deleted && parent.children.size() == 1;
    }
}
