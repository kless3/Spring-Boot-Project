package com.harmyFounder.SpringBootProject.controllers;

import com.harmyFounder.SpringBootProject.models.Post;
import com.harmyFounder.SpringBootProject.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class MainController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('posts:read')")
    public String getAllPosts(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "all";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('posts:read')")
    public String getCertainPost(Model model, @PathVariable("id") long id) {
        Post post = postRepository.getById(id);
        model.addAttribute("post", post);
        return "certain";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('posts:write')")
    public String createPost(Model model, @RequestParam String title, String tag) {
        Post post = new Post(title, tag);
        postRepository.save(post);
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "all";
    }

    @PostMapping("/find")
    @PreAuthorize("hasAuthority('posts:read')")
    public String findByTag(Model model, @RequestParam("filter") String filter) {
        Iterable<Post> posts;
        if (filter != null && !filter.isEmpty()) {
            posts = postRepository.findByTag(filter);
        } else {
            posts = postRepository.findAll();
        }
        model.addAttribute("posts", posts);
        return "all";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('posts:write')")
    public String delete(@ModelAttribute("post") Post post, @PathVariable("id") long id) {
        postRepository.delete(post);
        return "redirect:/posts/all";
    }

}
