package kukekyakya.kukemarket.dto.alarm;

import kukekyakya.kukemarket.dto.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlarmInfoDto {
    private MemberDto target;
    private String message;
}
