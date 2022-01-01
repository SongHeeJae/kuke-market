package kukekyakya.kukemarket.service.alarm;

import kukekyakya.kukemarket.dto.alarm.AlarmInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LineAlarmService implements AlarmService {
    @Override
    public void alarm(AlarmInfoDto infoDto) {
        log.info("{} 에게 라인 전송 = {}", infoDto.getTarget().getNickname(), infoDto.getMessage());
    }
}
