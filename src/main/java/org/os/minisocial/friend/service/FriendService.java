package org.os.minisocial.friend.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import org.os.minisocial.friend.dto.FriendRequestDTO;
import org.os.minisocial.friend.dto.FriendshipDTO;
import org.os.minisocial.friend.entity.Friendship;
import org.os.minisocial.friend.repository.FriendRepository;
import org.os.minisocial.shared.dto.ProfileDTO;
import org.os.minisocial.user.entity.User;
import org.os.minisocial.user.service.UserService;
import java.util.ArrayList;
import java.util.List;

@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class FriendService {
    @EJB
    private FriendRepository friendRepository;

    @EJB
    private UserService userService;

    public String sendFriendRequest(FriendRequestDTO requestDTO) {
        if (requestDTO.getRequesterEmail() == null || requestDTO.getAddresseeEmail() == null) {
            return "Emails cannot be null";
        }
        if (requestDTO.getRequesterEmail().equals(requestDTO.getAddresseeEmail())) {
            return "Cannot send friend request to yourself";
        }
        User requester = userService.getUserByEmail(requestDTO.getRequesterEmail()).orElse(null);
        User addressee = userService.getUserByEmail(requestDTO.getAddresseeEmail()).orElse(null);

        if (requester == null || addressee == null) {
            return "User not found";
        }

        if (friendRepository.findByUsers(requester, addressee) != null) {
            return "Friendship already exists";
        }

        Friendship friendship = new Friendship();
        friendship.setRequester(requester);
        friendship.setAddressee(addressee);
        friendship.setStatus(Friendship.Status.PENDING);

        friendRepository.save(friendship);
        return "Friend request sent successfully";
    }

    public String respondToRequest(FriendRequestDTO requestDTO, boolean accept) {
        User requester = userService.getUserByEmail(requestDTO.getRequesterEmail()).orElse(null);
        User addressee = userService.getUserByEmail(requestDTO.getAddresseeEmail()).orElse(null);

        if (requester == null || addressee == null) {
            return "User not found";
        }

        Friendship friendship = friendRepository.findByUsers(requester, addressee);
        if (friendship == null || friendship.getStatus() != Friendship.Status.PENDING) {
            return "No pending request found";
        }

        friendship.setStatus(accept ? Friendship.Status.ACCEPTED : Friendship.Status.REJECTED);
        friendRepository.update(friendship);
        return accept ? "Friend request accepted" : "Friend request rejected";
    }

    public List<FriendshipDTO> getFriendships(String email, Friendship.Status status) {
        User user = userService.getUserByEmail(email).orElse(null);
        if (user == null) return new ArrayList<>();

        List<Friendship> friendships = friendRepository.findByUserAndStatus(user, status);
        List<FriendshipDTO> result = new ArrayList<>();

        for (Friendship f : friendships) {
            FriendshipDTO dto = new FriendshipDTO();
            User friendUser = f.getRequester().equals(user) ? f.getAddressee() : f.getRequester();

            ProfileDTO profile = new ProfileDTO();
            profile.setName(friendUser.getName());
            profile.setEmail(friendUser.getEmail());
            profile.setBio(friendUser.getBio());

            dto.setFriend(profile);
            dto.setStatus(f.getStatus().name());
            dto.setSince(f.getCreatedAt());

            result.add(dto);
        }
        return result;
    }
}