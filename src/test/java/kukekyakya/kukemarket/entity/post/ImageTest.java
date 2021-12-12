package kukekyakya.kukemarket.entity.post;

import kukekyakya.kukemarket.exception.UnsupportedImageFormatException;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static kukekyakya.kukemarket.factory.entity.ImageFactory.*;
import static kukekyakya.kukemarket.factory.entity.PostFactory.*;
import static org.assertj.core.api.Assertions.*;

class ImageTest {

    @Test
    void createImageTest() {
        // given
        String validExtension = "JPEG";

        // when, then
        createImageWithOriginName("image." + validExtension);
    }

    @Test
    void createImageExceptionByUnsupportedFormatTest() {
        // given
        String invalidExtension = "invalid";

        // when, then
        assertThatThrownBy(() -> createImageWithOriginName("image." + invalidExtension))
                .isInstanceOf(UnsupportedImageFormatException.class);
    }

    @Test
    void createImageExceptionByNoneExtensionTest() {
        // given
        String originName = "image";

        // when, then
        assertThatThrownBy(() -> createImageWithOriginName(originName))
                .isInstanceOf(UnsupportedImageFormatException.class);
    }

    @Test
    void initPostTest() {
        // given
        Image image = createImage();

        // when
        Post post = createPost();
        image.initPost(post);

        // then
        assertThat(image.getPost()).isSameAs(post);
    }

    @Test
    void initPostNotChangedTest() {
        // given
        Image image = createImage();
        image.initPost(createPost());

        // when
        Post post = createPost();
        image.initPost(post);

        // then
        assertThat(image.getPost()).isNotSameAs(post);
    }

    @Test
    void isEqualsTrueTest() {
        // given
        Image image1 = createImageWithOriginName("image1.jpg");
        Image image2 = createImageWithOriginName("image2.jpg");
        ReflectionTestUtils.setField(image2, "uniqueName", image1.getUniqueName());

        // when
        boolean result = image1.isEquals(image2);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void isEqualsFalseTest() {
        // given
        Image image1 = createImageWithOriginName("image1.jpg");
        Image image2 = createImageWithOriginName("image1.jpg");

        // when
        boolean result = image1.isEquals(image2);

        // then
        assertThat(result).isFalse();
    }
}