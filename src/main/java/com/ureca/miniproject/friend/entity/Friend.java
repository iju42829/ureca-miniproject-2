package com.ureca.miniproject.friend.entity;

import com.ureca.miniproject.common.BaseTimeEntity;
import com.ureca.miniproject.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Friend extends BaseTimeEntity{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(nullable = false)	
	private User inviter;
	
	@ManyToOne
	@JoinColumn(nullable = false)	
	private User invitee;
	
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;
	
}
