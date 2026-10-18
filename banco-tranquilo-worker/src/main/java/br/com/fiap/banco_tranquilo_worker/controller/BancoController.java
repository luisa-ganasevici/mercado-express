package br.com.fiap.banco_tranquilo_worker.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/banco")
public class BancoController {

    private final JdbcTemplate jdbc;

    public BancoController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping
    public String tela(@RequestParam(required = false) String email, Model model) {
        if (email != null && !email.isBlank()) {
            String e = email.trim();
            model.addAttribute("email", e);
            List<Map<String, Object>> conta = jdbc.queryForList(
                    "SELECT TITULAR, SALDO FROM TDS_TB_CONTA_BANCO WHERE LOWER(EMAIL) = LOWER(?)", e);
            if (conta.isEmpty()) {
                model.addAttribute("erro", "Conta nao encontrada. Adicione saldo para criar.");
            } else {
                model.addAttribute("titular", conta.get(0).get("TITULAR"));
                model.addAttribute("saldo", conta.get(0).get("SALDO"));
            }
        }
        return "banco";
    }

    @PostMapping("/adicionar")
    public String adicionar(@RequestParam String email,
                            @RequestParam BigDecimal valor,
                            RedirectAttributes ra) {
        String e = email.trim();
        int linhas = jdbc.update(
                "UPDATE TDS_TB_CONTA_BANCO SET SALDO = SALDO + ? WHERE LOWER(EMAIL) = LOWER(?)", valor, e);
        if (linhas == 0) {
            jdbc.update("INSERT INTO TDS_TB_CONTA_BANCO (EMAIL, TITULAR, SALDO) VALUES (?, ?, ?)", e, e, valor);
        }
        ra.addAttribute("email", e);
        return "redirect:/banco";
    }
}