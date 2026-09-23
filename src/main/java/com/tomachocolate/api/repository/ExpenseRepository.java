package com.tomachocolate.api.repository;

import com.tomachocolate.api.model.Expense;
import com.tomachocolate.api.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByPayer(Participant payer);

    List<Expense> findByConsumersContaining(Participant consumer);
}