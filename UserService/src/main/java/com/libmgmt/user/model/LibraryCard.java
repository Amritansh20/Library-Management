package com.libmgmt.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LibraryCard {
    private String cardNumber;
    private LocalDate issuedDate;
    private LocalDate expiryDate;
    private boolean active;
}
