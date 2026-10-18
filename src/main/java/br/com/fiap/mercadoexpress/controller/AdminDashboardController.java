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

    public record Barra(String rotulo, String texto, int pct) {}

    private final PedidoService pedidoService;
    private final MercadoRepository mercadoRepository;

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        List<Pedido> pedidos = pedidoService.listarTodos();
        List<Pedido> pagos = pedidos.stream()
                .filter(p -> p.getStatus() == StatusPedido.PAGO).toList();

        BigDecimal receita = soma(pagos, Pedido::getValorTotal);
        BigDecimal custo = soma(pagos, Pedido::getCustoTotal);

        Map<String, Double> vendas = new LinkedHashMap<>();
        Map<String, Double> lucros = new LinkedHashMap<>();
        for (Pedido p : pagos) {
            String nome = p.getMercado().getNome();
            BigDecimal c = p.getCustoTotal() == null ? BigDecimal.ZERO : p.getCustoTotal();
            vendas.merge(nome, p.getValorTotal().doubleValue(), Double::sum);
            lucros.merge(nome, p.getValorTotal().subtract(c).doubleValue(), Double::sum);
        }

        Map<String, Double> status = new LinkedHashMap<>();
        for (StatusPedido s : StatusPedido.values()) {
            status.put(s.name(), (double) pedidos.stream().filter(p -> p.getStatus() == s).count());
        }

        Map<String, Double> estoque = new LinkedHashMap<>();
        for (Mercado m : mercadoRepository.findAll()) {
            estoque.put(m.getNome(), (double) (m.getEstoque() == null ? 0 : m.getEstoque()));
        }

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("totalPedidos", pedidos.size());
        model.addAttribute("totalPagos", pagos.size());
        model.addAttribute("receita", receita);
        model.addAttribute("custo", custo);
        model.addAttribute("lucro", receita.subtract(custo));
        model.addAttribute("vendas", barras(vendas, true));
        model.addAttribute("lucros", barras(lucros, true));
        model.addAttribute("estoque", barras(estoque, false));
        model.addAttribute("status", barras(status, false));
        return "dashboard";
    }

    private List<Barra> barras(Map<String, Double> dados, boolean moeda) {
        double max = dados.values().stream().mapToDouble(Double::doubleValue).max().orElse(0);
        List<Barra> lista = new ArrayList<>();
        for (Map.Entry<String, Double> e : dados.entrySet()) {
            double v = e.getValue();
            int pct = (max <= 0 || v <= 0) ? 0 : (int) Math.max(2, Math.round(v * 100 / max));
            String texto = moeda
                    ? String.format(Locale.of("pt", "BR"), "R$ %,.2f", v)
                    : String.valueOf((long) v);
            lista.add(new Barra(e.getKey(), texto, pct));
        }
        return lista;
    }

    private BigDecimal soma(List<Pedido> lista, Function<Pedido, BigDecimal> campo) {
        return lista.stream().map(campo).filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}