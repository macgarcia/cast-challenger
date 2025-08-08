package com.github.macgarcia.cast_challenger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.macgarcia.cast_challenger.models.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

}
