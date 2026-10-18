ALTER TABLE TDS_TB_MERCADO ADD (CUSTO NUMBER(10,2));

UPDATE TDS_TB_MERCADO SET CUSTO = ROUND(PRECO * 0.6, 2) WHERE CUSTO IS NULL;

CREATE SEQUENCE PEDIDO_SEQ START WITH 1 INCREMENT BY 1;

CREATE TABLE TDS_TB_PEDIDO (
                               ID           NUMBER(19)    NOT NULL,
                               MERCADO_ID   NUMBER(19)    NOT NULL,
                               USERNAME     VARCHAR2(255),
                               EMAIL        VARCHAR2(150),
                               QUANTIDADE   NUMBER(10)    NOT NULL,
                               VALOR_TOTAL  NUMBER(12,2)  NOT NULL,
                               CUSTO_TOTAL  NUMBER(12,2),
                               STATUS       VARCHAR2(20)  NOT NULL,
                               DATA_PEDIDO  TIMESTAMP     DEFAULT SYSTIMESTAMP,
                               CONSTRAINT PK_TDS_TB_PEDIDO PRIMARY KEY (ID),
                               CONSTRAINT FK_PEDIDO_MERCADO FOREIGN KEY (MERCADO_ID) REFERENCES TDS_TB_MERCADO (ID)
);