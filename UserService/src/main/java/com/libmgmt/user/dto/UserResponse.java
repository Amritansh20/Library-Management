package com.libmgmt.user.dto;

import com.libmgmt.user.model.LibraryCard;
import com.libmgmt.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private UserRole role;
    private LibraryCard libraryCard;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
