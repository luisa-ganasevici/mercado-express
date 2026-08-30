package br.com.fiap.mercadoexpress.controller;

import br.com.fiap.mercadoexpress.entity.Mercado;
import br.com.fiap.mercadoexpress.service.MercadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MercadoWebController {

    private final MercadoService mercadoService;

    @GetMapping("/produtos")
    public String getProdutos(Model model) {
        List<Mercado> listaProdutos = mercadoService.listarTodos();
        model.addAttribute("produto", listaProdutos);
        return "produtos";
    }

    @PostMapping("/produtos")
    public String criarProduto(@ModelAttribute Mercado mercado) {
        mercadoService.criar(mercado);
        return "redirect:/produtos";
    }

    private boolean isAdmin(Authentication authentication) {
        if (authentication == null) return false;
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")); //autenticacao para adms
    }
}