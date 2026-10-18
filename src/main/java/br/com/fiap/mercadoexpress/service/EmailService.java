package br.com.fiap.mercadoexpress.service;

import br.com.fiap.mercadoexpress.entity.EmailLog;
import br.com.fiap.mercadoexpress.entity.Pedido;
import br.com.fiap.mercadoexpress.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailRepository emailLogRepository;

    @Value("${spring.mail.username:}")
    private String remetente;

    public void enviarConfirmacaoPagamento(Pedido pedido) {
        String assunto = "Pagamento confirmado - Pedido " + pedido.getId();

        EmailLog log = new EmailLog();
        log.setPedidoId(pedido.getId());
        log.setDestinatario(pedido.getEmail());
        log.setAssunto(assunto);

        if (pedido.getEmail() == null || pedido.getEmail().isBlank()) {
            log.setStatus("FALHA");
            log.setMensagemErro("Cliente sem e-mail cadastrado");
            emailLogRepository.save(log);
            return;
        }

        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            if (!remetente.isBlank()) {
                msg.setFrom(remetente);
            }
            msg.setTo(pedido.getEmail());
            msg.setSubject(assunto);
            msg.setText("Ola, " + pedido.getUsername() + "!\n\n"
                    + "O Banco Tranquilo confirmou o pagamento do seu pedido.\n\n"
                    + "Pedido: " + pedido.getId() + "\n"
                    + "Produto: " + pedido.getMercado().getNome() + "\n"
                    + "Quantidade: " + pedido.getQuantidade() + "\n"
                    + "Total: R$ " + pedido.getValorTotal() + "\n\n"
                    + "Obrigado por comprar no Mercado Express!");
            mailSender.send(msg);
            log.setStatus("ENVIADO");
            System.out.println("==== E-mail enviado para " + pedido.getEmail());
        } catch (Exception e) {
            String erro = String.valueOf(e.getMessage());
            log.setStatus("FALHA");
            log.setMensagemErro(erro.length() > 500 ? erro.substring(0, 500) : erro);
            System.out.println("==== Falha ao enviar e-mail: " + erro);
        }

        emailLogRepository.save(log);
    }
}