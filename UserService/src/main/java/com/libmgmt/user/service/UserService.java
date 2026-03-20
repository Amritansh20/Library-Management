package com.libmgmt.user.service;

import com.libmgmt.user.dto.UserRequest;
import com.libmgmt.user.dto.UserResponse;
import com.libmgmt.user.exception.UserNotFoundException;
import com.libmgmt.user.model.LibraryCard;
import com.libmgmt.user.model.User;
import com.libmgmt.user.model.UserRole;
import com.libmgmt.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse createUser(UserRequest request) {
        LibraryCard card = LibraryCard.builder()
                .cardNumber("LIB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .issuedDate(LocalDate.now())
                .expiryDate(LocalDate.now().plusYears(1))
                .active(true)
                .build();

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .role(request.getRole() != null ? request.getRole() : UserRole.MEMBER)
                .libraryCard(card)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return toResponse(userRepository.save(user));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse getUserById(String id) {
        return toResponse(findUserOrThrow(id));
    }

    public UserResponse getUserByCardNumber(String cardNumber) {
        return toResponse(userRepository.findByLibraryCardCardNumber(cardNumber)
                .orElseThrow(() -> new UserNotFoundException("User not found with card number: " + cardNumber)));
    }

    public List<UserResponse> searchUsers(String name, String email, UserRole role) {
        if (email != null && !email.isBlank()) {
            return userRepository.findByEmail(email).map(u -> List.of(toResponse(u))).orElse(List.of());
        }
        if (name != null && !name.isBlank() && role != null) {
            return userRepository.findByNameContainingIgnoreCaseAndRole(name, role)
                    .stream().map(this::toResponse).toList();
        }
        if (name != null && !name.isBlank()) {
            return userRepository.findByNameContainingIgnoreCase(name)
                    .stream().map(this::toResponse).toList();
        }
        if (role != null) {
            return userRepository.findByRole(role).stream().map(this::toResponse).toList();
        }
        return getAllUsers();
    }

    public UserResponse updateUser(String id, UserRequest request) {
        User user = findUserOrThrow(id);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        user.setUpdatedAt(LocalDateTime.now());
        return toResponse(userRepository.save(user));
    }

    public void deleteUser(String id) {
        findUserOrThrow(id);
        userRepository.deleteById(id);
    }

    public UserResponse activateUser(String id) {
        User user = findUserOrThrow(id);
        user.setActive(true);
        user.getLibraryCard().setActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        return toResponse(userRepository.save(user));
    }

    public UserResponse deactivateUser(String id) {
        User user = findUserOrThrow(id);
        user.setActive(false);
        user.getLibraryCard().setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        return toResponse(userRepository.save(user));
    }

    private User findUserOrThrow(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(user.getRole())
                .libraryCard(user.getLibraryCard())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
