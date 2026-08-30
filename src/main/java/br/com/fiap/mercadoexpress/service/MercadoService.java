package br.com.fiap.mercadoexpress.service;

import br.com.fiap.mercadoexpress.entity.Mercado;
import br.com.fiap.mercadoexpress.repository.MercadoRepository;
import br.com.fiap.mercadoexpress.exception.MercadoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MercadoService {

    private final MercadoRepository mercadoRepository;

    public List<Mercado> listarTodos() {
        return mercadoRepository.findAll();
    }

    public Mercado buscarPorId(Long id) {
        return mercadoRepository.findById(id)
                .orElseThrow(() -> new MercadoNotFoundException(id));
    }

    public Mercado criar(Mercado mercado) {
        return mercadoRepository.save(mercado);
    }

    public Mercado atualizar(Long id, Mercado mercadoAtualizado) {
        Mercado mercado = buscarPorId(id);
        mercado.setNome(mercadoAtualizado.getNome());
        mercado.setTipo(mercadoAtualizado.getTipo());
        mercado.setSetor(mercadoAtualizado.getSetor());
        mercado.setTamanho(mercadoAtualizado.getTamanho());
        mercado.setCor(mercadoAtualizado.getCor());
        mercado.setPreco(mercadoAtualizado.getPreco());
        mercado.setEstoque(mercadoAtualizado.getEstoque());
        mercado.setDisponivel(mercadoAtualizado.getDisponivel());
        return mercadoRepository.save(mercado);
    }
    public void marcarIndisponivel(Long id) {
        Mercado mercado = buscarPorId(id);
        mercado.setDisponivel(false);
        mercadoRepository.save(mercado);
    }

    public Mercado atualizarEstoque(Long id, Integer novaQuantidade) {
        Mercado mercado = buscarPorId(id);
        mercado.setEstoque(novaQuantidade);
        if (novaQuantidade != null && novaQuantidade <= 0) {
            mercado.setDisponivel(false);
        }
        return mercadoRepository.save(mercado);
    }

    public Mercado atualizarParcial(Long id, Mercado mercadoParcial) {
        Mercado mercado = buscarPorId(id);

        if (mercadoParcial.getNome() != null) mercado.setNome(mercadoParcial.getNome());
        if (mercadoParcial.getTipo() != null) mercado.setTipo(mercadoParcial.getTipo());
        if (mercadoParcial.getSetor() != null) mercado.setSetor(mercadoParcial.getSetor());
        if (mercadoParcial.getTamanho() != null) mercado.setTamanho(mercadoParcial.getTamanho());
        if (mercadoParcial.getPreco() != null) mercado.setPreco(mercadoParcial.getPreco());
        if (mercadoParcial.getEstoque() != null) mercado.setEstoque(mercadoParcial.getEstoque());
        if (mercadoParcial.getCor() != null) mercado.setCor(mercadoParcial.getCor());
        if (mercadoParcial.getDisponivel() != null) mercado.setDisponivel(mercadoParcial.getDisponivel());

        return mercadoRepository.save(mercado);
    }

    public void deletar(Long id) {
        Mercado mercado = buscarPorId(id);
        mercadoRepository.delete(mercado);
    }
}