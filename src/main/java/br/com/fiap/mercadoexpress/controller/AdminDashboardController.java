package br.com.fiap.mercadoexpress.controller;

import br.com.fiap.mercadoexpress.entity.Mercado;
import br.com.fiap.mercadoexpress.entity.Pedido;
import br.com.fiap.mercadoexpress.entity.StatusPedido;
import br.com.fiap.mercadoexpress.repository.MercadoRepository;
import br.com.fiap.mercadoexpress.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

@Controller
@RequiredArgsConstructor
public class AdminDashboardController {

    private final PedidoService pedidoService;
    private final MercadoRepository mercadoRepository;

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        List<Pedido> pedidos = pedidoService.listarTodos();
        List<Pedido> pagos = pedidos.stream()
                .filter(p -> p.getStatus() == StatusPedido.PAGO).toList();

        BigDecimal receita = soma(pagos, Pedido::getValorTotal);
        BigDecimal custo = soma(pagos, Pedido::getCustoTotal);

        Map<String, BigDecimal> vendas = new LinkedHashMap<>();
        Map<String, BigDecimal> lucros = new LinkedHashMap<>();
        for (Pedido p : pagos) {
            String nome = p.getMercado().getNome();
            BigDecimal c = p.getCustoTotal() == null ? BigDecimal.ZERO : p.getCustoTotal();
            vendas.merge(nome, p.getValorTotal(), BigDecimal::add);
            lucros.merge(nome, p.getValorTotal().subtract(c), BigDecimal::add);
        }

        List<String> statusLabels = new ArrayList<>();
        List<Long> statusValores = new ArrayList<>();
        for (StatusPedido s : StatusPedido.values()) {
            statusLabels.add(s.name());
            statusValores.add(pedidos.stream().filter(p -> p.getStatus() == s).count());
        }

        List<Mercado> produtos = mercadoRepository.findAll();

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("totalPedidos", pedidos.size());
        model.addAttribute("totalPagos", pagos.size());
        model.addAttribute("receita", receita);
        model.addAttribute("custo", custo);
        model.addAttribute("lucro", receita.subtract(custo));
        model.addAttribute("vendasLabels", new ArrayList<>(vendas.keySet()));
        model.addAttribute("vendasValores", vendas.values().stream().map(BigDecimal::doubleValue).toList());
        model.addAttribute("lucroValores", lucros.values().stream().map(BigDecimal::doubleValue).toList());
        model.addAttribute("statusLabels", statusLabels);
        model.addAttribute("statusValores", statusValores);
        model.addAttribute("estoqueLabels", produtos.stream().map(Mercado::getNome).toList());
        model.addAttribute("estoqueValores", produtos.stream()
                .map(m -> m.getEstoque() == null ? 0 : m.getEstoque()).toList());
        return "dashboard";
    }

    private BigDecimal soma(List<Pedido> lista, Function<Pedido, BigDecimal> campo) {
        return lista.stream().map(campo).filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}