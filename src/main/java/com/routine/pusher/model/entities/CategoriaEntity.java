package com.routine.pusher.model.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Entity
@Table(name = "categoria")
public class CategoriaEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cor", unique = true, nullable = false)
    private String cor;

    @Column(name = "fatorOrdem", unique = true, nullable = false)
    private int fatorOrdem;

    @OneToMany(mappedBy = "categoria")
    private List<LembreteEntity> lembrete;
}