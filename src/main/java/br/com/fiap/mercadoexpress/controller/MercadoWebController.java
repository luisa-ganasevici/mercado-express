package br.com.fiap.mercadoexpress.controller;

import br.com.fiap.mercadoexpress.entity.Mercado;
import br.com.fiap.mercadoexpress.service.MercadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MercadoWebController {

    private final MercadoService mercadoService;

    @GetMapping("/produtos")
    public String getProdutos(Model model, Authentication authentication) {
        List<Mercado> listaProdutos = mercadoService.listarTodos();
        model.addAttribute("produtos", listaProdutos);
        model.addAttribute("isAdmin", isAdmin(authentication));
        model.addAttribute("logado", authentication != null && authentication.isAuthenticated());
        return "produtos";
    }

    @GetMapping("/produtos/novo")
    public String formNovoProduto(Model model) {
        model.addAttribute("mercado", new Mercado());
        return "produto-form";
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

    @GetMapping("/produtos/{id}/comprar")
    public String formComprar(@PathVariable Long id, Model model) {
        model.addAttribute("mercado", mercadoService.buscarPorId(id));
        return "produto-comprar";
    }
    @PostMapping("/produtos/{id}/comprar")
    public String comprar(@PathVariable Long id) {
        Mercado mercado = mercadoService.buscarPorId(id);
        int novaQuantidade = mercado.getEstoque() - 1;
        mercadoService.atualizarEstoque(id, novaQuantidade);
        return "redirect:/produtos";
    }


}