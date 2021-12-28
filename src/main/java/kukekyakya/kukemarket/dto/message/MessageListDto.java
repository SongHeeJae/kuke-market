package kukekyakya.kukemarket.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

import java.util.List;

@Data
@AllArgsConstructor
public class MessageListDto {
    private int numberOfElements;
    private boolean hasNext;
    private List<MessageSimpleDto> messageList;

    public static MessageListDto toDto(Slice<MessageSimpleDto> slice) {
        return new MessageListDto(slice.getNumberOfElements(), slice.hasNext(), slice.getContent());
    }
}
