package kukekyakya.kukemarket.entity.post;

import kukekyakya.kukemarket.entity.category.Category;
import kukekyakya.kukemarket.entity.common.EntityDate;
import kukekyakya.kukemarket.entity.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends EntityDate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(nullable = false)
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Image> images;

    public Post(String title, String content, Long price, Member member, Category category, List<Image> images) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.member = member;
        this.category = category;
        this.images = new ArrayList<>();
        addImages(images);
    }

    public UpdatedImageResult updateImages(List<Image> updatedImages) {
        UpdatedImageResult result = findUpdatedImages(updatedImages);
        addImages(result.getAddedImages());
        deleteImages(result.getDeletedImages());
        return result;
    }

    private void addImages(List<Image> added) {
        added.stream().forEach(i -> {
            images.add(i);
            i.initPost(this);
        });
    }

    private void deleteImages(List<Image> deleted) {
        deleted.stream().forEach(di -> this.images.remove(di));
    }

    private UpdatedImageResult findUpdatedImages(List<Image> updatedImages) {
        return new UpdatedImageResult(
                findAddedImages(updatedImages),
                findDeletedImages(updatedImages)
        );
    }

    private List<Image> findAddedImages(List<Image> updatedImages) {
        return findDifference(updatedImages, this.images);
    }

    private List<Image> findDeletedImages(List<Image> updatedImages) {
        return findDifference(this.images, updatedImages);
    }

    private List<Image> findDifference(List<Image> a, List<Image> b) {
        return a.stream()
                .filter(i -> !containsImage(b, i))
                .collect(toList());
    }

    private boolean containsImage(List<Image> list, Image image) {
        return list.stream().anyMatch(i -> i.isEquals(image));
    }

    @Getter
    @AllArgsConstructor
    public static class UpdatedImageResult {
        private List<Image> addedImages;
        private List<Image> deletedImages;
    }
}
