package kukekyakya.kukemarket.factory.entity;

import kukekyakya.kukemarket.entity.comment.Comment;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.entity.post.Post;

import static kukekyakya.kukemarket.factory.entity.MemberFactory.*;
import static kukekyakya.kukemarket.factory.entity.PostFactory.*;

public class CommentFactory {

    public static Comment createComment(Comment parent) {
        return new Comment("content", createMember(), createPost(), parent);
    }

    public static Comment createDeletedComment(Comment parent) {
        Comment comment = new Comment("content", createMember(), createPost(), parent);
        comment.delete();
        return comment;
    }

    public static Comment createComment(Member member, Post post, Comment parent) {
        return new Comment("content", member, post, parent);
    }
}
