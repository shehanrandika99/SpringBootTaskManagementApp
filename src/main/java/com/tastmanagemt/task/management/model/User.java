package com.tastmanagemt.task.management.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_uid")
    private Long userId;

    @NotBlank(message = "First name is required")
    @Column(name = "fname", length = 20)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(name = "lname", length = 20)
    private String lastName;

    @NotBlank(message = "Telephone Number is required")
    @Pattern(regexp = "^(07[0-9]{8})$", message = "Telephone number must be valid (e.g., 0712345678)")
    @Column(name = "tpno", length = 10,unique = true)
    private String telephoneNumber;


    @NotBlank(message = "Email  is required")
    @Email(message = "Invalid email format")
    @Column(name = "email", length = 50,unique = true)
    private String email;

   // @NotBlank(message = "First name is required")
    @Column(name ="Status")
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    @JsonBackReference
    private User assignedUser;
}