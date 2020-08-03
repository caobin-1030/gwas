package com.mugu.gene.jpa;

import com.mugu.gene.model.UserRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunRepository extends JpaRepository<UserRun,Integer> {


    List<UserRun> findByEmailOrderByStartTimeDesc(String email);

    UserRun findByUuid(String uuid);

}
