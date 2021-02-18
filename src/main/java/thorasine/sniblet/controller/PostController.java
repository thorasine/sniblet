package thorasine.sniblet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thorasine.sniblet.exception.ResourceNotFoundException;
import thorasine.sniblet.model.Post;
import thorasine.sniblet.model.Tag;
import thorasine.sniblet.repository.PostRepository;
import thorasine.sniblet.repository.TagRepository;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/posts")
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @GetMapping(value = "/posts", params = "tags")
    public Page<Post> getPostsWithTags(Pageable pageable, @RequestParam(name = "tags") Set<String> tags) {
        return postRepository.findByTags(pageable, tags, tags.size());
    }

    @PostMapping("/posts")
    public Post createPost(@Valid @RequestBody Post post) {
        tagCheck(post);
        return postRepository.save(post);
    }

    @PutMapping("/posts/{postId}")
    public Post updatePost(@PathVariable Long postId, @Valid @RequestBody Post postRequest) {
        return postRepository.findById(postId).map(post -> {
            post.setTitle(postRequest.getTitle());
            post.setDescription(postRequest.getDescription());
            post.setContent(postRequest.getContent());
            tagCheck(postRequest);
            post.setTags(postRequest.getTags());
            return postRepository.save(post);
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        return postRepository.findById(postId).map(post -> {
            post.removeAllTags();
            postRepository.delete(post);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }

    private void tagCheck(Post post) {
        Set<Tag> initialTags = new HashSet<>(post.getTags());
        for (Tag tag : initialTags) {
            Tag dbTag = tagRepository.findFirstByName(tag.getName());
            if (dbTag != null) {
                post.removeTag(tag);
                post.addTag(dbTag);
            }
        }
    }

}