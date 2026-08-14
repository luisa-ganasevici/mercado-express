package br.com.fiap.mercadoexpress.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "TDS_TB_mercado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mercado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mercado_seq_generator")
    @SequenceGenerator(name = "mercado_seq_generator", sequenceName = "MERCADO_SEQ", allocationSize = 1)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Nome", nullable = false)
    private String nome;

    @Column(name = "Tipo")
    private String tipo;

    @Column(name = "Setor")
    private String setor;

    @Column(name = "Tamanho")
    private String tamanho;

    @Column(name = "Preco", precision = 10, scale = 2)
    private BigDecimal preco;
}