package com.example.api.app.service.userservise;

import com.example.api.app.shared.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(String id, UserDTO userDTO);

    void deleteUser(String id);

    UserDTO getUser(String email);

    UserDTO getUserById(String userId);
}
