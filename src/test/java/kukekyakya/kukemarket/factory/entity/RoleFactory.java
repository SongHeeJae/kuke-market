package kukekyakya.kukemarket.factory.entity;

import kukekyakya.kukemarket.entity.member.Role;
import kukekyakya.kukemarket.entity.member.RoleType;

public class RoleFactory {
    public static Role createRole() {
        return new Role(RoleType.ROLE_NORMAL);
    }
}
