package com.tomachocolate.api.repository;

import com.tomachocolate.api.model.Expense;
import com.tomachocolate.api.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    void deleteByPayer(Participant payer);
}