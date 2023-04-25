package uz.urinov.roleandpermission.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.urinov.roleandpermission.entity.Role;
import uz.urinov.roleandpermission.entity.User;
import uz.urinov.roleandpermission.payload.UserDTO;
import uz.urinov.roleandpermission.repository.RoleRepository;
import uz.urinov.roleandpermission.repository.UserRepository;

import java.util.Optional;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> addUser(UserDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername()))
            return status(UNPROCESSABLE_ENTITY).body("Username already exists");
        Optional<Role> optionalRole = roleRepository.findById(dto.getRoleId());
        if (optionalRole.isEmpty()) return status(NOT_FOUND).body("Role not found");
        User user = new User(
                dto.getFullName(),
                dto.getUsername(),
                passwordEncoder.encode(dto.getPassword()),
                optionalRole.get(),
                true
        );
        return status(CREATED).body("User created");
    }
}
