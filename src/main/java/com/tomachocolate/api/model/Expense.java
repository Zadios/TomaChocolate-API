package com.tomachocolate.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "expenses")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String description;
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    @JsonIgnore
    private Participant payer;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    @JsonIgnore
    private Meeting meeting;

    @JsonProperty("payerName")
    public String getPayerName() {
        return payer != null ? payer.getName() : "Desconocido";
    }

    @JsonProperty("payerId")
    public Long getPayerId() {
        return payer != null ? payer.getId() : null;
    }
}