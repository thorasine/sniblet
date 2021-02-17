package thorasine.sniblet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thorasine.sniblet.exception.ResourceNotFoundException;
import thorasine.sniblet.model.Post;
import thorasine.sniblet.repository.PostRepository;

import javax.validation.Valid;
import java.util.Set;

@RestController
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/posts")
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @GetMapping(value = "/posts", params = "tag")
    public Page<Post> getPostsWithTag(Pageable pageable, @RequestParam(name = "tag") String tag) {
        return postRepository.findByTags_Name(pageable, tag);
    }

    // tag1 OR tag2 OR tag3 ...
    @GetMapping(value = "/posts", params = "tags")
    public Page<Post> getPostsWithTags(Pageable pageable, @RequestParam(name = "tags") Set<String> tags) {
        return postRepository.findByTags_NameIn(pageable, tags);
    }

    @PostMapping("/posts")
    public Post createPost(@Valid @RequestBody Post post) {
        return postRepository.save(post);
    }

    @PutMapping("/posts/{postId}")
    public Post updatePost(@PathVariable Long postId, @Valid @RequestBody Post postRequest) {
        return postRepository.findById(postId).map(post -> {
            post.setTitle(postRequest.getTitle());
            post.setDescription(postRequest.getDescription());
            post.setContent(postRequest.getContent());
            return postRepository.save(post);
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        return postRepository.findById(postId).map(post -> {
            postRepository.delete(post);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }

}