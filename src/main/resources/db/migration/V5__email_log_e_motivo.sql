ALTER TABLE TDS_TB_PEDIDO ADD MOTIVO VARCHAR2(200);

CREATE SEQUENCE EMAIL_LOG_SEQ START WITH 1 INCREMENT BY 1;

CREATE TABLE TDS_TB_EMAIL_LOG (
                                  ID             NUMBER PRIMARY KEY,
                                  PEDIDO_ID      NUMBER,
                                  DESTINATARIO   VARCHAR2(150),
                                  ASSUNTO        VARCHAR2(200),
                                  STATUS         VARCHAR2(20),
                                  MENSAGEM_ERRO  VARCHAR2(500),
                                  DATA_ENVIO     TIMESTAMP DEFAULT SYSTIMESTAMP
);