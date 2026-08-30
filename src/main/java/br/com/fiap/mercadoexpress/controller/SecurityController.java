    package br.com.fiap.mercadoexpress.controller;


    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.ResponseBody;


    @Controller
    public class SecurityController {

        @GetMapping("/home/publico")
        public String rotaPublica(){
            return "logado";
        }


        @GetMapping("/home/privado")

            String rotaPrivada (){
        return "privado";
        }

        @GetMapping("/home")
            String retornohtml() { return "index"; }




    }

