package br.com.fiap.mercadoexpress.controller;

import br.com.fiap.mercadoexpress.entity.Usuario;
import br.com.fiap.mercadoexpress.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UsuarioWebController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        return "usuarios";
    }

    @PostMapping("/usuarios/{id}/promover")
    public String promoverAdmin(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
        usuario.setRole("ADMIN");
        usuarioRepository.save(usuario);
        return "redirect:/usuarios";
    }

    @PostMapping("/usuarios/{id}/rebaixar")
    public String rebaixarCliente(@PathVariable Long id, Authentication authentication) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));

        if (usuario.getUsername().equals(authentication.getName())) {
            return "redirect:/usuarios?erro=nao-pode-rebaixar-si-mesmo";
        }

        usuario.setRole("CLIENTE");
        usuarioRepository.save(usuario);
        return "redirect:/usuarios";
    }

}