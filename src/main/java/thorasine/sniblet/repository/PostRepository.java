package thorasine.sniblet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import thorasine.sniblet.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByTags_Name(Pageable pageable, String name);

    // tag1 OR tag2 OR tag3 ...
    //Page<Post> findByTags_NameIn(Set<String> name, Pageable pageable);

    // tag1 AND tag2 AND tag3 ...
    @Query(value = "SELECT p FROM Post p LEFT JOIN p.tags tag WHERE tag.name IN :tagset"
            + " GROUP BY p HAVING COUNT(tag) = :tagSetSize")
    Page<Post> findByTags(Pageable pageable, @Param("tagset") Set<String> tags, @Param("tagSetSize") long tagSetSize);
}