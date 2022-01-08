package kukekyakya.kukemarket.service.message;

import kukekyakya.kukemarket.dto.message.MessageCreateRequest;
import kukekyakya.kukemarket.dto.message.MessageDto;
import kukekyakya.kukemarket.dto.message.MessageListDto;
import kukekyakya.kukemarket.dto.message.MessageReadCondition;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.entity.message.Message;
import kukekyakya.kukemarket.exception.MemberNotFoundException;
import kukekyakya.kukemarket.exception.MessageNotFoundException;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import kukekyakya.kukemarket.repository.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    public MessageListDto readAllBySender(MessageReadCondition cond) {
        return MessageListDto.toDto(
                messageRepository.findAllBySenderIdOrderByMessageIdDesc(cond.getMemberId(), cond.getLastMessageId(), Pageable.ofSize(cond.getSize()))
        );
    }

    public MessageListDto readAllByReceiver(MessageReadCondition cond) {
        return MessageListDto.toDto(
                messageRepository.findAllByReceiverIdOrderByMessageIdDesc(cond.getMemberId(), cond.getLastMessageId(), Pageable.ofSize(cond.getSize()))
        );
    }

    @PreAuthorize("@messageGuard.check(#id)")
    public MessageDto read(Long id) {
        return MessageDto.toDto(
                messageRepository.findWithSenderAndReceiverById(id).orElseThrow(MessageNotFoundException::new)
        );
    }

    @Transactional
    public void create(MessageCreateRequest req) {
        Member sender = memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new);
        Member receiver = memberRepository.findById(req.getReceiverId()).orElseThrow(MemberNotFoundException::new);
        Message message = new Message(req.getContent(), sender, receiver);
        messageRepository.save(message);
    }

    @Transactional
    @PreAuthorize("@messageSenderGuard.check(#id)")
    public void deleteBySender(Long id) {
        delete(id, Message::deleteBySender);
    }

    @Transactional
    @PreAuthorize("@messageReceiverGuard.check(#id)")
    public void deleteByReceiver(Long id) {
        delete(id, Message::deleteByReceiver);
    }

    private void delete(Long id, Consumer<Message> delete) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        delete.accept(message);
        if(message.isDeletable()) {
            messageRepository.delete(message);
        }
    }
}
