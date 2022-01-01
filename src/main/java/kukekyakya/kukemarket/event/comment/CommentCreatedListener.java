package kukekyakya.kukemarket.event.comment;

import kukekyakya.kukemarket.dto.alarm.AlarmInfoDto;
import kukekyakya.kukemarket.dto.member.MemberDto;
import kukekyakya.kukemarket.service.alarm.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentCreatedListener {
    private final AlarmService emailAlarmService;
    private final AlarmService lineAlarmService;
    private final AlarmService smsAlarmService;
    private List<AlarmService> alarmServices = new ArrayList<>();

    @PostConstruct
    public void postConstruct() {
        alarmServices.add(emailAlarmService);
        alarmServices.add(lineAlarmService);
        alarmServices.add(smsAlarmService);
    }

    @TransactionalEventListener
    @Async
    public void handleAlarm(CommentCreatedEvent event) {
        log.info("CommentCreatedListener.handleAlarm");
        String message = generateAlarmMessage(event);
        if(isAbleToSendToPostWriter(event)) alarmTo(event.getPostWriter(), message);
        if(isAbleToSendToParentWriter(event)) alarmTo(event.getParentWriter(), message);
    }

    private void alarmTo(MemberDto memberDto, String message) {
        alarmServices.stream().forEach(alarmService -> alarmService.alarm(new AlarmInfoDto(memberDto, message)));
    }

    private boolean isAbleToSendToPostWriter(CommentCreatedEvent event) {
        if(!isSameMember(event.getPublisher(), event.getPostWriter())) {
            if(hasParent(event)) return !isSameMember(event.getPostWriter(), event.getParentWriter());
            return true;
        }
        return false;
    }

    private boolean isAbleToSendToParentWriter(CommentCreatedEvent event) {
        return hasParent(event) && !isSameMember(event.getPublisher(), event.getParentWriter());
    }

    private boolean isSameMember(MemberDto a, MemberDto b) {
        return Objects.equals(a.getId(), b.getId());
    }

    private boolean hasParent(CommentCreatedEvent event) {
        return event.getParentWriter().getId() != null;
    }

    private String generateAlarmMessage(CommentCreatedEvent event) {
        return event.getPublisher().getNickname() + " : " + event.getContent();
    }
}
