package thorasine.sniblet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thorasine.sniblet.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

}
