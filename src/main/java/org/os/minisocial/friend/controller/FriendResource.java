package org.os.minisocial.friend.controller;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.os.minisocial.friend.dto.FriendRequestDTO;
import org.os.minisocial.friend.dto.FriendshipDTO;
import org.os.minisocial.friend.entity.Friendship;
import org.os.minisocial.friend.service.FriendService;
import org.os.minisocial.shared.dto.ErrorResponse;
import java.util.List;

@Path("/friends")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FriendResource {
    @EJB
    private FriendService friendService;

    @POST
    @Path("/request")
    public Response sendRequest(FriendRequestDTO requestDTO) {
        try {
            String result = friendService.sendFriendRequest(requestDTO);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/respond")
    public Response respondToRequest(
            @QueryParam("accept") boolean accept,
            FriendRequestDTO requestDTO) {
        try {
            String result = friendService.respondToRequest(requestDTO, accept);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{email}")
    public Response getFriends(@PathParam("email") String email) {
        try {
            List<FriendshipDTO> friends = friendService.getFriendships(email, Friendship.Status.ACCEPTED);
            return Response.ok(friends).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/pending/{email}")
    public Response getPendingRequests(@PathParam("email") String email) {
        try {
            List<FriendshipDTO> pending = friendService.getFriendships(email, Friendship.Status.PENDING);
            return Response.ok(pending).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }
}