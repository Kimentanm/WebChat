package com.hsd.security;

import com.hsd.model.Role;
import com.hsd.model.User;
import com.hsd.service.RoleService;
import com.hsd.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String loginName) {
        log.debug("Authenticating {}", loginName);
        String lowercaseLogin = loginName.toLowerCase();
        User user = userService.findBy("loginName",loginName);
        if(user != null){
            List<Role> roleList = roleService.findRolesByUserId(user.getId());
            List<GrantedAuthority> grantedAuthorities = roleList.stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getCode()))
                    .collect(Collectors.toList());
            AuthenticatedUser authenticatedUser =
                    new AuthenticatedUser(lowercaseLogin, user.getPassword(), grantedAuthorities);
            authenticatedUser.setUserId(user.getId());
            return authenticatedUser;
        }else{
            throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " +
                    "database");
        }
    }
}
