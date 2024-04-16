package vttp.server.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

    private User user; 

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        List<SimpleGrantedAuthority> role = new ArrayList<>();
        role.add(new SimpleGrantedAuthority(user.getRole().toString()));
        return role;
        
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }


    public User getUser() {
        return user;
    }

    @Override
    public boolean isAccountNonExpired() {
        return user!= null; 
    }

    @Override
    public boolean isAccountNonLocked() {
        return user!= null; 
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user!= null; 
    }

    @Override
    public boolean isEnabled() {
        return user!= null; 
    }

    public UserPrincipal() {
    }

    public UserPrincipal(User user) {
        this.user = user;
    }
    
    
}
