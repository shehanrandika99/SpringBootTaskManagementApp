package com.tastmanagemt.task.management.service;

import com.tastmanagemt.task.management.dao.UserDao;
import com.tastmanagemt.task.management.dto.ApiResponse;
import com.tastmanagemt.task.management.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
//CODE START DEV
    // CREATE A NEW USER ------------------------------------------------------------------------------START
    public ResponseEntity<ApiResponse<User>> saveUser(User user) {
        if (userDao.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        if (userDao.existsByTelephoneNumber(user.getTelephoneNumber())) {
            throw new RuntimeException("Telephone number already exists: " + user.getTelephoneNumber());
        }

        user.setStatus(true);
        User savedUser = userDao.save(user);
        if (savedUser.getUserId() == null) {
            throw new IllegalStateException("User not saved properly to the database");
        }
        return ResponseEntity.ok(new ApiResponse<>("User saved successfully", savedUser));
    }
    // CREATE A NEW USER ------------------------------------------------------------------------------END


    //GET ALL USERS --------------------------------------------------------------------------------START
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userDao.findAll());
    }
    //GET ALL USERS --------------------------------------------------------------------------------END


    //GET USERS BY USER ID --------------------------------------------------------------------------------START
    public ResponseEntity<User> getUserById(Long useruid) {
        return ResponseEntity.ok(userDao.findById(useruid)
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + useruid)));
    }
    //GET ALL USERS BY USER ID --------------------------------------------------------------------------------END


    //UPDATE EXISTING USER --------------------------------------------------------------------------------START
    public ResponseEntity<ApiResponse<User>> updateUser(Long useruid, User user) {
        User existingUser = userDao.findById(useruid)
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + useruid));


        if (!existingUser.getEmail().equals(user.getEmail()) &&
                userDao.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already in use: " + user.getEmail());
        }


        if (!existingUser.getTelephoneNumber().equals(user.getTelephoneNumber()) &&
                userDao.existsByTelephoneNumber(user.getTelephoneNumber())) {
            throw new RuntimeException("Telephone number already in use: " + user.getTelephoneNumber());
        }

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setTelephoneNumber(user.getTelephoneNumber());
        existingUser.setEmail(user.getEmail());

        User updatedUser = userDao.save(existingUser);
        return ResponseEntity.ok(new ApiResponse<>("User updated successfully", updatedUser));
    }

    //UPDATE EXISTING USER --------------------------------------------------------------------------------END

    //PERMENENTLY DELETE USER --------------------------------------------------------------------------------START
    public ResponseEntity<ApiResponse<String>> deleteUser(Long useruid) {
        User user = userDao.findById(useruid)
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + useruid));

        userDao.deleteById(useruid);
        return ResponseEntity.ok(new ApiResponse<>("User deleted successfully", "Deleted user ID: " + useruid));
    }
    //PERMENENTLY DELETE USER --------------------------------------------------------------------------------END



    //SEARCH USERS BY FIRST NAME WITH SORTING----------------------------------------------------------------START
    public ResponseEntity<List<User>> searchUsersByFirstName(String firstName) {
        List<User> users = userDao.findByFirstNameContainingIgnoreCaseOrderByFirstNameAsc(firstName);
        if (users.isEmpty()) {
            throw new RuntimeException("No users found with first name: " + firstName);
        }
        return ResponseEntity.ok(users);
    }
    //SEARCH USERS BY FIRST NAME WITH SORTING-------------------------------------------------------------------END

//    // DEACTIVATE AND ACTIVATE USER ----------------------------------------------------------------------START
    public ResponseEntity<ApiResponse<User>> deactivate(Long useruid) {
        User user = userDao.findById(useruid)
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + useruid));

        if (!user.isStatus()) throw new RuntimeException("User is already deactivated.");

        user.setStatus(false);
        return ResponseEntity.ok(new ApiResponse<>("User deactivated successfully", userDao.save(user)));
    }
    // DEACTIVATE AND ACTIVATE USER ----------------------------------------------------------------------END

// ACTIVATE USER --------------------------------------------------------------------------------START
    public ResponseEntity<ApiResponse<User>> activate(Long useruid) {
        User user = userDao.findById(useruid)
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + useruid));

        if (user.isStatus()) throw new RuntimeException("User is already active.");

        user.setStatus(true);
        return ResponseEntity.ok(new ApiResponse<>("User activated successfully", userDao.save(user)));
    }

// ACTIVATE USER --------------------------------------------------------------------------------END

// GET ACTIVE USERS --------------------------------------------------------------------------------START
    public ResponseEntity<List<User>> getActiveUsers() {
        List<User> activeUsers = userDao.findAll().stream().filter(User::isStatus).toList();
        if (activeUsers.isEmpty()) throw new RuntimeException("No active users found.");
        return ResponseEntity.ok(activeUsers);
    }

    public ResponseEntity<Page<User>> getUsersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("userId").descending());
        Page<User> usersPage = userDao.findAll(pageable);
        return ResponseEntity.ok(usersPage);
    }
    // GET ACTIVE USERS --------------------------------------------------------------------------------END
}
