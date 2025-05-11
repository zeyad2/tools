package org.os.minisocial.user.controller;

import org.os.minisocial.shared.dto.SignInRequest;
import org.os.minisocial.shared.dto.SignupRequest;
import org.os.minisocial.user.entity.User;
import org.os.minisocial.user.service.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    @Inject
    private AuthService authService;

    @POST
    @Path("/signup")
    public Response registerUser(SignupRequest signupRequest) {
        try {
            var user = authService.register(signupRequest);
            if (user.isPresent()) {
                return Response
                        .status(Response.Status.CREATED)
                        .entity(user.get())
                        .build();
            }
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity("Email already exists")
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Invalid registration data")
                    .build();
        }
    }

    @POST
    @Path("/signin")
    public Response authenticateUser(SignInRequest signInRequest) {
        try {
            var user = authService.authenticate(signInRequest);
            if (user.isPresent()) {
                return Response
                        .ok(user.get())
                        .build();
            }
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid credentials")
                    .build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Invalid login data")
                    .build();
        }
    }
}