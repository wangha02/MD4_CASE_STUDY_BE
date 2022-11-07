package rikkei.academy.service.user;

import com.sun.security.auth.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rikkei.academy.model.User;
import rikkei.academy.repository.IUserRepository;

import java.util.Optional;

@Service
public class UserServiceIMPL implements IUSerService{
    @Autowired
    private IUserRepository userRepository;
    @Override
    public Boolean existsByUsername(String user) {
        return userRepository.existsByUsername(user);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

}
