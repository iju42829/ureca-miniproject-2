package com.ureca.miniproject.user.entity;

import com.ureca.miniproject.common.BaseTimeEntity;
import com.ureca.miniproject.user.dto.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long Id;
	private String userName;
	private String password;
	private String email;
	private Role role;	
	private Boolean isOnline;
}

