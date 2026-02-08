package ec.edu.ups.icc.portafolio.modules.users.services;

import ec.edu.ups.icc.portafolio.modules.users.dtos.UserRequestDto;
import ec.edu.ups.icc.portafolio.modules.users.dtos.UserResponseDto;
import ec.edu.ups.icc.portafolio.modules.users.dtos.UserUpdateDto;
import ec.edu.ups.icc.portafolio.modules.users.models.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDto toDto(UserEntity user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setBio(user.getBio());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setGithubUrl(user.getGithubUrl());
        dto.setLinkedinUrl(user.getLinkedinUrl());
        dto.setPhone(user.getPhone());
        dto.setRoles(user.getRoles().stream()
                .map(role -> role.getName().name())
                .toList());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    public UserEntity toEntity(UserRequestDto dto) {
        UserEntity user = new UserEntity();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setBio(dto.getBio());
        user.setProfilePicture(dto.getProfilePicture());
        user.setGithubUrl(dto.getGithubUrl());
        user.setLinkedinUrl(dto.getLinkedinUrl());
        user.setPhone(dto.getPhone());
        return user;
    }

    public void updateEntity(UserUpdateDto dto, UserEntity user) {
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getBio() != null) {
            user.setBio(dto.getBio());
        }
        if (dto.getProfilePicture() != null) {
            user.setProfilePicture(dto.getProfilePicture());
        }
        if (dto.getGithubUrl() != null) {
            user.setGithubUrl(dto.getGithubUrl());
        }
        if (dto.getLinkedinUrl() != null) {
            user.setLinkedinUrl(dto.getLinkedinUrl());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
    }

    public void partialUpdate(UserUpdateDto dto, UserEntity user) {
        updateEntity(dto, user);
    }
}