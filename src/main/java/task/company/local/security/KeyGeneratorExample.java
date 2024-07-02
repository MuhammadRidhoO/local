// package task.company.local.security;
// import java.security.Key;
// import java.util.Base64;

// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.security.Keys;

// public class KeyGeneratorExample {
//     public static void main(String[] args) {
//         Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//         String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
//         System.out.println("Base64 Encoded Key: " + base64Key);
//     }
// }