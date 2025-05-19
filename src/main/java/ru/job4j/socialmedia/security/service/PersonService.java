package ru.job4j.socialmedia.security.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.socialmedia.security.dtos.request.SignupRequestDTO;
import ru.job4j.socialmedia.security.dtos.response.RegisterDTO;
import ru.job4j.socialmedia.security.model.ERole;
import ru.job4j.socialmedia.security.model.Person;
import ru.job4j.socialmedia.security.model.Role;
import ru.job4j.socialmedia.security.repository.PersonRepository;
import ru.job4j.socialmedia.security.repository.RoleRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class PersonService {
    private PasswordEncoder encoder;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    public RegisterDTO signUp(SignupRequestDTO signupRequest) {
        Person person = new Person(signupRequest.getUsername(), signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));

        Set<String> stringRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();
        Supplier<RuntimeException> supplier = () -> new RuntimeException("Error: Role is not found.");

        if (stringRoles == null) {
            roles.add(roleRepository.findByName(ERole.ROLE_USER).orElseThrow(supplier));
        } else {
            stringRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> roles.add(roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(supplier));
                    case "mod" -> roles.add(roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(supplier));
                    default -> roles.add(roleRepository.findByName(ERole.ROLE_USER).orElseThrow(supplier));
                }
            });
        }

        person.setRoles(roles);
        personRepository.save(person);
        return new RegisterDTO(HttpStatus.OK, "Person registered successfully!");
    }
}
