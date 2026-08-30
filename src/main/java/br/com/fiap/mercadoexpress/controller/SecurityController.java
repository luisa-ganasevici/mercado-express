    package br.com.fiap.mercadoexpress.controller;


    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.ResponseBody;


    @Controller
    public class SecurityController {

        @GetMapping("/home/publico")
        @ResponseBody
            String rotaPublica(){
        return "<h1>Rota publica </h1>";
    }


        @GetMapping("/home/privado")

            String rotaPrivada (){
        return "privado";
        }

        @GetMapping("/home")
            String retornohtml() { return "index"; }



        @GetMapping("/whoami")
        @ResponseBody
        public String whoami(org.springframework.security.core.Authentication authentication) {
            if (authentication == null) {
                return "authentication é NULL";
            }
            return "Nome: " + authentication.getName()
                    + " | Autenticado: " + authentication.isAuthenticated()
                    + " | Authorities: " + authentication.getAuthorities();
        }
    }

