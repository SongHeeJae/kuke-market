package kukekyakya.kukemarket.dto.comment;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static kukekyakya.kukemarket.factory.dto.CommentCreateRequestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

class CommentCreateRequestValidationTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest() {
        // given
        CommentCreateRequest req = createCommentCreateRequestWithMemberId(null);

        // when
        Set<ConstraintViolation<CommentCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByEmptyContentTest() {
        // given
        String invalidValue = null;
        CommentCreateRequest req = createCommentCreateRequestWithContent(invalidValue);

        // when
        Set<ConstraintViolation<CommentCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByBlankContentTest() {
        // given
        String invalidValue = " ";
        CommentCreateRequest req = createCommentCreateRequestWithContent(invalidValue);

        // when
        Set<ConstraintViolation<CommentCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNegativeOrZeroPostIdTest() {
        // given
        Long invalidValue = 0L;
        CommentCreateRequest req = createCommentCreateRequestWithPostId(invalidValue);

        // when
        Set<ConstraintViolation<CommentCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNullPostIdTest() {
        // given
        Long invalidValue = null;
        CommentCreateRequest req = createCommentCreateRequestWithPostId(invalidValue);

        // when
        Set<ConstraintViolation<CommentCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNotNullMemberIdTest() {
        // given
        Long invalidValue = 0L;
        CommentCreateRequest req = createCommentCreateRequestWithMemberId(invalidValue);

        // when
        Set<ConstraintViolation<CommentCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }
}