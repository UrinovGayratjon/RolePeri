package uz.urinov.roleandpermission.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import uz.urinov.roleandpermission.entity.enums.Permission;

import java.util.Set;

@Data
public class RoleDTO {
    @NotBlank
    private String name;

    private String description;

    @NotEmpty
    private Set<Permission> permissions;
}
