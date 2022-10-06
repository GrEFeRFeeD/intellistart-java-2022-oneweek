package com.intellias.intellistart.interviewplanning.model.user;

import org.springframework.data.repository.CrudRepository;

/**
 * DAO for User entity.
 */
public interface UserRepository extends CrudRepository<User, Long> {

}
