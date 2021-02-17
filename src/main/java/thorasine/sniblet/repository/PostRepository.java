package thorasine.sniblet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import thorasine.sniblet.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByTags_Name(Pageable pageable, String name);

    Page<Post> findByTags_NameIn(Pageable pageable, Set<String> name);
}