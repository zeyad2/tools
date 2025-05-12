package org.os.minisocial.post.controller;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.os.minisocial.post.dto.CommentDTO;
import org.os.minisocial.post.dto.CommentResponseDTO;
import org.os.minisocial.post.dto.PostDTO;
import org.os.minisocial.post.dto.PostResponseDTO;
import org.os.minisocial.post.service.PostService;
import java.util.List;

@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {
    @EJB
    private PostService postService;

    @POST
    public Response createPost(PostDTO postDTO, @HeaderParam("X-User-Email") String userEmail) {
        try {
            PostResponseDTO response = postService.createPost(userEmail, postDTO);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/feed")
    public Response getUserFeed(@HeaderParam("X-User-Email") String userEmail) {
        try {
            List<PostResponseDTO> feed = postService.getUserFeed(userEmail);
            return Response.ok(feed).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updatePost(@PathParam("id") Long postId, PostDTO postDTO,
                               @HeaderParam("X-User-Email") String userEmail) {
        try {
            PostResponseDTO response = postService.updatePost(postId, userEmail, postDTO);
            return Response.ok(response).build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletePost(@PathParam("id") Long postId,
                               @HeaderParam("X-User-Email") String userEmail) {
        try {
            postService.deletePost(postId, userEmail);
            return Response.noContent().build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/{id}/like")
    public Response likePost(@PathParam("id") Long postId) {
        try {
            PostResponseDTO response = postService.likePost(postId);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    public Response getAllPosts() {
        try {
            List<PostResponseDTO> posts = postService.getAllPosts();
            return Response.ok(posts).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
    @POST
    @Path("/{id}/comments")
    public Response addComment(@PathParam("id") Long postId, CommentDTO commentDTO,
                               @HeaderParam("X-User-Email") String userEmail) {
        try {
            CommentResponseDTO response = postService.addComment(postId, userEmail, commentDTO);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}/comments")
    public Response getComments(@PathParam("id") Long postId) {
        try {
            List<CommentResponseDTO> comments = postService.getCommentsForPost(postId);
            return Response.ok(comments).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/{id}/toggle-like")
    public Response toggleLike(@PathParam("id") Long postId,
                               @HeaderParam("X-User-Email") String userEmail) {
        try {
            PostResponseDTO response = postService.toggleLike(postId, userEmail);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}