package ec.edu.ups.icc.portafolio.modules.users.dtos;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserUpdateDto {
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String name;

    @Email(message = "El email debe ser válido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    private String email;

    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @Size(max = 500, message = "La biografía no puede exceder 500 caracteres")
    private String bio;

    private String profilePicture;
    private String githubUrl;
    private String linkedinUrl;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String phone;

    private List<String> roles;

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}