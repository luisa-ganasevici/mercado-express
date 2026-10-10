package br.com.fiap.mercadoexpress.controller;

import br.com.fiap.mercadoexpress.entity.Mercado;
import br.com.fiap.mercadoexpress.service.MercadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MercadoWebController {

    private final MercadoService mercadoService;

    @GetMapping("/produtos")
    public String getProdutos(@RequestParam(required = false) String nome,
                              Model model, Authentication authentication) {
        List<Mercado> listaProdutos = mercadoService.listarTodos();

        if (nome != null && !nome.isBlank()) {
            listaProdutos = listaProdutos.stream()
                    .filter(p -> p.getNome().toLowerCase().contains(nome.toLowerCase()))
                    .toList();
        }

        model.addAttribute("produtos", listaProdutos);
        model.addAttribute("isAdmin", isAdmin(authentication));
        model.addAttribute("logado", authentication != null && authentication.isAuthenticated());
        model.addAttribute("termoBusca", nome);
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

    @GetMapping("/produtos/{id}/comprar")
    public String formComprar(@PathVariable Long id, Model model) {
        model.addAttribute("mercado", mercadoService.buscarPorId(id));
        return "produto-comprar";
    }

    @PostMapping("/produtos/{id}/comprar")
    public String comprar(@PathVariable Long id, @RequestParam Integer quantidade, Model model) {
        Mercado mercado = mercadoService.buscarPorId(id);

        if (quantidade == null || quantidade <= 0 || quantidade > mercado.getEstoque()) {
            model.addAttribute("mercado", mercado);
            model.addAttribute("erro", "Quantidade indisponível em estoque.");
            return "produto-comprar";
        }

        int novaQuantidade = mercado.getEstoque() - quantidade;
        mercadoService.atualizarEstoque(id, novaQuantidade);

        model.addAttribute("mercado", mercado);
        model.addAttribute("quantidade", quantidade);
        return "compra-confirmada";
    }

    @PostMapping("/produtos/{id}/excluir")
    public String excluirProduto(@PathVariable Long id) {
        mercadoService.marcarIndisponivel(id);
        return "redirect:/produtos";
    }

    @GetMapping("/produtos/editar")
    public String formEditar(@RequestParam Long id, Model model) {
        model.addAttribute("mercado", mercadoService.buscarPorId(id));
        return "produto-editar";
    }

    @PostMapping("/produtos/{id}/editar")
    public String editarProduto(@PathVariable Long id, @ModelAttribute Mercado mercado) {
        mercadoService.atualizar(id, mercado);
        return "redirect:/produtos";
    }

    @PostMapping("/produtos/{id}/estoque")
    public String atualizarEstoqueRapido(@PathVariable Long id, @RequestParam Integer estoque) {
        mercadoService.atualizarEstoque(id, estoque);
        return "redirect:/produtos";
    }

    private boolean isAdmin(Authentication authentication) {
        if (authentication == null) return false;
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}