// package task.company.local.implement;

// import java.util.Collection;

// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;

// import task.company.local.entity.user_entity;

// public class CustomUserDetails extends user_entity implements UserDetails {

//   private String email;
//   private String password;
//   Collection<? extends GrantedAuthority> authorities;

//   public CustomUserDetails(user_entity userEmail) {
//     this.email = userEmail.getEmail();
//     this.password = userEmail.getPassword();
//   }

//   @Override
//   public Collection<? extends GrantedAuthority> getAuthorities() {
//     return authorities;
//   }

//   @Override
//   public String getPassword() {
//     return password;
//   }

//   @Override
//   public String getUsername() {
//     return email;
//   }

//   @Override
//   public boolean isAccountNonExpired() {
//     return true;
//   }

//   @Override
//   public boolean isAccountNonLocked() {
//     return true;
//   }

//   @Override
//   public boolean isCredentialsNonExpired() {
//     return true;
//   }

//   @Override
//   public boolean isEnabled() {
//     return true;
//   }
// }