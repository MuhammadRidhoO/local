package task.company.local.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import task.company.local.entity.CustomUserDetails;
import task.company.local.entity.user_entity;
import task.company.local.service.user_repository_auth;


@Component
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private user_repository_auth user_repository_auth;

  private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    logger.debug("Entering in Email Method...");
    user_entity user = user_repository_auth.findByEmail(email);
    if (user == null) {
      logger.error("Email not found: " + email);
      throw new UsernameNotFoundException("could not found email..!!");
    }
    logger.info("User Authenticated Successfully..!!!");
    return new CustomUserDetails(user);
  }
}