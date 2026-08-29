package br.com.fiap.mercadoexpress.controller;


import br.com.fiap.mercadoexpress.entity.Mercado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MercadoWebController {

    @RequestMapping("/produtos")
    public String produtos(Mercado mercado) {
        List<Mercado> listaProdutos = mercadoService.listarTodos();

        model.addAttribute("produtos", listaProdutos);

        return "produtos";
    }