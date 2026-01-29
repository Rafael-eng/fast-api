package br.com.teste.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "colaborador")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;
}
