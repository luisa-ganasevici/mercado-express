CREATE SEQUENCE MERCADO_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE USUARIO_SEQ START WITH 1 INCREMENT BY 1;

CREATE TABLE TDS_TB_MERCADO (
                                ID          NUMBER(19)    NOT NULL,
                                NOME        VARCHAR2(255) NOT NULL,
                                TIPO        VARCHAR2(255),
                                SETOR       VARCHAR2(255),
                                TAMANHO     VARCHAR2(255),
                                PRECO       NUMBER(10,2),
                                ESTOQUE     NUMBER(10),
                                COR         VARCHAR2(255),
                                DISPONIVEL  NUMBER(1) DEFAULT 1,
                                CONSTRAINT PK_TDS_TB_MERCADO PRIMARY KEY (ID)
);

CREATE TABLE TDS_TB_USUARIO (
                                ID        NUMBER(19)    NOT NULL,
                                USERNAME  VARCHAR2(255),
                                PASSWORD  VARCHAR2(255),
                                ROLE      VARCHAR2(50),
                                CONSTRAINT PK_TDS_TB_USUARIO PRIMARY KEY (ID)
);