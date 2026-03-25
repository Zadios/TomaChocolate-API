package com.tomachocolate.api.repository;

import com.tomachocolate.api.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}