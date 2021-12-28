package kukekyakya.kukemarket.factory.entity;

import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.entity.message.Message;

import static kukekyakya.kukemarket.factory.entity.MemberFactory.createMember;

public class MessageFactory {
    public static Message createMessage() {
        return new Message("content", createMember(), createMember());
    }

    public static Message createMessage(Member sender, Member receiver) {
        return new Message("content", sender, receiver);
    }
}
