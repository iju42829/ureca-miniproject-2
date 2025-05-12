package com.ureca.miniproject.friend.entity;

import com.ureca.miniproject.common.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	private Long id;
	
	@EmbeddedId	
	private FriendId friendId;
	
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;
	
}
