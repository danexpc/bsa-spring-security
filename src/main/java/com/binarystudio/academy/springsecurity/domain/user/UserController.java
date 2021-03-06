package com.binarystudio.academy.springsecurity.domain.user;

import com.binarystudio.academy.springsecurity.domain.user.dto.UserDto;
import com.binarystudio.academy.springsecurity.domain.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
	private final UserService userService;

	public UserController(UserService users) {
		this.userService = users;
	}

	@GetMapping("all")
	public List<UserDto> listAllUsers() {
		return userService.getAll();
	}

	@GetMapping("me")
	public UserDto whoAmI(@AuthenticationPrincipal User user) {
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		return UserDto.fromEntity(user);
	}
}
