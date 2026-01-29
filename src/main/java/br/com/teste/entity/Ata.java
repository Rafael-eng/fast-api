package br.com.teste.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "ata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workshop_id", nullable = false)
    private Workshop workshop;

    @ManyToMany
    @JoinTable(
            name = "ata_colaborador",
            joinColumns = @JoinColumn(name = "ata_id"),
            inverseJoinColumns = @JoinColumn(name = "colaborador_id")
    )
    private List<Colaborador> colaboradores;
}
