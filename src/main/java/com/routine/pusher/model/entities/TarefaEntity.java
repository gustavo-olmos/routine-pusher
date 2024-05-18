package com.routine.pusher.model.entities;

import com.routine.pusher.model.enums.StatusConclusao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "tarefa")
public class TarefaEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private LembreteEntity lembrete;

    @OneToOne(mappedBy = "tarefa")
    private SubtarefaEntity subTarefa;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "comentario")
    private String comentario;

    @Column(name = "status")
    private StatusConclusao status;
}
