package kukekyakya.kukemarket.repository.message;

import kukekyakya.kukemarket.config.QuerydslConfig;
import kukekyakya.kukemarket.dto.message.MessageSimpleDto;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.entity.message.Message;
import kukekyakya.kukemarket.exception.MessageNotFoundException;
import kukekyakya.kukemarket.factory.entity.MemberFactory;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static kukekyakya.kukemarket.factory.entity.MessageFactory.createMessage;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class MessageRepositoryTest {
    @Autowired MessageRepository messageRepository;
    @Autowired MemberRepository memberRepository;
    @PersistenceContext EntityManager em;

    Member sender, receiver;

    @BeforeEach
    void beforeEach() {
        sender = memberRepository.save(MemberFactory.createMember("sender@sender.com", "sender", "sender", "sender"));
        receiver = memberRepository.save(MemberFactory.createMember("receiver@receiver.com", "receiver", "receiver", "receiver"));
    }

    @Test
    void createAndReadTest() {
        // given
        Message message = messageRepository.save(createMessage(sender, receiver));
        clear();

        // when
        Message foundMessage = messageRepository.findById(message.getId()).orElseThrow(MessageNotFoundException::new);

        // then
        assertThat(foundMessage.getId()).isEqualTo(message.getId());
    }

    @Test
    void deleteTest() {
        // given
        Message message = messageRepository.save(createMessage(sender, receiver));

        // when
        messageRepository.delete(message);

        // then
        assertThat(messageRepository.findById(message.getId())).isEmpty();
    }

    @Test
    void deleteCascadeBySenderTest() {
        // given
        Message message = messageRepository.save(createMessage(sender, receiver));
        clear();

        // when
        memberRepository.deleteById(sender.getId());
        clear();

        // then
        assertThat(messageRepository.findById(message.getId())).isEmpty();
    }

    @Test
    void deleteCascadeByReceiverTest() {
        // given
        Message message = messageRepository.save(createMessage(sender, receiver));
        clear();

        // when
        memberRepository.deleteById(receiver.getId());
        clear();

        // then
        assertThat(messageRepository.findById(message.getId())).isEmpty();
    }

    @Test
    void findWithSenderAndReceiverByIdTest() {
        // given
        Message message = messageRepository.save(createMessage(sender, receiver));
        clear();

        // when
        Message foundMessage = messageRepository.findWithSenderAndReceiverById(message.getId()).orElseThrow(MessageNotFoundException::new);

        // then
        assertThat(foundMessage.getId()).isEqualTo(message.getId());
        assertThat(foundMessage.getSender().getEmail()).isEqualTo(sender.getEmail());
        assertThat(foundMessage.getReceiver().getEmail()).isEqualTo(receiver.getEmail());
    }

    @Test
    void findAllBySenderIdOrderByMessageIdDescTest() {
        // given
        List<Message> messages = IntStream.range(0, 4)
                .mapToObj(i -> messageRepository.save(createMessage(sender, receiver))).collect(toList());
        messages.get(2).deleteBySender();
        final int size = 2;
        clear();

        // when
        Slice<MessageSimpleDto> result1 = messageRepository.findAllBySenderIdOrderByMessageIdDesc(sender.getId(), Long.MAX_VALUE, Pageable.ofSize(size));
        List<MessageSimpleDto> content1 = result1.getContent();
        Long lastMessageId1 = content1.get(content1.size() - 1).getId();

        Slice<MessageSimpleDto> result2 = messageRepository.findAllBySenderIdOrderByMessageIdDesc(sender.getId(), lastMessageId1, Pageable.ofSize(size));
        List<MessageSimpleDto> content2 = result2.getContent();

        // then
        assertThat(result1.hasNext()).isTrue();
        assertThat(result1.getNumberOfElements()).isEqualTo(2);
        assertThat(content1.get(0).getId()).isEqualTo(messages.get(3).getId());
        assertThat(content1.get(1).getId()).isEqualTo(messages.get(1).getId());

        assertThat(result2.hasNext()).isFalse();
        assertThat(result2.getNumberOfElements()).isEqualTo(1);
        assertThat(content2.get(0).getId()).isEqualTo(messages.get(0).getId());
    }

    @Test
    void findAllByReceiverIdOrderByMessageIdDescTest() {
        // given
        List<Message> messages = IntStream.range(0, 4)
                .mapToObj(i -> messageRepository.save(createMessage(sender, receiver))).collect(toList());
        messages.get(2).deleteByReceiver();
        final int size = 2;
        clear();

        // when
        Slice<MessageSimpleDto> result1 = messageRepository.findAllByReceiverIdOrderByMessageIdDesc(receiver.getId(), Long.MAX_VALUE, Pageable.ofSize(size));
        List<MessageSimpleDto> content1 = result1.getContent();
        Long lastMessageId1 = content1.get(content1.size() - 1).getId();

        Slice<MessageSimpleDto> result2 = messageRepository.findAllByReceiverIdOrderByMessageIdDesc(receiver.getId(), lastMessageId1, Pageable.ofSize(size));
        List<MessageSimpleDto> content2 = result2.getContent();

        // then
        assertThat(result1.hasNext()).isTrue();
        assertThat(result1.getNumberOfElements()).isEqualTo(2);
        assertThat(content1.get(0).getId()).isEqualTo(messages.get(3).getId());
        assertThat(content1.get(1).getId()).isEqualTo(messages.get(1).getId());

        assertThat(result2.hasNext()).isFalse();
        assertThat(result2.getNumberOfElements()).isEqualTo(1);
        assertThat(content2.get(0).getId()).isEqualTo(messages.get(0).getId());
    }

    void clear() {
        em.flush();
        em.clear();
    }
}