package br.com.fiap.mercadoexpress.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TDS_TB_PEDIDO")
@Data
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_seq_generator")
    @SequenceGenerator(name = "pedido_seq_generator", sequenceName = "PEDIDO_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MERCADO_ID", nullable = false)
    private Mercado mercado;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "QUANTIDADE", nullable = false)
    private Integer quantidade;

    @Column(name = "VALOR_TOTAL", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "CUSTO_TOTAL", precision = 12, scale = 2)
    private BigDecimal custoTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private StatusPedido status;

    @Column(name = "DATA_PEDIDO")
    private LocalDateTime dataPedido;

    @Column(name = "MOTIVO", length = 200)
    private String motivo;

    @PrePersist
    void antesDeSalvar() {
        if (dataPedido == null) {
            dataPedido = LocalDateTime.now();
        }
    }
}