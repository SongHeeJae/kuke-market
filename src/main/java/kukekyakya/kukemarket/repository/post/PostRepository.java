package kukekyakya.kukemarket.repository.post;

import kukekyakya.kukemarket.dto.post.PostSimpleDto;
import kukekyakya.kukemarket.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p join fetch p.member where p.id = :id")
    Optional<Post> findByIdWithMember(Long id);

    @Query("select new kukekyakya.kukemarket.dto.post.PostSimpleDto(p.id, p.title, m.nickname, p.createdAt) " +
                    "from Post p join p.member m order by p.id desc")
    Page<PostSimpleDto> findAllWithMemberOrderByIdDesc(Pageable pageable);
}
