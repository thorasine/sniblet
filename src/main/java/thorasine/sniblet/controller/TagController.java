package thorasine.sniblet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thorasine.sniblet.exception.ResourceNotFoundException;
import thorasine.sniblet.model.Post;
import thorasine.sniblet.model.Tag;
import thorasine.sniblet.repository.TagRepository;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
public class TagController {

    @Autowired
    TagRepository tagRepository;

    @GetMapping("/tags")
    public Page<Tag> getAllTags(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @PostMapping("/tags")
    public Tag createTag(@Valid @RequestBody Tag tag){
        return tagRepository.save(tag);
    }

    @PutMapping("/tags/{tagId}")
    public Tag updatePost(@PathVariable Long tagId, @Valid @RequestBody Tag tagRequest) {
        return tagRepository.findById(tagId).map(tag -> {
            tag.setName(tagRequest.getName());
            tag.setPosts(tagRequest.getPosts());
            return tagRepository.save(tag);
        }).orElseThrow(() -> new ResourceNotFoundException("TagId " + tagId + " not found"));
    }

    @DeleteMapping("/tags/{tagId}")
    public ResponseEntity<?> deleteTag(@PathVariable Long tagId) {
        return tagRepository.findById(tagId).map(tag -> {
            Set<Post> initial = new HashSet<>(tag.getPosts());
            initial.forEach(post -> post.removeTag(tag));
            tagRepository.delete(tag);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("TagId " + tagId + " not found"));
    }
}
