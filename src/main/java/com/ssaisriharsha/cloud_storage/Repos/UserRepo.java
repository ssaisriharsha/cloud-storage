package com.ssaisriharsha.cloud_storage.Repos;

import com.ssaisriharsha.cloud_storage.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

}
