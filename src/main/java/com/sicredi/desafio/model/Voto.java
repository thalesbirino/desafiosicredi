package com.sicredi.desafio.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "voto")
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pauta_id")
    private Pauta pauta;

    @ManyToOne
    @JoinColumn(name = "associado_id")
    private Associado associado;
    private boolean voto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Voto voto = (Voto) o;
        return id != null && Objects.equals(id, voto.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
