package br.com.fiap.mercadoexpress.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TDS_TB_EMAIL_LOG")
@Data
@NoArgsConstructor
public class EmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_log_seq_generator")
    @SequenceGenerator(name = "email_log_seq_generator", sequenceName = "EMAIL_LOG_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PEDIDO_ID")
    private Long pedidoId;

    @Column(name = "DESTINATARIO")
    private String destinatario;

    @Column(name = "ASSUNTO")
    private String assunto;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "MENSAGEM_ERRO", length = 500)
    private String mensagemErro;

    @Column(name = "DATA_ENVIO")
    private LocalDateTime dataEnvio;

    @PrePersist
    void antesDeSalvar() {
        if (dataEnvio == null) {
            dataEnvio = LocalDateTime.now();
        }
    }
}