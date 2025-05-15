package com.ureca.miniproject.user.service.response;

import java.util.List;

import com.ureca.miniproject.game.entity.GameParticipant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
public class InvitesUserResponse {
	List<Long> invites;
}
