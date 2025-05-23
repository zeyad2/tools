package org.os.minisocial.user.controller;

import org.os.minisocial.shared.dto.ProfileDTO;
import org.os.minisocial.user.entity.User;
import org.os.minisocial.user.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/profile")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProfileResource {
    @Inject
    private UserService userService;

    @GET
    @Path("/{email}")
    public Response getUserProfile(@PathParam("email") String email) {
        try {
            var user = userService.getUserByEmail(email);
            if (user.isPresent()) {
                return Response
                        .ok(user.get())
                        .build();
            }
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("User not found")
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Invalid request")
                    .build();
        }
    }

    @PUT
    @Path("/{email}")
    public Response updateUserProfile(
            @PathParam("email") String email,
            ProfileDTO profileDTO) {
        try {
            var updatedUser = userService.updateProfile(email, profileDTO);
            if (updatedUser.isPresent()) {
                return Response
                        .ok(updatedUser.get())
                        .build();
            }
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("User not found")
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Invalid profile data")
                    .build();
        }
    }

    @GET
    @Path("/search")
    public Response searchUsers(@QueryParam("q") String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("Search query cannot be empty")
                        .build();
            }

            List<User> users = userService.searchUsers(query);
            return Response
                    .ok(users)
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error searching users")
                    .build();
        }
    }
}