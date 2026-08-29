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
        mercado.setPreco(mercadoAtualizado.getPreco());
        mercado.setEstoque(mercadoAtualizado.getEstoque());
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

        return mercadoRepository.save(mercado);
    }

    public void deletar(Long id) {
        Mercado mercado = buscarPorId(id);
        mercadoRepository.delete(mercado);
    }
}