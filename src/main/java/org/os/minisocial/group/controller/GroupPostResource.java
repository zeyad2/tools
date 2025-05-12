// org.os.minisocial.group.controller/GroupPostResource.java
package org.os.minisocial.group.controller;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.os.minisocial.post.dto.PostDTO;
import org.os.minisocial.post.dto.PostResponseDTO;
import org.os.minisocial.post.service.PostService;
import java.util.List;

@Path("/groups/{groupId}/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupPostResource {
    @EJB
    private PostService postService;

    @POST
    public Response createGroupPost(
            @PathParam("groupId") Long groupId,
            PostDTO postDTO,
            @HeaderParam("X-User-Email") String userEmail
    ) {
        try {
            PostResponseDTO responseDTO = postService.createGroupPost(userEmail, groupId, postDTO);
            return Response.status(Response.Status.CREATED).entity(responseDTO).build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    public Response getGroupPosts(
            @PathParam("groupId") Long groupId,
            @HeaderParam("X-User-Email") String userEmail
    ) {
        try {
            List<PostResponseDTO> posts = postService.getGroupPosts(groupId, userEmail);
            return Response.ok(posts).build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}