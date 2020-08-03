package com.mugu.gene.jpa;

import com.mugu.gene.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<User,Integer> {


    User findByEmail(String email);
}
