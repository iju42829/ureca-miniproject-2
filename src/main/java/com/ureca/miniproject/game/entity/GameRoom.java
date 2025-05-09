package com.ureca.miniproject.game.entity;

import com.ureca.miniproject.common.BaseTimeEntity;
import com.ureca.miniproject.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User hostUser;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;

    @Column(nullable = false)
    private Integer maxPlayer;

    @Builder
    private GameRoom(User hostUser, String title, RoomStatus roomStatus, Integer maxPlayer) {
        this.hostUser = hostUser;
        this.title = title;
        this.roomStatus = roomStatus;
        this.maxPlayer = maxPlayer;
    }
}
