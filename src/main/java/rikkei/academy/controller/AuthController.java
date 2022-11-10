package rikkei.academy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rikkei.academy.dto.request.ChangeAvatar;
import rikkei.academy.dto.request.ChangePasswordForm;
import rikkei.academy.dto.request.SignIn;
import rikkei.academy.dto.request.SignUpForm;
import rikkei.academy.dto.response.JwtResponse;
import rikkei.academy.dto.response.ResponseMessage;
import rikkei.academy.model.Role;
import rikkei.academy.model.RoleName;
import rikkei.academy.model.User;
import rikkei.academy.security.jwt.JwtAuthTokenFilter;
import rikkei.academy.security.jwt.JwtProvider;
import rikkei.academy.security.userprincipal.UserPrinciple;
import rikkei.academy.service.role.IRoleService;
import rikkei.academy.service.user.IUSerService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    IRoleService roleService;
    @Autowired
     IUSerService uSerService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    JwtAuthTokenFilter jwtAuthTokenFilter;
    @Autowired
     AuthenticationManager authenticationManager;
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpForm signUpForm){
        if (uSerService.existsByUsername(signUpForm.getUsername())){
            return new ResponseEntity<>(new ResponseMessage("username_existed"), HttpStatus.OK);
        }
        if (uSerService.existsByEmail(signUpForm.getEmail())){
            return new ResponseEntity<>(new ResponseMessage("email_existed"),HttpStatus.OK);
        }
//        Set<String> strRoles = signUpForm.getRoles();
        Set<Role>roles = new HashSet<>();
        Role roleSignUp = roleService.findByName(RoleName.ADMIN).orElseThrow(()-> new RuntimeException("not_found"));
        roles.add(roleSignUp);

//        strRoles.forEach(role->{
//            switch (role.toLowerCase()){
//                case "admin":
//                    Role adminRole = roleService.findByName(RoleName.ADMIN).orElseThrow(()->new RuntimeException("not_found"));
//                    roles.add(adminRole);
//                    break;
//                case "pm":
//                    Role pmRole = roleService.findByName(RoleName.PM).orElseThrow(()->new RuntimeException("not_found"));
//                    roles.add(pmRole);
//                    break;
//                default:
//                    Role userRole = roleService.findByName(RoleName.USER).orElseThrow(()->new RuntimeException("not_found"));
//                    roles.add(userRole);
//
//            }
//        });
        signUpForm.setAvatar("https://firebasestorage.googleapis.com/v0/b/longdaica1xbet.appspot.com/o/anh1.jpg?alt=media&token=cb1d12b3-e235-4ab4-9c6a-49c2d255cff6");
        User user = new User(signUpForm.getName(),signUpForm.getUsername(),signUpForm.getEmail(),signUpForm.getAvatar(),passwordEncoder.encode(signUpForm.getPassword()),roles);
        uSerService.save(user);
        return new ResponseEntity<>(new ResponseMessage("create_success"),HttpStatus.OK);
    }
    @PostMapping("/signin")
    public ResponseEntity<?>signIn(@Valid @RequestBody SignIn signIn){


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signIn.getUsername(),signIn.getPassword())
        );
        System.out.println("check auth"+authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateJwtToken(authentication);
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(token,userPrinciple.getName(),userPrinciple.getAuthorities()));
    }
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(HttpServletRequest request, @Valid @RequestBody ChangePasswordForm changePasswordForm){
        String jwt = jwtAuthTokenFilter.getJwt(request);
        String username = jwtProvider.getUserNameFromJwtToken(jwt);
        User user;
        try {
            user = uSerService.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User Not Found with -> username"+username));
            boolean matches = passwordEncoder.matches(changePasswordForm.getCurrentPassword(), user.getPassword());
            if(changePasswordForm.getNewPassword() != null){
                if(matches){
                    user.setPassword(passwordEncoder.encode(changePasswordForm.getNewPassword()));
                    uSerService.save(user);
                } else {
                    return new ResponseEntity<>(new ResponseMessage("Can't change password"), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(new ResponseMessage("Changed password"), HttpStatus.OK);
        } catch (UsernameNotFoundException exception){
            return new ResponseEntity<>(new ResponseMessage(exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/change_avatar")
    public ResponseEntity<?>changeAvatar(HttpServletRequest httpServletRequest, @Valid @RequestBody ChangeAvatar changeAvatar){
        String jwt = jwtAuthTokenFilter.getJwt(httpServletRequest);
        String username = jwtProvider.getUserNameFromJwtToken(jwt);
        User user;
        try{
            if (changeAvatar.getAvatar()==null){
                return new ResponseEntity<>(new ResponseMessage("no"),HttpStatus.OK);
            }
            else {
                user = uSerService.findByUsername(username).get();
                user.setAvatar(changeAvatar.getAvatar());
                uSerService.save(user);
            }
            return new ResponseEntity<>(new ResponseMessage("yes"),HttpStatus.OK);

        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()),HttpStatus.NOT_FOUND);
        }
    }

}