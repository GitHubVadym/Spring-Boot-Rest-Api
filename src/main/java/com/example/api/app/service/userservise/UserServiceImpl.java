package com.example.api.app.service.userservise;

import com.example.api.app.io.entity.UserEntity;
import com.example.api.app.io.repositories.UserRepository;
import com.example.api.app.shared.UserDTO;
import com.example.api.app.utils.Utils;
import lombok.AllArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.example.api.app.ui.model.response.ErrorMessages.NO_RECORD_FOUND;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("Record already exists");
        }

        userDTO.getAddresses()
                .forEach(addressDTO -> {
                    addressDTO.setUserDetails(userDTO);
                    addressDTO.setAddressId(utils.getAddressId(30));
                });

        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);

        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()))
                .setUserId(utils.getUserId(30));

        UserEntity storedUserDetails = userRepository.save(userEntity);
        UserDTO returnValue = modelMapper.map(storedUserDetails, UserDTO.class);

        return returnValue;
    }

    @Override
    public UserDTO getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        ModelMapper modelMapper = new ModelMapper();
        UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);

        return userDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserDTO getUserById(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new ServiceException(NO_RECORD_FOUND.getErrorMessage());
        }
        ModelMapper modelMapper = new ModelMapper();
        UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
        return userDTO;
    }

    @Override
    public UserDTO updateUser(String userId, UserDTO userDTO) {
        UserDTO user = new UserDTO();

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new ServiceException(userId);
        }

        userEntity.setFirstName(userDTO.getFirstName())
                .setLastName(userDTO.getLastName());

        UserEntity updatedUserEntity = userRepository.save(userEntity);
        BeanUtils.copyProperties(updatedUserEntity, user);
        return user;
    }

    @Override
    public void deleteUser(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new ServiceException(userId);
        }

        userRepository.delete(userEntity);
    }
}
