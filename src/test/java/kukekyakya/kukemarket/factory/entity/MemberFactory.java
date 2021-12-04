package kukekyakya.kukemarket.factory.entity;

import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.entity.member.Role;

import java.util.List;

import static java.util.Collections.emptyList;

public class MemberFactory {

    public static Member createMember() {
        return new Member("email@email.com", "123456a!", "username", "nickname", emptyList());
    }

    public static Member createMember(String email, String password, String username, String nickname) {
        return new Member(email, password, username, nickname, emptyList());
    }

    public static Member createMemberWithRoles(List<Role> roles) {
        return new Member("email@email.com", "123456a!", "username", "nickname", roles);
    }

}
