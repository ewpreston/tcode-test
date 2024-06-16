package integrations.turnitin.com.membersearcher.service;

import java.util.concurrent.CompletableFuture;

import integrations.turnitin.com.membersearcher.client.MembershipBackendClient;
import integrations.turnitin.com.membersearcher.model.MembershipList;

import integrations.turnitin.com.membersearcher.model.User;
import integrations.turnitin.com.membersearcher.model.UserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembershipService {
	@Autowired
	private MembershipBackendClient membershipBackendClient;

	/**
	 * Method to fetch all memberships with their associated user details included.
	 * This method calls out to the php-backend service and fetches all users,
	 * then all memberships.
	 * For each member it finds the corresponding user from the users list and sets the details.
	 *
	 * @return A CompletableFuture containing a fully populated MembershipList object.
	 */
	public CompletableFuture<MembershipList> fetchAllMembershipsWithUsers() {
        return membershipBackendClient.fetchUsers()
                .thenCompose(users -> membershipBackendClient.fetchMemberships()
                    .thenCompose(members -> {
                        CompletableFuture<?>[] userCalls = members.getMemberships().stream()
                                .map(member -> {
									User matchingUser = users.getUsers().stream().filter(u ->
											u.getId().equals(member.getUserId())).findAny().orElse(null);
									member.setUser(matchingUser);
									return CompletableFuture.completedFuture(member);
								})
                                .toArray(CompletableFuture<?>[]::new);
                        return CompletableFuture.allOf(userCalls)
                                .thenApply(nil -> members);
				}));
	}

	/**
	 * Method to fetch all users.
	 * This method calls out to the php-backend service and fetches all users.
	 *
	 * @return A CompletableFuture containing a fully populated UserList object.
	 */
	public CompletableFuture<UserList> fetchAllUsers() {
		return membershipBackendClient.fetchUsers();
	}
}
