package kukekyakya.kukemarket.repository.message;

import kukekyakya.kukemarket.entity.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
