package br.com.fiap.mercadoexpress.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "TDS_TB_USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
    public class Usuario {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq_generator")
        @SequenceGenerator(name = "usuario_seq_generator", sequenceName = "USUARIO_SEQ", allocationSize = 1)

        @Column(name = "Id")
        private long id;

        @Column(name = "username")
        private String username;

        @Column(name = "password")
        private String password;

        @Column (name = "Role")
        private String role;

    }
