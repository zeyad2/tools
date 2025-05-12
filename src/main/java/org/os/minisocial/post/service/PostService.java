package org.os.minisocial.post.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.os.minisocial.friend.service.FriendService;
import org.os.minisocial.group.entity.Group;
import org.os.minisocial.post.dto.CommentDTO;
import org.os.minisocial.post.dto.CommentResponseDTO;
import org.os.minisocial.post.dto.PostDTO;
import org.os.minisocial.post.dto.PostResponseDTO;
import org.os.minisocial.post.entity.Comment;
import org.os.minisocial.post.entity.Like;
import org.os.minisocial.post.entity.Post;
import org.os.minisocial.post.repository.CommentRepository;
import org.os.minisocial.post.repository.LikeRepository;
import org.os.minisocial.post.repository.PostRepository;
import org.os.minisocial.shared.dto.UserDTO;
import org.os.minisocial.user.entity.User;
import org.os.minisocial.user.service.UserService;

import org.os.minisocial.group.repository.GroupRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class PostService {
    @EJB
    private PostRepository postRepository;

    @EJB
    private UserService userService;

    @EJB
    private FriendService friendService;

    @EJB
    private GroupRepository groupRepository;


    public PostResponseDTO createPost(String userEmail, PostDTO postDTO) {
        User author = userService.getUserByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );

        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setImageUrls(postDTO.getImageUrls());
        post.setLinks(postDTO.getLinks());
        post.setAuthor(author);

        post = postRepository.save(post);
        return convertToDTO(post);
    }

    public List<PostResponseDTO> getUserFeed(String userEmail) {
        User user = userService.getUserByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );

        List<Post> posts = postRepository.findPostsByUserAndFriends(user);
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PostResponseDTO updatePost(Long postId, String userEmail, PostDTO postDTO) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found");
        }

        if (!post.getAuthor().getEmail().equals(userEmail)) {
            throw new SecurityException("You can only update your own posts");
        }

        post.setContent(postDTO.getContent());
        post.setImageUrls(postDTO.getImageUrls());
        post.setLinks(postDTO.getLinks());

        post = postRepository.update(post);
        return convertToDTO(post);
    }

    public void deletePost(Long postId, String userEmail) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found");
        }

        if (!post.getAuthor().getEmail().equals(userEmail)) {
            throw new SecurityException("You can only delete your own posts");
        }

        postRepository.delete(postId);
    }

    public PostResponseDTO likePost(Long postId) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found");
        }

        post.setLikeCount(post.getLikeCount() + 1);
        post = postRepository.update(post);
        return convertToDTO(post);
    }

    private PostResponseDTO convertToDTO(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setImageUrls(post.getImageUrls() != null ? new ArrayList<>(post.getImageUrls()) : new ArrayList<>());
        dto.setLinks(post.getLinks() != null ? new ArrayList<>(post.getLinks()) : new ArrayList<>());

        List<Comment> comments = commentRepository.findByPost(post.getId());
        List<CommentResponseDTO> commentDTOs = comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        dto.setComments(commentDTOs);


        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setLikeCount(post.getLikeCount());

        UserDTO authorDTO = new UserDTO();
        authorDTO.setEmail(post.getAuthor().getEmail());
        authorDTO.setName(post.getAuthor().getName());
        authorDTO.setBio(post.getAuthor().getBio());
        dto.setAuthor(authorDTO);

        return dto;
    }

    // Add these methods to PostService.java

    @EJB
    private CommentRepository commentRepository;

    @EJB
    private LikeRepository likeRepository;

    public CommentResponseDTO addComment(Long postId, String userEmail, CommentDTO commentDTO) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found");
        }

        User author = userService.getUserByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );

        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setAuthor(author);
        comment.setPost(post);

        comment = commentRepository.save(comment);
        return convertToDTO(comment);
    }

    public List<CommentResponseDTO> getCommentsForPost(Long postId) {
        List<Comment> comments = commentRepository.findByPost(postId);
        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PostResponseDTO toggleLike(Long postId, String userEmail) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found");
        }

        User user = userService.getUserByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );

        Like existingLike = likeRepository.findByUserAndPost(user, post);
        if (existingLike != null) {
            // Unlike
            likeRepository.delete(existingLike.getId());
            post.setLikeCount(post.getLikeCount() - 1);
        } else {
            // Like
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);
            post.setLikeCount(post.getLikeCount() + 1);
        }

        post = postRepository.update(post);
        return convertToDTO(post);
    }

    private CommentResponseDTO convertToDTO(Comment comment) {
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());

        UserDTO authorDTO = new UserDTO();
        authorDTO.setEmail(comment.getAuthor().getEmail());
        authorDTO.setName(comment.getAuthor().getName());
        authorDTO.setBio(comment.getAuthor().getBio());
        dto.setAuthor(authorDTO);

        return dto;
    }
    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // org.os.minisocial.post.service/PostService.java (add these methods)
    public PostResponseDTO createGroupPost(String userEmail, Long groupId, PostDTO postDTO) {
        User author = userService.getUserByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );

        Group group = groupRepository.findById(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found");
        }

        if (!group.getMembers().contains(author)) {
            throw new SecurityException("Only group members can post in the group");
        }

        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setImageUrls(postDTO.getImageUrls());
        post.setLinks(postDTO.getLinks());
        post.setAuthor(author);
        post.setGroup(group);

        post = postRepository.save(post);
        return convertToDTO(post);
    }

    public List<PostResponseDTO> getGroupPosts(Long groupId, String userEmail) {
        User user = userService.getUserByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );

        Group group = groupRepository.findById(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found");
        }

        if (!group.getMembers().contains(user)) {
            throw new SecurityException("Only group members can view group posts");
        }

        List<Post> posts = postRepository.findByGroup(groupId);
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}