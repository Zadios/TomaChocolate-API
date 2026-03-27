package com.tomachocolate.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "participants")
@Getter @Setter
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    @JsonIgnore
    private Meeting meeting;
}