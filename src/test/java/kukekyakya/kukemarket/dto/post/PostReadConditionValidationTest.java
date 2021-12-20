package kukekyakya.kukemarket.dto.post;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static kukekyakya.kukemarket.factory.dto.PostReadConditionFactory.createPostReadCondition;
import static org.assertj.core.api.Assertions.assertThat;

class PostReadConditionValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        // given
        PostReadCondition cond = createPostReadCondition(1, 1);

        // when
        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(cond);

        // then
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByNullPageTest() {
        // given
        Integer invalidValue = null;
        PostReadCondition req = createPostReadCondition(invalidValue, 1);

        // when
        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNegativePageTest() {
        // given
        Integer invalidValue = -1;
        PostReadCondition req = createPostReadCondition(invalidValue, 1);

        // when
        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNullSizeTest() {
        // given
        Integer invalidValue = null;
        PostReadCondition req = createPostReadCondition(1, invalidValue);

        // when
        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNegativeOrZeroPageTest() {
        // given
        Integer invalidValue = 0;
        PostReadCondition req = createPostReadCondition(1, invalidValue);

        // when
        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }
}