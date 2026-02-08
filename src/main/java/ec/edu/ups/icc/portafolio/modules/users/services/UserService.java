package ec.edu.ups.icc.portafolio.modules.users.services;

import ec.edu.ups.icc.portafolio.modules.users.dtos.UserRequestDto;
import ec.edu.ups.icc.portafolio.modules.users.dtos.UserResponseDto;
import ec.edu.ups.icc.portafolio.modules.users.dtos.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<UserResponseDto> findAll(Pageable pageable);

    UserResponseDto findById(Long id);

    UserResponseDto create(UserRequestDto userDto);

    UserResponseDto update(Long id, UserUpdateDto userDto);

    UserResponseDto partialUpdate(Long id, UserUpdateDto userDto);

    void delete(Long id);

    List<UserResponseDto> findProgrammers();

    Page<UserResponseDto> search(String name, String email, String role, Pageable pageable);
}