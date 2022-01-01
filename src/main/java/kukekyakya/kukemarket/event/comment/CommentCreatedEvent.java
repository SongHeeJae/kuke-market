package kukekyakya.kukemarket.event.comment;

import kukekyakya.kukemarket.dto.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentCreatedEvent {
    private MemberDto publisher;
    private MemberDto postWriter;
    private MemberDto parentWriter;
    private String content;
}
