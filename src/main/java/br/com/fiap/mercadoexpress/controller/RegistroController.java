package br.com.fiap.mercadoexpress.controller;

import br.com.fiap.mercadoexpress.entity.Usuario;
import br.com.fiap.mercadoexpress.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequiredArgsConstructor
public class RegistroController {


    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;


    @GetMapping("/cadastro")
    public String cadastroUsuario(Model model){
        model.addAttribute("usuario", new Usuario()); //o return é a pgn html
        return "cadastro";
    }

    @PostMapping("/cadastro")

    public String usuario(@ModelAttribute Usuario usuario){

       usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); //o encode criptografa a senha no banco
       usuarioRepository.save(usuario);
       return "redirect:/login";


    }

    @GetMapping("/login")

    public String login(){ return "login";} //chamando classe html

}
