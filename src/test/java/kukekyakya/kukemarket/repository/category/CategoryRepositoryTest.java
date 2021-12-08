package kukekyakya.kukemarket.repository.category;

import kukekyakya.kukemarket.entity.category.Category;
import kukekyakya.kukemarket.exception.CategoryNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static java.util.stream.Collectors.*;
import static kukekyakya.kukemarket.factory.entity.CategoryFactory.createCategory;
import static kukekyakya.kukemarket.factory.entity.CategoryFactory.createCategoryWithName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class CategoryRepositoryTest {
    @Autowired CategoryRepository categoryRepository;
    @PersistenceContext EntityManager em;

    @Test
    void createAndReadTest() {
        // given
        Category category = createCategory();

        // when
        Category savedCategory = categoryRepository.save(category);
        clear();

        // then
        Category foundCategory = categoryRepository.findById(savedCategory.getId()).orElseThrow(CategoryNotFoundException::new);
        assertThat(foundCategory.getId()).isEqualTo(savedCategory.getId());
    }

    @Test
    void readAllTest() {
        // given
        List<Category> categories = List.of("name1", "name2", "name3").stream().map(n -> createCategoryWithName(n)).collect(toList());
        categoryRepository.saveAll(categories);
        clear();

        // when
        List<Category> foundCategories = categoryRepository.findAll();

        // then
        assertThat(foundCategories.size()).isEqualTo(3);
    }

    @Test
    void deleteTest() {
        // given
        Category category = categoryRepository.save(createCategory());
        clear();

        // when
        categoryRepository.delete(category);
        clear();

        // then
        assertThatThrownBy(() -> categoryRepository.findById(category.getId()).orElseThrow(CategoryNotFoundException::new))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    void deleteCascadeTest() {
        // given
        Category category1 = categoryRepository.save(createCategoryWithName("category1"));
        Category category2 = categoryRepository.save(createCategory("category2", category1));
        Category category3 = categoryRepository.save(createCategory("category3", category2));
        Category category4 = categoryRepository.save(createCategoryWithName("category4"));
        clear();

        // when
        categoryRepository.deleteById(category1.getId());
        clear();

        // then
        List<Category> result = categoryRepository.findAll();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(category4.getId());
    }

    @Test
    void deleteNoneValueTest() {
        // given
        Long noneValueId = 100L;

        // when, then
        assertThatThrownBy(() -> categoryRepository.deleteById(noneValueId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void findAllWithParentOrderByParentIdAscNullsFirstCategoryIdAscTest() {
        // given
        // 1		NULL
        // 2		1
        // 3		1
        // 4		2
        // 5		2
        // 6		4
        // 7		3
        // 8		NULL
        Category c1 = categoryRepository.save(createCategory("category1", null));
        Category c2 = categoryRepository.save(createCategory("category2", c1));
        Category c3 = categoryRepository.save(createCategory("category3", c1));
        Category c4 = categoryRepository.save(createCategory("category4", c2));
        Category c5 = categoryRepository.save(createCategory("category5", c2));
        Category c6 = categoryRepository.save(createCategory("category6", c4));
        Category c7 = categoryRepository.save(createCategory("category7", c3));
        Category c8 = categoryRepository.save(createCategory("category8", null));
        clear();

        // when
        List<Category> result = categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc();

        // then
        // 1	NULL
        // 8	NULL
        // 2	1
        // 3	1
        // 4	2
        // 5	2
        // 7	3
        // 6	4
        assertThat(result.size()).isEqualTo(8);
        assertThat(result.get(0).getId()).isEqualTo(c1.getId());
        assertThat(result.get(1).getId()).isEqualTo(c8.getId());
        assertThat(result.get(2).getId()).isEqualTo(c2.getId());
        assertThat(result.get(3).getId()).isEqualTo(c3.getId());
        assertThat(result.get(4).getId()).isEqualTo(c4.getId());
        assertThat(result.get(5).getId()).isEqualTo(c5.getId());
        assertThat(result.get(6).getId()).isEqualTo(c7.getId());
        assertThat(result.get(7).getId()).isEqualTo(c6.getId());

    }

    void clear() {
        em.flush();
        em.clear();
    }
}