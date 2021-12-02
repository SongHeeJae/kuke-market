package kukekyakya.kukemarket.service.member;

import kukekyakya.kukemarket.dto.member.MemberDto;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.exception.MemberNotFoundException;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks MemberService memberService;
    @Mock MemberRepository memberRepository;

    @Test
    void readTest() {
        // given
        Member member = createMember();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        MemberDto result = memberService.read(1L);

        // then
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void readExceptionByMemberNotFoundTest() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> memberService.read(1L)).isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void deleteTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));

        // when
        memberService.delete(1L);

        // then
        verify(memberRepository).delete(any());
    }

    @Test
    void deleteExceptionByMemberNotFoundTest() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> memberService.delete(1L)).isInstanceOf(MemberNotFoundException.class);
    }


    private Member createMember() {
        return new Member("email@email.com", "123456a!", "username", "nickname", List.of());
    }
}