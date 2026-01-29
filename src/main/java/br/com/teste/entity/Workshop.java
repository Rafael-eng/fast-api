package br.com.teste.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "workshop")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workshop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "data_realizacao", nullable = false)
    private LocalDateTime dataRealizacao;

    @Column(name = "descricao")
    private String descricao;
}
