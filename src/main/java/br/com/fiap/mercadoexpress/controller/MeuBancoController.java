package br.com.fiap.mercadoexpress.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.fiap.mercadoexpress.repository.UsuarioRepository;

@Controller
public class MeuBancoController {

    private final UsuarioRepository usuarioRepository;
    private final String bancoUrl;

    public MeuBancoController(UsuarioRepository usuarioRepository,
                              @Value("${banco.url:http://localhost:8083}") String bancoUrl) {
        this.usuarioRepository = usuarioRepository;
        this.bancoUrl = bancoUrl;
    }

    @GetMapping("/meu-banco")
    public String meuBanco(Authentication authentication) {
        String email = usuarioRepository.findByUsername(authentication.getName())
                .map(u -> u.getEmail())
                .orElse("");
        return "redirect:" + bancoUrl + "/banco?email="
                + URLEncoder.encode(email, StandardCharsets.UTF_8);
    }
}