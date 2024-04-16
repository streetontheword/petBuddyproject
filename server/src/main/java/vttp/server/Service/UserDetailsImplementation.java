package vttp.server.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vttp.server.Model.User;
import vttp.server.Model.UserPrincipal;
import vttp.server.Repository.UserRepository;

@Service
public class UserDetailsImplementation implements UserDetailsService {

     @Autowired 
    UserRepository userRepo;

    public User getUserByEmail(String email){
        return userRepo.getPasswordByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("%s does not exist".formatted(email));
        }
        return new UserPrincipal(user);
    } 

    
    
}
