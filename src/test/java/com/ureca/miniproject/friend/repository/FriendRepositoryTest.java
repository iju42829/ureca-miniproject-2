package com.ureca.miniproject.friend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ureca.miniproject.friend.entity.Friend;
import com.ureca.miniproject.friend.entity.FriendId;
import com.ureca.miniproject.friend.entity.Status;
import com.ureca.miniproject.user.entity.Role;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.repository.UserRepository;

@DataJpaTest
class FriendRepositoryTest {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository; // 임시로 정의한 테스트용 유저 저장소

    private User createUser(String name, String email) {
        return userRepository.save(User.builder()
                .userName(name)
                .email(email)
                .password("1234")
                .role(Role.USER)
                .isOnline(true)
                .build());
    }

    private Friend createFriend(User inviter, User invitee, Status status) {
        FriendId friendId = new FriendId(inviter, invitee);
        return friendRepository.save(Friend.builder()
                .friendId(friendId)
                .status(status)
                .build());
    }

    @Test
    @DisplayName("친구 상태로 존재하는지 확인 - existsByFriendIdAndStatus")
    void testExistsByFriendIdAndStatus() {
        User a = createUser("UserA", "a@test.com");
        User b = createUser("UserB", "b@test.com");
        Friend friend = createFriend(a, b, Status.ACCEPTED);

        boolean exists = friendRepository.existsByFriendIdAndStatus(friend.getFriendId(), Status.ACCEPTED);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("초대받은 사람이 특정 상태로 존재하는지 확인 - existsByFriendIdInviteeIdAndStatus")
    void testExistsByFriendIdInviteeIdAndStatus() {
        User inviter = createUser("UserA", "a@test.com");
        User invitee = createUser("UserB", "b@test.com");
        createFriend(inviter, invitee, Status.WAITING);

        boolean exists = friendRepository.existsByFriendIdInviteeIdAndStatus(invitee.getId(), Status.WAITING);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("친구 요청자 ID와 상태로 존재하는지 확인 - existsByFriendIdInviterIdAndStatus")
    void testExistsByFriendIdInviterIdAndStatus() {
        User inviter = createUser("UserA", "a@test.com");
        User invitee = createUser("UserB", "b@test.com");
        createFriend(inviter, invitee, Status.DECLINED);

        boolean exists = friendRepository.existsByFriendIdInviterIdAndStatus(inviter.getId(), Status.DECLINED);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("자신이 받은 요청만 조회 - findByUserIdAndStatus")
    void testFindByUserIdAndStatus() {
        User inviter = createUser("UserA", "a@test.com");
        User invitee = createUser("UserB", "b@test.com");
        createFriend(inviter, invitee, Status.WAITING);

        List<Friend> friends = friendRepository.findByUserIdAndStatus(invitee.getId(), Status.WAITING);
        assertThat(friends).hasSize(1);
        assertThat(friends.get(0).getFriendId().getInviter().getId()).isEqualTo(inviter.getId());
    }

    @Test
    @DisplayName("자신이 보낸/받은 요청 모두 조회 - findFriendsByUserIdAndStatus")
    void testFindFriendsByUserIdAndStatus() {
        User a = createUser("UserA", "a@test.com");
        User b = createUser("UserB", "b@test.com");
        User c = createUser("UserC", "c@test.com");
        
        createFriend(a, b, Status.ACCEPTED);
        createFriend(c, a, Status.ACCEPTED);

        List<Friend> friends = friendRepository.findFriendsByUserIdAndStatus(a.getId(), Status.ACCEPTED);
        assertThat(friends).hasSize(2);
    }

    @Test
    @DisplayName("이메일로 친구 관계 확인 - findFriendsByEmail")
    void testFindFriendsByEmail() {
        User a = createUser("UserA", "a@test.com");
        User b = createUser("UserB", "b@test.com");
        createFriend(a, b, Status.ACCEPTED);

        List<Friend> result = friendRepository.findFriendsByEmail("a@test.com", "b@test.com");
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("이메일 + 상태로 친구 관계 확인 - findInvitesRelatedToMe")
    void testFindInvitesRelatedToMe() {
        User a = createUser("UserA", "a@test.com");
        User b = createUser("UserB", "b@test.com");
        createFriend(a, b, Status.WAITING);

        List<Friend> result = friendRepository.findInvitesRelatedToMe("a@test.com", "b@test.com", Status.WAITING);
        assertThat(result).hasSize(1);
    }
}
