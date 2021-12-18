package kukekyakya.kukemarket.dto.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kukekyakya.kukemarket.entity.category.Category;
import kukekyakya.kukemarket.exception.CategoryNotFoundException;
import kukekyakya.kukemarket.repository.category.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Optional;

@ApiModel(value = "카테고리 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateRequest {

    @ApiModelProperty(value = "카테고리 명", notes = "카테고리 명을 입력해주세요", required = true, example = "my category")
    @NotBlank(message = "카테고리 명을 입력해주세요.")
    @Size(min = 2, max = 30, message = "카테고리 명의 길이는 2글자에서 30글자 입니다.")
    private String name;

    @ApiModelProperty(value = "부모 카테고리 아이디", notes = "부모 카테고리 아이디를 입력해주세요", example = "7")
    private Long parentId;

    public static Category toEntity(CategoryCreateRequest req, CategoryRepository categoryRepository) {
        return new Category(req.getName(),
                Optional.ofNullable(req.getParentId())
                        .map(id -> categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new))
                        .orElse(null));
    }
}
