// org.os.minisocial.group.controller/GroupResource.java
package org.os.minisocial.group.controller;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.os.minisocial.group.dto.CreateGroupDTO;
import org.os.minisocial.group.dto.GroupDTO;
import org.os.minisocial.group.dto.GroupMembershipRequestDTO;
import org.os.minisocial.group.service.GroupService;
import java.util.List;

@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupResource {
    @EJB
    private GroupService groupService;

    @POST
    public Response createGroup(CreateGroupDTO createGroupDTO, @HeaderParam("X-User-Email") String userEmail) {
        try {
            GroupDTO groupDTO = groupService.createGroup(userEmail, createGroupDTO);
            return Response.status(Response.Status.CREATED).entity(groupDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    public Response getAllGroups() {
        List<GroupDTO> groups = groupService.getAllGroups();
        return Response.ok(groups).build();
    }

    @GET
    @Path("/{id}")
    public Response getGroupById(@PathParam("id") Long id) {
        try {
            GroupDTO groupDTO = groupService.getGroupById(id);
            return Response.ok(groupDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteGroup(@PathParam("id") Long id, @HeaderParam("X-User-Email") String userEmail) {
        try {
            groupService.deleteGroup(id, userEmail);
            return Response.noContent().build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/{groupId}/join")
    public Response joinGroup(@PathParam("groupId") Long groupId, @HeaderParam("X-User-Email") String userEmail) {
        try {
            GroupMembershipRequestDTO requestDTO = groupService.joinGroup(groupId, userEmail);
            if (requestDTO != null) {
                return Response.status(Response.Status.CREATED).entity(requestDTO).build();
            }
            return Response.ok().entity("Successfully joined open group").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/{groupId}/leave")
    public Response leaveGroup(@PathParam("groupId") Long groupId, @HeaderParam("X-User-Email") String userEmail) {
        try {
            groupService.leaveGroup(groupId, userEmail);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{groupId}/requests")
    public Response getPendingRequests(
            @PathParam("groupId") Long groupId,
            @HeaderParam("X-User-Email") String adminEmail
    ) {
        try {
            List<GroupMembershipRequestDTO> requests = groupService.getPendingRequests(groupId, adminEmail);
            return Response.ok(requests).build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/requests/{requestId}")
    public Response processRequest(
            @PathParam("requestId") Long requestId,
            @QueryParam("approve") boolean approve,
            @HeaderParam("X-User-Email") String adminEmail
    ) {
        try {
            GroupMembershipRequestDTO requestDTO = groupService.processRequest(requestId, adminEmail, approve);
            return Response.ok(requestDTO).build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}