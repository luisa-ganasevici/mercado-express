package br.com.fiap.mercadoexpress.controller;


import br.com.fiap.mercadoexpress.entity.Mercado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import br.com.fiap.mercadoexpress.service.MercadoService;

@RequiredArgsConstructor
@Controller

public class MercadoWebController {


    private final MercadoService mercadoService;

    @RequestMapping("/produtos")
    public String produtos(Model model) {
        List<Mercado> listaProdutos = mercadoService.listarTodos();

        model.addAttribute("produtos", listaProdutos);

        return "produtos";


    }
}