package uz.urinov.roleandpermission.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.urinov.roleandpermission.entity.Role;
import uz.urinov.roleandpermission.entity.User;
import uz.urinov.roleandpermission.entity.enums.Permission;
import uz.urinov.roleandpermission.repository.RoleRepository;
import uz.urinov.roleandpermission.repository.UserRepository;
import uz.urinov.roleandpermission.utils.AppConstants;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static uz.urinov.roleandpermission.entity.enums.Permission.*;


@Component
public class DataLoader implements CommandLineRunner {

    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public synchronized void run(String... args) {
        if (initialMode.equalsIgnoreCase("always")) {
            loadRole();
            loadUser();
        }
    }

    public void loadRole() {
        roleRepository.saveAll(List.of(
                new Role(AppConstants.ADMIN, Set.of(Permission.values())),
                new Role(AppConstants.USER, Set.of(ADD_COMMENT, EDIT_COMMENT, DELETE_MY_COMMENT))
        ));
    }

    public void loadUser() {
        Optional<Role> optionalAdmin = roleRepository.findByName(AppConstants.ADMIN);
        Optional<Role> optionalUser = roleRepository.findByName(AppConstants.USER);
        if (optionalAdmin.isPresent() && optionalUser.isPresent()) {
            userRepository.saveAll(List.of(
                    new User("John Doe", "admin", passwordEncoder.encode("root123@"), optionalAdmin.get(), true),
                    new User("Abror Abrorov", "user1", passwordEncoder.encode("root123@"), optionalUser.get(), true),
                    new User("Abdusattor Abdusattorov", "user2", passwordEncoder.encode("root123@"), optionalUser.get(), true)
            ));
        } else {
            System.out.println("User saqlanishida hatolik yuz berdi. Admin ma'lumotlar omboriga saqlanmadi");
        }
    }
}
