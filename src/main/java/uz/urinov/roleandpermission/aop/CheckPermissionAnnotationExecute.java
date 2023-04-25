package uz.urinov.roleandpermission.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.urinov.roleandpermission.entity.User;
import uz.urinov.roleandpermission.exception.ForbiddenException;

@Component
@Aspect
public class CheckPermissionAnnotationExecute {

    @Before(value = "@annotation(checkPermission)")
    public void checkPermission(CheckPermission checkPermission) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean exists = false;
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.getAuthority().equals(checkPermission.value())) {
                exists = true;
                break;
            }
        }
        if (!exists) throw new ForbiddenException(checkPermission.value(), "You don't have permission");
    }
}
