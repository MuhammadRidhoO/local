// package task.company.local.service;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import task.company.local.entity.user_entity;
// import task.company.local.repositories.user_repository;

// @Service
// public class user_service {
//       @Autowired
//   private user_repository user_repository;

//   public List<user_entity> getAllUser() {
//     List<user_entity> Users = user_repository.findAll();
//     return Users;
//   }

//   public user_entity findByEmail(String email) {
//     return user_repository.findByEmail(email);
//   }

//   public user_entity RegisterUser(register_request dto) throws UserAlreadyExistException {
//     user_entity existingRegister = user_repository.findByEmail(dto.getEmail());

//     if (existingRegister != null) {
//       throw new UserAlreadyExistException("An account for that email already exists.");
//     } else {
//       user_entity newRegister = new user();
//       newRegister.setEmail(dto.getEmail());
//       newRegister.setPassword(Bcrypt.hashpw(dto.getPassword(), Bcrypt.gensalt()));
//       newRegister.setFullName(dto.getFullName());

//       user_repository.save(newRegister);

//       return newRegister;
//     }
//   }

//   // public user LoginUser(login_request dto) throws UserAlreadyExistException {
//   //   return null;
//   // }

// }
