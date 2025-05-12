package org.os.minisocial;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.os.minisocial.friend.controller.FriendResource;
import org.os.minisocial.group.controller.GroupPostResource;
import org.os.minisocial.group.controller.GroupResource;
import org.os.minisocial.post.controller.PostResource;
import org.os.minisocial.user.controller.AuthResource;
import org.os.minisocial.user.controller.ProfileResource;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class HelloApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(AuthResource.class);
        resources.add(ProfileResource.class);
        resources.add(FriendResource.class);
        resources.add(GroupResource.class);
        resources.add(GroupPostResource.class);
        resources.add(GroupResource.class);
        resources.add(PostResource.class);


        return resources;
    }
}

