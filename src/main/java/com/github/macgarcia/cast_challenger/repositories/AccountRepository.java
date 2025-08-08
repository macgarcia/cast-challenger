package com.github.macgarcia.cast_challenger.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.macgarcia.cast_challenger.models.Account;

import jakarta.persistence.LockModeType;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	Account findByNumberAccountAndDocument(@Param("numberAccount") UUID numberAccount,
			@Param("document") String doument);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Account findByNumberAccount(@Param("numberAccount") UUID numberAccount);

}
