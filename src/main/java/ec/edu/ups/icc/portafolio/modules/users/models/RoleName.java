package ec.edu.ups.icc.portafolio.modules.users.models;

public enum RoleName {
    ROLE_ADMIN("Administrador del sistema"),
    ROLE_PROGRAMMER("Programador con portafolio"),
    ROLE_USER("Usuario externo que agenda asesorías");

    private final String description;

    RoleName(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}