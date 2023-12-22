package com.erismaroliveira.todonote.services;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erismaroliveira.todonote.models.User;
import com.erismaroliveira.todonote.models.enums.ProfileEnum;
import com.erismaroliveira.todonote.repositories.UserRepository;
import com.erismaroliveira.todonote.security.UserSpringSecurity;
import com.erismaroliveira.todonote.services.exceptions.AuthorizationException;
import com.erismaroliveira.todonote.services.exceptions.DataBindingViolationException;
import com.erismaroliveira.todonote.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  
  @Autowired
  private UserRepository userRepository;

  public User findById(Long id) {
    UserSpringSecurity userSpringSecurity = authenticated();
    if (!Objects.nonNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId()))
      throw new AuthorizationException("Access denied");
    Optional<User> user = this.userRepository.findById(id);
    return user.orElseThrow(() -> new ObjectNotFoundException(
      "User not found! " + "ID: " + id + ", Type: " + User.class.getName()));
  }

  @Transactional
  public User create(User user) {
    user.setId(null);
    user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
    user.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
    user = this.userRepository.save(user);
    return user;
  }

  @Transactional
  public User update(User user) {
    User newUser = this.findById(user.getId());
    newUser.setPassword(user.getPassword());
    newUser.setPassword(this.bCryptPasswordEncoder.encode(newUser.getPassword()));
    return this.userRepository.save(newUser);
  }

  public void delete(Long id) {
    this.findById(id);
    try {
      this.userRepository.deleteById(id);
    } catch (Exception e) {
      throw new DataBindingViolationException("User has tasks, it can't be deleted");
    }
  }

  public static UserSpringSecurity authenticated() {
    try {
      return (UserSpringSecurity) SecurityContextHolder
          .getContext().getAuthentication().getPrincipal();
    } catch (Exception e) {
      return null;
    }
  }
}
