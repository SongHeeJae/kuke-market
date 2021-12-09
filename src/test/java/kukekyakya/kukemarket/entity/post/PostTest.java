package kukekyakya.kukemarket.entity.post;

import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static kukekyakya.kukemarket.factory.entity.CategoryFactory.createCategory;
import static kukekyakya.kukemarket.factory.entity.ImageFactory.createImageWithOriginName;
import static kukekyakya.kukemarket.factory.entity.MemberFactory.createMember;
import static kukekyakya.kukemarket.factory.entity.PostFactory.createPostWithImages;
import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    void updateImagesTest() {
        // given
        Image A = createImageWithOriginName("A.jpg");
        Image B = createImageWithOriginName("B.png");
        Image C = createImageWithOriginName("C.png");
        Post post = createPostWithImages(createMember(), createCategory(), List.of(A, B));

        // when
        Post.UpdatedImageResult updatedImageResult = post.updateImages(List.of(B, C));

        // then
        List<Image> addedImages = updatedImageResult.getAddedImages();
        List<String> addedOriginNames = addedImages.stream().map(i -> i.getOriginName()).collect(toList());
        assertThat(addedImages.size()).isEqualTo(1);
        assertThat(addedOriginNames).contains(C.getOriginName());

        List<Image> deletedImages = updatedImageResult.getDeletedImages();
        List<String> deletedOriginNames = deletedImages.stream().map(i -> i.getOriginName()).collect(toList());
        assertThat(deletedImages.size()).isEqualTo(1);
        assertThat(deletedOriginNames).contains(A.getOriginName());

        List<Image> resultImages = post.getImages();
        List<String> resultOriginNames = resultImages.stream().map(i -> i.getOriginName()).collect(toList());
        assertThat(resultImages.size()).isEqualTo(2);
        assertThat(resultOriginNames).contains(B.getOriginName(), C.getOriginName());
    }

}