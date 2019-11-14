package com.example.api.app.service.userservise;

import com.example.api.app.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(String id, UserDto userDto);

    void deleteUser(String id);

    UserDto getUser(String email);

    List<UserDto> getUsers(int page, int limit);

    UserDto getUserById(String userId);
}
