package kukekyakya.kukemarket.dto.message;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static kukekyakya.kukemarket.factory.dto.MessageReadConditionFactory.createMessageReadCondition;
import static org.assertj.core.api.Assertions.assertThat;

class MessageReadConditionValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        // given
        MessageReadCondition cond = createMessageReadCondition(null, 1L, 1);

        // when
        Set<ConstraintViolation<MessageReadCondition>> validate = validator.validate(cond);

        // then
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByNotNullMemberIdTest() {
        // given
        Long invalidValue = 1L;
        MessageReadCondition cond = createMessageReadCondition(invalidValue, 1L, 1);

        // when
        Set<ConstraintViolation<MessageReadCondition>> validate = validator.validate(cond);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNullSizeTest() {
        // given
        Integer invalidValue = null;
        MessageReadCondition cond = createMessageReadCondition(null, 1L, invalidValue);

        // when
        Set<ConstraintViolation<MessageReadCondition>> validate = validator.validate(cond);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNegativeOrZeroSizeTest() {
        // given
        Integer invalidValue = 0;
        MessageReadCondition cond = createMessageReadCondition(null, 1L, invalidValue);

        // when
        Set<ConstraintViolation<MessageReadCondition>> validate = validator.validate(cond);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }
}