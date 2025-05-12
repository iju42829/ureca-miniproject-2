package com.ureca.miniproject.friend.entity;

import java.io.Serializable;

import com.ureca.miniproject.user.entity.User;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@Embeddable
@Builder
@Getter@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class FriendId implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(nullable = false)	
	private User inviter;
	
	@ManyToOne
	@JoinColumn(nullable = false)	
	private User invitee;
}
