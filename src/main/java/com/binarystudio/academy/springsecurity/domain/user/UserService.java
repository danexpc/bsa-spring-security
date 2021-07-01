package com.binarystudio.academy.springsecurity.domain.user;

import com.binarystudio.academy.springsecurity.domain.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not found"));
	}

	public List<User> getAll() {
		return userRepository.findUsers();
	}

	public User createUser(String login, String email, String password) {
		return userRepository.createUser(login, email, password);
	}

	public User updatePassword(User user, String newPassword) {
		return userRepository.updatePassword(user, newPassword).orElseThrow(() -> new NoSuchElementException("Not found"));
	}
}
