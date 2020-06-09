drop schema IF EXISTS vendas cascade;
create schema vendas;
set search_path to vendas;

create table tb_cliente (
	id integer not null,
	id_ramo_atividade integer not null,
	id_vendedor integer default null,
	nome_fantasia varchar(150) not null,
	razao_social varchar(150) default null,
	cnpj varchar(15) null,
	cpf varchar(18) default null,
	insc_estadual varchar(12) null,
	site varchar (250) default null,
	email varchar (250) default null,
	data_ultimo_contato date default null,
	informacoes_adicionais varchar(500),
	prospeccao_finalizada boolean default false
);

create table tb_logradouro_cliente (
	id integer not null,
	id_cliente integer not null
);

create table tb_cliente_tb_transportadora (
	id_cliente integer not null,
	id_transportadora integer not null
);


create table tb_contato (
	id integer not null,
	id_logradouro integer default null,
	nome varchar (20) not null,
	sobrenome varchar (100) default null,
	cpf varchar(18) default null,
	ddi_1 varchar(3) default null,
	ddd_1 varchar(3) default null,
	telefone_1 varchar(9) default null,
	ramal_1 varchar(5),
	fax_1 varchar(8) default null,
	ddi_2 varchar(3) default null,
	ddd_2 varchar(3),
	telefone_2 varchar(9) default null,
	ramal_2 varchar(5) default null,
	fax_2 varchar(8) default null,
	email varchar(200) default null
);

create table tb_logradouro (
	id integer not null,
	id_endereco varchar(8) not null,
	id_tipo_logradouro integer not null,
	numero integer,
	complemento varchar(50)
);

create table tb_ramo_atividade (
	id integer not null,
	sigla varchar(10) not null,
	descricao varchar (100) not null,
	ativo boolean default true
);

create table tb_regiao (
	id integer not null,
	nome varchar(50) not null
);

create table tb_regiao_tb_bairro (
	id_regiao integer not null,
	id_bairro integer not null
);

create table tb_material (
	id integer not null,
	sigla varchar (10) not null,
	descricao varchar(50),
	peso_especifico numeric(5, 2) not null,
	ativo boolean default true
);

create table tb_material_tb_representada (
	id_material integer not null,
	id_representada integer not null
);

create table tb_perfil_acesso (
	id integer not null,
	descricao varchar(50) not null
);

create table tb_representada (
	id integer not null,
	id_logradouro integer default null,
	nome_fantasia varchar(150) not null,
	razao_social varchar(150) default null,
	cnpj varchar(15) null,
	insc_estadual varchar(12) null,
	site varchar (250) default null,
	email varchar (250) default null,
	ativo boolean default true
);

create table tb_tipo_logradouro (
	id integer not null,
	descricao varchar(20) not null
);

create table tb_contato_representada (
	id integer not null,
	id_representada integer not null
);

create table tb_contato_cliente (
	id integer not null,
	id_cliente integer not null
);

create table tb_contato_transportadora (
	id integer not null,	
	id_transportadora integer not null
);

create table tb_transportadora (
	id integer not null,
	id_logradouro integer default null,
	nome_fantasia varchar(150) not null,
	razao_social varchar(150) default null,
	cnpj varchar(15) null,
	insc_estadual varchar(12) null,
	site varchar (250) default null,
	area_atuacao varchar(250) default null,
	ativo boolean default true
);


create table tb_usuario (
	id integer not null, 
	id_logradouro integer default null,
	email varchar(50) not null unique,
	senha varchar(30) not null, 
	nome varchar(20) not null, 
	sobrenome varchar(40) not null,
	cpf varchar(11) default null,	
	ativo boolean default false,
	vendedor boolean default false 
);

create table tb_usuario_tb_perfil_acesso (
	id_usuario integer not null,
	id_perfil_acesso integer not null
);

create table tb_contato_usuario (
	id integer not null,
	id_usuario integer not null
);

create table tb_tipo_entrega (
	id integer not null,
	sigla varchar(10) not null,
	descricao varchar(100)
);

create table tb_situacao_pedido (
	id integer not null,
	descricao varchar(50)
);

create table tb_finalidade_pedido (
	id varchar(25) not null,
	descricao varchar(100)
);

create table tb_pedido (
	id integer not null,
	id_cliente integer not null,
	id_representada integer not null,
	id_transportadora integer,
	id_transportadora_redespacho integer,
	id_tipo_entrega integer,
	id_vendedor integer not null,
	id_situacao_pedido integer not null,
	id_finalidade_pedido varchar not null,
	id_contato integer not null,
	numero_pedido_cliente varchar(15),
	data_inclusao date not null,
	data_envio date,
	data_entrega date,
	observacao varchar(500),
	valor_pedido numeric(10, 2) default 0,
	forma_pagamento varchar (30)
);

create table tb_contato_pedido (
	id integer not null,
	id_pedido integer not null
);

create table tb_forma_material (
	id integer not null,
	sigla varchar(5) not null,
	descricao varchar(100),
	aliquota_ipi numeric(5,5)
);

create table tb_item_pedido (
	id integer not null,
	id_pedido integer not null,
	id_material integer not null,
	id_forma_material integer not null,
	id_tipo_venda integer not null,
	descricao_peca varchar (100),
	comprimento integer,
	medida_interna integer,
	medida_externa integer,
	quantidade integer not null,
	preco_unidade numeric(9, 2),
	preco_venda numeric(9, 2)
);

create table tb_tipo_venda (
	id integer not null,
	sigla char not null,
	descricao varchar (20) 
);

create table tb_configuracao_sistema (
	parametro varchar (40) not null,
	valor varchar (50) not null
);

create table tb_remuneracao (
	id integer not null,
	id_usuario integer not null,
	salario real,
	comissao real,
	data_inicio_vigencia date default null,
	data_fim_vigencia date default null
);

ALTER TABLE tb_cliente ADD PRIMARY KEY (id);
ALTER TABLE tb_contato_cliente ADD PRIMARY KEY (id);
ALTER TABLE tb_contato_representada ADD PRIMARY KEY (id);
ALTER TABLE tb_contato_transportadora ADD PRIMARY KEY (id);
ALTER TABLE tb_contato ADD PRIMARY KEY (id);
ALTER TABLE tb_forma_material ADD PRIMARY KEY (id);
ALTER TABLE tb_item_pedido ADD PRIMARY KEY (id);
ALTER TABLE tb_logradouro ADD PRIMARY KEY (id);
ALTER TABLE tb_logradouro_cliente ADD PRIMARY KEY (id);
ALTER TABLE tb_material ADD PRIMARY KEY (id);
ALTER TABLE tb_perfil_acesso ADD PRIMARY KEY (id);
ALTER TABLE tb_pedido ADD PRIMARY KEY (id);
ALTER TABLE tb_ramo_atividade ADD PRIMARY KEY (id);
ALTER TABLE tb_regiao ADD PRIMARY KEY (id);
ALTER TABLE tb_remuneracao ADD PRIMARY KEY (id);
ALTER TABLE tb_representada ADD PRIMARY KEY (id);
ALTER TABLE tb_tipo_logradouro ADD PRIMARY KEY (id);
ALTER TABLE tb_tipo_venda ADD PRIMARY KEY (id);
ALTER TABLE tb_transportadora ADD PRIMARY KEY (id);
ALTER TABLE tb_situacao_pedido ADD PRIMARY KEY (id);
ALTER TABLE tb_usuario ADD PRIMARY KEY (id);

ALTER TABLE tb_cliente ADD CONSTRAINT id_ramo_atividade FOREIGN KEY (id_ramo_atividade) REFERENCES tb_ramo_atividade (id);
ALTER TABLE tb_cliente ADD CONSTRAINT id_vendedor FOREIGN KEY (id_vendedor) REFERENCES tb_usuario (id);
ALTER TABLE tb_cliente_tb_transportadora ADD CONSTRAINT id_cliente FOREIGN KEY (id_cliente) REFERENCES tb_cliente (id);
ALTER TABLE tb_cliente_tb_transportadora ADD CONSTRAINT id_transportadora FOREIGN KEY (id_transportadora) REFERENCES tb_transportadora (id);
ALTER TABLE tb_contato ADD CONSTRAINT id_logradouro FOREIGN KEY (id_logradouro) REFERENCES tb_logradouro (id);
ALTER TABLE tb_pedido ADD CONSTRAINT id_contato FOREIGN KEY (id_contato) REFERENCES tb_contato (id);
ALTER TABLE tb_item_pedido ADD CONSTRAINT id_material FOREIGN KEY (id_material) REFERENCES tb_material (id);
ALTER TABLE tb_item_pedido ADD CONSTRAINT id_pedido FOREIGN KEY (id_pedido) REFERENCES tb_pedido (id);
ALTER TABLE tb_item_pedido ADD CONSTRAINT id_tipo_venda FOREIGN KEY (id_tipo_venda) REFERENCES tb_tipo_venda (id);
ALTER TABLE tb_item_pedido ADD CONSTRAINT id_forma_material FOREIGN KEY (id_forma_material) REFERENCES tb_forma_material (id);

ALTER TABLE tb_logradouro ADD CONSTRAINT id_endereco FOREIGN KEY (id_endereco) REFERENCES enderecamento.tb_endereco (cep);
ALTER TABLE tb_logradouro ADD CONSTRAINT id_tipo_logradouro FOREIGN KEY (id_tipo_logradouro) REFERENCES tb_tipo_logradouro (id);
ALTER TABLE tb_logradouro_cliente ADD CONSTRAINT id_logradouro_cliente FOREIGN KEY (id) REFERENCES tb_logradouro (id);
ALTER TABLE tb_logradouro_cliente ADD CONSTRAINT id_cliente FOREIGN KEY (id_cliente) REFERENCES tb_cliente (id);
ALTER TABLE tb_contato_cliente ADD CONSTRAINT id_cliente FOREIGN KEY (id_cliente) references tb_cliente (id);
ALTER TABLE tb_contato_cliente ADD CONSTRAINT id_contato_cliente FOREIGN KEY (id) references tb_contato (id);

ALTER TABLE tb_contato_representada ADD CONSTRAINT id_representada FOREIGN KEY (id_representada) references tb_representada (id);
ALTER TABLE tb_contato_representada ADD CONSTRAINT id_contato_representada FOREIGN KEY (id) references tb_contato (id);
ALTER TABLE tb_contato_transportadora ADD CONSTRAINT id_transportadora FOREIGN KEY (id_transportadora) references tb_transportadora (id);
ALTER TABLE tb_contato_transportadora ADD CONSTRAINT id_contato_transportadora FOREIGN KEY (id) references tb_contato (id);
ALTER TABLE tb_contato_usuario ADD CONSTRAINT id_usuario FOREIGN KEY (id_usuario) references tb_usuario (id);
ALTER TABLE tb_contato_usuario ADD CONSTRAINT id_contato_usuario FOREIGN KEY (id) references tb_contato (id);

ALTER TABLE tb_material_tb_representada ADD CONSTRAINT id_material FOREIGN KEY (id_material) references tb_material (id);
ALTER TABLE tb_material_tb_representada ADD CONSTRAINT id_representada FOREIGN KEY (id_representada) references tb_representada (id);
ALTER TABLE tb_regiao_tb_bairro ADD CONSTRAINT id_regiao FOREIGN KEY (id_regiao) references tb_regiao (id);
ALTER TABLE tb_regiao_tb_bairro ADD CONSTRAINT id_bairro FOREIGN KEY (id_bairro) references enderecamento.tb_bairro (id_bairro);
ALTER TABLE tb_remuneracao ADD CONSTRAINT id_usuario FOREIGN KEY (id_usuario) REFERENCES tb_usuario (id);
ALTER TABLE tb_transportadora ADD CONSTRAINT id_logradouro FOREIGN KEY (id_logradouro) REFERENCES tb_logradouro (id);
ALTER TABLE tb_usuario_tb_perfil_acesso ADD CONSTRAINT id_usuario FOREIGN KEY (id_usuario) REFERENCES tb_usuario (id);
ALTER TABLE tb_usuario_tb_perfil_acesso ADD CONSTRAINT id_perfil_acesso FOREIGN KEY (id_perfil_acesso) REFERENCES tb_perfil_acesso (id);

create sequence seq_cliente_id increment by 1 minvalue 1 no maxvalue start with 1;
create sequence seq_contato_representada_id increment by 1 minvalue 1 no maxvalue start with 1;
create sequence seq_contato_transportadora_id increment by 1 minvalue 1 no maxvalue start with 1;
create sequence seq_contato_id increment by 1 minvalue 1 no maxvalue start with 1;
create sequence seq_item_pedido_id increment by 1 minvalue 1 no maxvalue start with 1;
create sequence seq_logradouro_id increment by 1 minvalue 1 no maxvalue start with 1;
create sequence seq_material_id increment by 1 minvalue 1 no maxvalue start with 1;
create sequence seq_pedido_id increment by 1 minvalue 1 no maxvalue start with 1;
create sequence seq_perfil_acesso_id increment by 1 minvalue 1 no maxvalue start with 1;
create sequence seq_ramo_atividade_id increment by 1 minvalue 1 no maxvalue start with 1;
create sequence seq_regiao_id increment by 1 minvalue 1 no maxvalue start with 1;
create sequence seq_representada_id increment by 1 minvalue 1 no maxvalue start with 1;
create sequence seq_remuneracao_id increment by 1 minvalue 1 no maxvalue start with 1;
create sequence seq_transportadora_id increment by 1 minvalue 1 no maxvalue start with 1;
create sequence seq_usuario_id increment by 1 minvalue 1 no maxvalue start with 1;

insert into tb_perfil_acesso values (nextval('seq_perfil_acesso_id'), 'ADMINISTRACAO');
insert into tb_perfil_acesso values (nextval('seq_perfil_acesso_id'), 'ASSOCIACAO_CLIENTE_VENDEDOR');
insert into tb_perfil_acesso values (nextval('seq_perfil_acesso_id'), 'CONSULTA_RELATORIO_VENDAS_REPRESENTADA');
insert into tb_perfil_acesso values (nextval('seq_perfil_acesso_id'), 'CONSULTA_RELATORIO_CLIENTE_REGIAO');
insert into tb_perfil_acesso values (nextval('seq_perfil_acesso_id'), 'CONSULTA_RELATORIO_CLIENTE_VENDEDOR');
insert into tb_perfil_acesso values (nextval('seq_perfil_acesso_id'), 'CONSULTA_RELATORIO_ENTREGA');
insert into tb_perfil_acesso values (nextval('seq_perfil_acesso_id'), 'CADASTRO_BASICO');
insert into tb_perfil_acesso values (nextval('seq_perfil_acesso_id'), 'CADASTRO_CLIENTE');
insert into tb_perfil_acesso values (nextval('seq_perfil_acesso_id'), 'CADASTRO_PEDIDO');
insert into tb_perfil_acesso values (nextval('seq_perfil_acesso_id'), 'MANUTENCAO');

insert into tb_tipo_logradouro values (0, 'COBRANCA');
insert into tb_tipo_logradouro values (1, 'COMERCIAL');
insert into tb_tipo_logradouro values (2, 'ENTREGA');
insert into tb_tipo_logradouro values (3, 'FATURAMENTO');
insert into tb_tipo_logradouro values (4, 'RESIDENCIAL');

insert into tb_forma_material values (0, 'CH', 'CHAPA', 0.15);
insert into tb_forma_material values (1, 'BR', 'BARRA REDONDA', 0.1);
insert into tb_forma_material values (2, 'BQ', 'BARRA QUADRADA', 0.1);
insert into tb_forma_material values (3, 'BS', 'BARRA SEXTAVADA', 0.1);
insert into tb_forma_material values (4, 'TB', 'TUBO', 0.0);
insert into tb_forma_material values (5, 'PC', 'PEÇA', 0.0);

insert into tb_situacao_pedido values (0, 'PEDIDO EM DIGITAÇÃO');
insert into tb_situacao_pedido values (1, 'ORÇAMENTO');
insert into tb_situacao_pedido values (2, 'PEDIDO ENVIADO');
insert into tb_situacao_pedido values (3, 'PEDIDO CANCELADO');

insert into tb_finalidade_pedido values ('INDUSTRIALIZACAO', 'INSDUSTRIALIZAÇÃO');
insert into tb_finalidade_pedido values ('CONSUMO', 'CONSUMO');
insert into tb_finalidade_pedido values ('REVENDA', 'REVENDA');

insert into tb_tipo_entrega values (0, 'CIF', 'PEDIDO ENTREGUE NO CLIENTE');
insert into tb_tipo_entrega values (1, 'CIF_TRANS', 'PEDIDO ENTREGUE NA TRANPORTADORA PARA REDESPACHO');
insert into tb_tipo_entrega values (2, 'FOB', 'PEDIDO RETIRADO PELO CLIENTE');

insert into tb_tipo_venda values (0, 'K', 'VENDA POR KILO');
insert into tb_tipo_venda values (1, 'P', 'VENDA POR PEÇA');

insert into tb_configuracao_sistema values ('DIAS_INATIVIDADE_CLIENTE', '90');
insert into tb_configuracao_sistema values ('NOME_SERVIDOR_SMTP', 'smtp.plastecno.com.br');
insert into tb_configuracao_sistema values ('PORTA_SERVIDOR_SMTP', '587');
insert into tb_configuracao_sistema values ('SSL_HABILITADO_PARA_SMTP', 'false');

insert into vendas.tb_usuario (id, email, senha, nome, sobrenome, ativo, vendedor) 
	values (nextval('vendas.seq_usuario_id'), 'admin@plastecno.com.br', '345678', 'ADMIN', 'ADMIN', true, false);
insert into vendas.tb_usuario_tb_perfil_acesso values (
	(select id from vendas.tb_usuario where email = 'admin@plastecno.com.br'),
	(select id from vendas.tb_perfil_acesso where descricao = 'ADMINISTRACAO'));
	
create table tb_pedido_tb_logradouro (
	id_pedido integer not null,
	id_logradouro integer not null
);

ALTER TABLE tb_pedido_tb_logradouro ADD CONSTRAINT id_pedido FOREIGN KEY (id_pedido) REFERENCES tb_pedido (id);
ALTER TABLE tb_pedido_tb_logradouro ADD CONSTRAINT id_logradouro FOREIGN KEY (id_logradouro) REFERENCES tb_logradouro (id);


alter table tb_pedido add column valor_pedido_ipi numeric(10, 2) default 0;
alter table tb_item_pedido add column preco_unidade_ipi numeric(9, 2);
alter table tb_material add column importado boolean default false;

create table tb_tipo_apresentacao_ipi (
	id integer not null,
	descricao varchar(50)
);

ALTER TABLE tb_tipo_apresentacao_ipi ADD PRIMARY KEY (id);

insert into tb_tipo_apresentacao_ipi values (0, 'NUNCA');
insert into tb_tipo_apresentacao_ipi values (1, 'SEMPRE');
insert into tb_tipo_apresentacao_ipi values (2, 'OCASIONAL');

alter table tb_representada add column id_tipo_apresentacao_ipi integer;
ALTER TABLE tb_representada ADD CONSTRAINT id_tipo_apresentacao_ipi FOREIGN KEY (id_tipo_apresentacao_ipi ) REFERENCES tb_tipo_apresentacao_ipi (id);

alter table tb_logradouro alter column complemento type varchar(250) ;
alter table tb_logradouro add column codificado boolean default true;


alter table tb_logradouro_cliente add column cancelado boolean default false;

create table tb_pedido_tb_logradouro_cliente (
	id_pedido integer not null,
	id_logradouro_cliente integer not null
);

ALTER TABLE tb_pedido_tb_logradouro_cliente ADD CONSTRAINT id_pedido FOREIGN KEY (id_pedido) REFERENCES tb_pedido (id);
ALTER TABLE tb_pedido_tb_logradouro_cliente ADD CONSTRAINT id_logradouro_cliente FOREIGN KEY (id_logradouro_cliente) REFERENCES tb_logradouro_cliente (id);

ALTER TABLE tb_representada add comissao numeric(3,2) default 0;  

insert into tb_perfil_acesso values (nextval('vendas.seq_perfil_acesso_id'), 'GERENCIA_VENDAS');
insert into tb_perfil_acesso values (nextval('vendas.seq_perfil_acesso_id'), 'OPERACAO_CONTABIL');

ALTER TABLE tb_pedido add cliente_notificado_venda boolean default false;  
ALTER TABLE tb_item_pedido add aliquota_icms numeric(5,5) ;

ALTER TABLE vendas.tb_item_pedido ALTER COLUMN comprimento TYPE numeric(10,2);
ALTER TABLE vendas.tb_item_pedido ALTER COLUMN medida_interna TYPE numeric(10,2);
ALTER TABLE vendas.tb_item_pedido ALTER COLUMN medida_externa TYPE numeric(10,2);
ALTER TABLE vendas.tb_cliente ALTER COLUMN insc_estadual TYPE varchar(15);

alter table vendas.tb_contato add departamento varchar(50) default null;
alter table vendas.tb_contato alter column telefone_1 type varchar(10);
alter table vendas.tb_contato alter column telefone_2 type varchar(10);
alter table vendas.tb_contato alter column fax_1 type varchar(9);
alter table vendas.tb_contato alter column fax_2 type varchar(9);

alter table vendas.tb_pedido alter column observacao type varchar(800);

create table vendas.tb_comentario_cliente (
	id integer not null,
	data_inclusao date not null,
	conteudo varchar(800) not null,
	id_vendedor integer not null,
	id_cliente  integer not null
);

ALTER TABLE vendas.tb_comentario_cliente ADD PRIMARY KEY (id);
ALTER TABLE vendas.tb_comentario_cliente  ADD CONSTRAINT id_vendedor FOREIGN KEY (id_vendedor) REFERENCES vendas.tb_usuario (id);
ALTER TABLE vendas.tb_comentario_cliente  ADD CONSTRAINT id_cliente FOREIGN KEY (id_cliente) REFERENCES vendas.tb_cliente (id);

create sequence vendas.seq_comentario_cliente_id increment by 1 minvalue 1 no maxvalue start with 1;

alter table vendas.tb_item_pedido add aliquota_ipi numeric (5,5) default 0;
alter table vendas.tb_item_pedido add sequencial integer default 0;

create table vendas.tb_tipo_pedido (
	id integer not null,
	descricao varchar(50)
);
ALTER TABLE vendas.tb_tipo_pedido ADD PRIMARY KEY (id);

insert into vendas.tb_tipo_pedido values (0, 'REPRESENTACAO');
insert into vendas.tb_tipo_pedido values (1, 'REVENDA');
insert into vendas.tb_tipo_pedido values (2, 'COMPRA');

alter table vendas.tb_pedido add id_tipo_pedido integer default 0;
ALTER TABLE vendas.tb_pedido ADD CONSTRAINT id_tipo_pedido FOREIGN KEY (id_tipo_pedido) references vendas.tb_tipo_pedido (id);

insert into vendas.tb_perfil_acesso (id, descricao) values (nextval('vendas.seq_perfil_acesso_id'), 'CADASTRO_PEDIDO_COMPRA');

ALTER TABLE vendas.tb_pedido RENAME id_vendedor TO id_proprietario;
INSERT INTO VENDAS.TB_SITUACAO_PEDIDO VALUES (4, 'COMPRA AGUARDANDO RECEBIMENTO');
INSERT INTO VENDAS.TB_SITUACAO_PEDIDO VALUES (5, 'COMPRA RECEBIDA');
ALTER TABLE vendas.tb_item_pedido add item_recebido boolean default false;

create table vendas.tb_item_estoque (
	id integer not null,
	id_material integer not null,
	id_forma_material integer not null,
	descricao_peca varchar (100),
	comprimento numeric(10,2),
  	medida_interna numeric(10,2),
  	medida_externa numeric(10,2),
  	quantidade integer not null default 0,
	preco_medio numeric(9, 2),
	aliquota_icms numeric(5,5) default 0,
	aliquota_ipi numeric (5,5) default 0
);

ALTER TABLE vendas.tb_item_estoque ADD PRIMARY KEY (id);
ALTER TABLE vendas.tb_item_estoque ADD CONSTRAINT id_material FOREIGN KEY (id_material) REFERENCES vendas.tb_material (id);
ALTER TABLE vendas.tb_item_estoque ADD CONSTRAINT id_forma_material FOREIGN KEY (id_forma_material) REFERENCES vendas.tb_forma_material (id);
create sequence vendas.seq_item_estoque_id increment by 1 minvalue 1 no maxvalue start with 1;

create table vendas.tb_tipo_relacionamento (
	id integer not null,
	descricao varchar(50)
);
ALTER TABLE vendas.tb_tipo_relacionamento ADD PRIMARY KEY (id);
insert into vendas.tb_tipo_relacionamento values (0, 'REPRESENTACAO');
insert into vendas.tb_tipo_relacionamento  values (1, 'FORNECIMENTO');
insert into vendas.tb_tipo_relacionamento  values (2, 'REPRESENTACAO E FORNECIMENTO');
insert into vendas.tb_tipo_relacionamento  values (3, 'REVENDA');

ALTER TABLE vendas.tb_representada ADD id_tipo_relacionamento integer not null default 0;
ALTER TABLE vendas.tb_representada ADD CONSTRAINT id_tipo_relacionamento FOREIGN KEY (id_tipo_relacionamento) REFERENCES vendas.tb_tipo_relacionamento (id);

update vendas.tb_representada set id_tipo_relacionamento = 3 where nome_fantasia = 'PLASTECNO MATRIZ';

INSERT INTO VENDAS.TB_SITUACAO_PEDIDO VALUES (6, 'REVENDA AGUARDANDO EMPACOTAMENTO');
INSERT INTO VENDAS.TB_SITUACAO_PEDIDO VALUES (7, 'REVENDA AGUARDANDO ENCOMENDA');


create table vendas.tb_item_reservado (
	id integer not null,
	id_item_estoque integer not null,
	id_item_pedido integer not null,
	data_reserva date default null
);

ALTER TABLE vendas.tb_item_reservado ADD PRIMARY KEY (id);
ALTER TABLE vendas.tb_item_reservado ADD CONSTRAINT id_item_estoque FOREIGN KEY (id_item_estoque) REFERENCES vendas.tb_item_estoque (id);
ALTER TABLE vendas.tb_item_reservado ADD CONSTRAINT id_item_pedido FOREIGN KEY (id_item_pedido) REFERENCES vendas.tb_item_pedido (id);
create sequence vendas.seq_item_reservado_id increment by 1 minvalue 1 no maxvalue start with 1;


INSERT INTO VENDAS.TB_SITUACAO_PEDIDO VALUES (8, 'PEDIDO EMPACOTADO');
alter table vendas.tb_item_pedido add quantidade_reservada integer default 0;


CREATE TABLE VENDAS.TB_TIPO_CLIENTE(
	id integer not null,
	descricao varchar(50)
);
ALTER TABLE vendas.TB_TIPO_CLIENTE ADD PRIMARY KEY (id);

insert into vendas.TB_TIPO_CLIENTE values (0, 'NORMAL');
insert into vendas.TB_TIPO_CLIENTE values (1, 'COMPRADOR');

ALTER TABLE VENDAS.TB_CLIENTE ADD ID_TIPO_CLIENTE INTEGER DEFAULT 0;
ALTER TABLE vendas.TB_CLIENTE ADD CONSTRAINT ID_TIPO_CLIENTE FOREIGN KEY (ID_TIPO_CLIENTE) REFERENCES vendas.TB_TIPO_CLIENTE (id);

alter table vendas.tb_item_pedido add item_encomendado boolean default false;
alter table vendas.tb_cliente drop prospeccao_finalizada;
alter table vendas.tb_item_pedido add quantidade_recepcionada integer default 0;

INSERT INTO vendas.tb_perfil_acesso values(14, 'MANUTENCAO_ESTOQUE');

alter table vendas.tb_usuario drop vendedor;
create table vendas.tb_comentario_representada (
	id integer not null,
	data_inclusao date not null,
	conteudo varchar(800) not null,
	id_usuario integer not null,
	id_representada integer not null
);

ALTER TABLE vendas.tb_comentario_representada ADD PRIMARY KEY (id);
ALTER TABLE vendas.tb_comentario_representada ADD CONSTRAINT id_usuario FOREIGN KEY (id_usuario) REFERENCES vendas.tb_usuario (id);
ALTER TABLE vendas.tb_comentario_representada ADD CONSTRAINT id_representada FOREIGN KEY (id_representada) REFERENCES vendas.tb_representada (id);

create sequence vendas.seq_comentario_representada_id increment by 1 minvalue 1 no maxvalue start with 1;

drop table vendas.tb_remuneracao;
drop sequence vendas.seq_remuneracao_id;

create table vendas.tb_comissao (
	id integer not null,
	id_vendedor integer default null,
	id_material integer default null,
	id_forma_material integer default null,
	valor numeric (2,2) default 0,
	data_inicio timestamp not null,
	data_fim timestamp default null
);

ALTER TABLE vendas.tb_comissao ADD PRIMARY KEY (id);
create index idx_comissao_id_vendedor on vendas.tb_comissao (id_vendedor);
create index idx_comissao_id_forma_material on vendas.tb_comissao (id_forma_material);
create index idx_comissao_id_material on vendas.tb_comissao (id_material);
create sequence vendas.seq_comissao_id increment by 1 minvalue 1 no maxvalue start with 1;

alter table vendas.tb_comissao rename column valor to aliquota_revenda;
alter table vendas.tb_comissao add aliquota_representacao numeric(2, 2) default null;

alter table vendas.tb_item_pedido add comissao numeric (2,2) default 0;
update vendas.tb_perfil_acesso set descricao = 'CADASTRO_PEDIDO_VENDAS' WHERE ID = 9;

alter table vendas.tb_item_pedido drop comissao;
alter table vendas.tb_item_pedido add aliquota_comissao numeric (5,5) default 0;
alter table vendas.tb_item_pedido add preco_custo numeric (9,2) default 0;
alter table vendas.tb_item_pedido add valor_comissionado numeric (9,2) default 0;

INSERT INTO vendas.tb_perfil_acesso values(15, 'RECEPCAO_COMPRA');

alter table vendas.tb_pedido add aliquota_comissao numeric (5,5) default 0;

insert into vendas.tb_situacao_pedido values (9, 'COMPRA EM ANDAMENTO');
insert into vendas.tb_situacao_pedido values (10, 'ITEM AGUARDANDO MATERIAL');
insert into vendas.tb_situacao_pedido values (11, 'REVENDA PARCIALMENTE RESERVADA');	

drop table IF EXISTS vendas.tb_limite_minimo_estoque cascade ;
alter table vendas.tb_item_estoque drop column if exists id_limite_minimo_estoque;

alter table vendas.tb_item_estoque add quantidade_minima integer default null;
alter table vendas.tb_item_estoque add margem_minima_lucro numeric(5, 2) default null;
alter table vendas.tb_item_pedido add preco_minimo numeric(9, 2) default null;

alter table vendas.tb_representada add aliquota_icms numeric(5, 2) default null;
alter table vendas.tb_item_estoque add preco_medio_fatoricms numeric(9, 2) default null;

alter table vendas.tb_representada  ALTER COLUMN comissao SET DATA TYPE numeric(5, 3);

alter table vendas.tb_pedido drop column data_inclusao;
alter table vendas.tb_pedido add prazo_entrega integer default null;
alter table vendas.tb_cliente add documento_estrangeiro varchar(15);
alter table vendas.tb_cliente add email_cobranca varchar(250) default null;
alter table vendas.tb_item_pedido add id_pedido_compra integer default null;
create index idx_item_pedido_id_pedido_compra on vendas.tb_item_pedido (id_pedido_compra);
alter table vendas.tb_item_pedido add id_pedido_venda integer default null;
alter table vendas.tb_item_pedido add prazo_entrega integer default null;
alter table vendas.tb_pedido add numero_nf varchar(8) default null;
alter table vendas.tb_pedido add data_emissao_nf date default null;
alter table vendas.tb_pedido add data_vencimento_nf date default null;
alter table vendas.tb_pedido add valor_total_nf numeric(9,2) default null;
alter table vendas.tb_pedido add valor_parcela_nf numeric(9,2) default null;
alter table vendas.tb_pedido add numero_coleta varchar(8) default null;
alter table vendas.tb_pedido add numero_volumes integer default null;
create table vendas.tb_tipo_cst (
	id integer not null,
	descricao varchar(50) not null
);
ALTER TABLE vendas.tb_tipo_cst ADD PRIMARY KEY (id);
insert into vendas.tb_tipo_cst values (0, 'PRODUTO_NACIONAL');
insert into vendas.tb_tipo_cst values (1, 'IMPORTADO_DIRETAMENTE');
insert into vendas.tb_tipo_cst values (2, 'IMPORTADO_ADQUIRIDO_MERCADO_INTERNO');
insert into vendas.tb_tipo_cst values (3, 'PRODUTO_NACIONAL_IMPORTACAO_40_70');
insert into vendas.tb_tipo_cst values (4, 'PRODUTO_NACIONAL_IMPORTACAO_ATE_40');
alter table vendas.tb_item_pedido add id_tipo_cst integer default null;
create index idx_item_pedido_tipo_cst on vendas.tb_item_pedido (id_tipo_cst);
alter table vendas.tb_item_pedido add ncm varchar(15) default null;


alter table vendas.tb_material ALTER COLUMN sigla SET DATA TYPE varchar(20);
alter table vendas.tb_item_estoque add ncm varchar(15) default null;

alter table vendas.tb_representada add telefone varchar(15) default null;
UPDATE vendas.tb_representada SET telefone = '1130219600' where nome_fantasia = 'PLASTECNO MATRIZ';
insert into vendas.tb_configuracao_sistema (parametro, valor) values ('PERCENTUAL_COFINS', '3');
insert into vendas.tb_configuracao_sistema (parametro, valor) values ('PERCENTUAL_PIS', '0.65');
insert into vendas.tb_configuracao_sistema (parametro, valor) values ('REGIME_TRIBUTACAO', '3');
insert into vendas.tb_configuracao_sistema (parametro, valor) values ('DIRETORIO_XML_NFE', 'C:\\NFe');
insert into vendas.tb_configuracao_sistema (parametro, valor) values ('CNAE', '4689399');
ALTER TABLE vendas.tb_configuracao_sistema ADD PRIMARY KEY (parametro);
create index idx_transportadora_cnpj on vendas.tb_transportadora (cnpj);
create index idx_cliente_cnpj on vendas.tb_cliente (cnpj);
alter table vendas.tb_logradouro ALTER COLUMN numero SET DATA TYPE varchar(20);
insert into vendas.tb_configuracao_sistema (parametro, valor) values ('CODIGO_MUNICIPIO_GERADOR_ICMS', '3550308');
create table vendas.tb_nfe_pedido (
	numero integer not null,
	serie integer default null,
	modelo integer default null,
	numero_triang integer default null,
	id_pedido integer not null,
	xml_nfe text default null
);
ALTER TABLE vendas.tb_nfe_pedido ADD PRIMARY KEY (numero);
create index idx_nfe_pedido_id_pedido on vendas.tb_nfe_pedido (id_pedido);


create table vendas.tb_nfe_item_fracionado (
id integer not null,	
id_item_pedido integer not null,
	id_pedido integer not null,
	descricao varchar(350) not null,
	numero_item integer not null,
	numero_nfe integer not null,
	quantidade integer not null,
	quantidade_fracionada integer not null,
	valor_bruto numeric(9, 2)
);
ALTER TABLE vendas.tb_nfe_item_fracionado ADD PRIMARY KEY (id);
create index idx_nfe_item_fracionado_item_pedido on vendas.tb_nfe_item_fracionado (id_item_pedido);
create index idx_nfe_item_fracionado_numeroNFe on vendas.tb_nfe_item_fracionado (numero_nfe);
create index idx_nfe_item_fracionado_pedido on vendas.tb_nfe_item_fracionado (id_pedido);
create sequence vendas.seq_item_fracionado_id increment by 1 minvalue 1 no maxvalue start with 1;

insert into vendas.tb_perfil_acesso (id, descricao) values (16, 'FATURAMENTO');

create table vendas.tb_situacao_nfe (
	id integer not null,	
	descricao varchar(20) not null
);
ALTER TABLE vendas.tb_situacao_nfe ADD PRIMARY KEY (id);
insert into vendas.tb_situacao_nfe(id, descricao) values (0, 'INDEFINIDO');
insert into vendas.tb_situacao_nfe(id, descricao) values (1, 'EMITIDA');
insert into vendas.tb_situacao_nfe(id, descricao) values (2, 'CANCELADA');

ALTER TABLE vendas.tb_nfe_pedido add id_situacao_nfe integer default null;
create index idx_nfe_pedido_id_pedido on vendas.tb_nfe_pedido (id_pedido);

create table vendas.tb_tipo_nfe (
	id integer not null,	
	descricao varchar(20) not null
);
ALTER TABLE vendas.tb_tipo_nfe ADD PRIMARY KEY (id);
insert into vendas.tb_tipo_nfe(id, descricao) values (0, 'INDEFINIDO');
insert into vendas.tb_tipo_nfe(id, descricao) values (1, 'SAIDA');
insert into vendas.tb_tipo_nfe(id, descricao) values (2, 'ENTRADA');
insert into vendas.tb_tipo_nfe(id, descricao) values (3, 'DEVOLUCAO');
insert into vendas.tb_tipo_nfe(id, descricao) values (4, 'TRIANGULARIZACAO');

ALTER TABLE vendas.tb_nfe_pedido add id_tipo_nfe integer default null;

ALTER TABLE vendas.tb_nfe_pedido RENAME column numero_triang TO numero_associado;

update vendas.tb_nfe_pedido set id_tipo_nfe = 0, id_situacao_nfe =0;

alter table vendas.tb_nfe_pedido add constraint id_tipo_nfe foreign key (id_tipo_nfe ) references vendas.tb_tipo_nfe (id);
alter table vendas.tb_nfe_pedido add constraint id_situacao_nfe foreign key (id_situacao_nfe ) references vendas.tb_situacao_nfe (id);

alter table vendas.tb_item_pedido add aliquota_comissao_representada numeric(5,5) default 0;
ALTER TABLE vendas.tb_item_pedido add valor_comissionado_representada numeric(9,2) default 0;

alter table vendas.tb_cliente add inscricao_suframa varchar(10) default null;


create table vendas.tb_situacao_duplicata (
	id integer not null,	
	descricao varchar(20) not null
);

ALTER TABLE vendas.tb_situacao_duplicata ADD PRIMARY KEY (id);
insert into vendas.tb_situacao_duplicata(id, descricao) values (0, 'A VENCER');
insert into vendas.tb_situacao_duplicata(id, descricao) values (1, 'VENCIDO');
insert into vendas.tb_situacao_duplicata(id, descricao) values (2, 'LIQUIDADO');

create table vendas.tb_nfe_duplicata (
	id integer not null,
	id_nfe_pedido integer not null,
	nome_cliente varchar(150) not null,
	data_vencimento date not null,
	valor numeric(10,2) not null,
	id_situacao_duplicata integer not null
);
ALTER TABLE vendas.tb_nfe_duplicata ADD PRIMARY KEY (id);
alter table vendas.tb_nfe_duplicata add constraint id_nfe_pedido foreign key (id_nfe_pedido) references vendas.tb_nfe_pedido (numero);
alter table vendas.tb_nfe_duplicata add constraint id_situacao_duplicata foreign key (id_situacao_duplicata) references vendas.tb_situacao_duplicata (id);
create sequence vendas.seq_nfe_duplicata_id increment by 1 minvalue 1 no maxvalue start with 1;

insert into vendas.tb_finalidade_pedido(id, descricao) values ('NOTA_ENTRADA', 'NOTA DE ENTRADA');
insert into vendas.tb_finalidade_pedido(id, descricao) values ('RETORNO', 'RETORNO');
insert into vendas.tb_finalidade_pedido(id, descricao) values ('DEVOLUCAO', 'DEVOLUÇÃO');
insert into vendas.tb_finalidade_pedido(id, descricao) values ('AMOSTRA_GRATIS', 'AMOSTRA GRÁTIS');
insert into vendas.tb_finalidade_pedido(id, descricao) values ('REMESSA_INDUSTRIALIZACAO', 'REMESSA P/ INDUSTRIALIZAÇÃO');
insert into vendas.tb_finalidade_pedido(id, descricao) values ('REMESSA_CONSERTO', 'REMESSA P/ CONSERTO');
insert into vendas.tb_finalidade_pedido(id, descricao) values ('REMESSA_ANALISE', 'REMESSA P/ ANÁLISE');
insert into vendas.tb_finalidade_pedido(id, descricao) values ('SIMPLES_REMESSA', 'SIMPLES REMESSA');
insert into vendas.tb_finalidade_pedido(id, descricao) values ('OUTRA_ENTRADA', 'OUTRA ENTRADA');
insert into vendas.tb_finalidade_pedido(id, descricao) values ('PRIMEIRA_NOTA_TRIANGULAR', 'PRIMEIRA NOTA TRIANGULAR');

ALTER TABLE vendas.tb_nfe_pedido add valor numeric(10,2) ;
ALTER TABLE vendas.tb_nfe_pedido add valor_icms numeric(10,2) ;
ALTER TABLE vendas.tb_nfe_pedido add data_emissao date ;
ALTER TABLE vendas.tb_nfe_pedido add nome_cliente varchar(250) ;

ALTER TABLE vendas.tb_logradouro_cliente add cep varchar (8);
ALTER TABLE vendas.tb_logradouro_cliente add endereco varchar (500);
ALTER TABLE vendas.tb_logradouro_cliente add numero varchar (20);
ALTER TABLE vendas.tb_logradouro_cliente add complemento varchar (250);
ALTER TABLE vendas.tb_logradouro_cliente add cidade varchar (50);
ALTER TABLE vendas.tb_logradouro_cliente add uf varchar (2);
ALTER TABLE vendas.tb_logradouro_cliente add pais varchar (50);
ALTER TABLE vendas.tb_logradouro_cliente add id_tipo_logradouro integer NOT NULL default 3;
ALTER TABLE vendas.tb_logradouro_cliente add codificado boolean DEFAULT true;
ALTER TABLE vendas.tb_logradouro_cliente add bairro varchar (50);

ALTER TABLE vendas.tb_logradouro_cliente add constraint id_tipo_logradouro foreign key (id_tipo_logradouro) references vendas.tb_tipo_logradouro (id);


CREATE TABLE vendas.tb_logradouro_pedido (
  id integer NOT NULL,
  id_pedido integer NOT NULL,
  cep character varying(8),
  endereco character varying(500),
  numero character varying(20),
  complemento character varying(250),
  bairro character varying(50),
  cidade character varying(50),
  uf character varying(2),
  pais character varying(50),
  id_tipo_logradouro integer NOT NULL DEFAULT 3,
  codificado boolean DEFAULT true,
  CONSTRAINT tb_logradouro_pedido_pkey PRIMARY KEY (id),
  CONSTRAINT id_pedido FOREIGN KEY (id_pedido) REFERENCES vendas.tb_pedido (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_tipo_logradouro FOREIGN KEY (id_tipo_logradouro) REFERENCES vendas.tb_tipo_logradouro (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE vendas.tb_logradouro_usuario (
  id integer NOT NULL,
  cep character varying(8),
  endereco character varying(500),
  numero character varying(20),
  complemento character varying(250),
  bairro character varying(50),
  cidade character varying(50),
  uf character varying(2),
  pais character varying(50),
  id_tipo_logradouro integer NOT NULL DEFAULT 3,
  codificado boolean DEFAULT true,
  CONSTRAINT tb_logradouro_usuario_pkey PRIMARY KEY (id),
  CONSTRAINT id_tipo_logradouro FOREIGN KEY (id_tipo_logradouro) REFERENCES vendas.tb_tipo_logradouro (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
alter table vendas.tb_usuario add id_logradouro_usuario integer default null;
ALTER TABLE vendas.tb_usuario add constraint id_logradouro_usuario  foreign key (id_logradouro_usuario ) references vendas.tb_logradouro_usuario  (id);

CREATE TABLE vendas.tb_logradouro_representada (
  id integer NOT NULL,
  cep character varying(8),
  endereco character varying(500),
  numero character varying(20),
  complemento character varying(250),
  bairro character varying(50),
  cidade character varying(50),
  uf character varying(2),
  pais character varying(50),
  id_tipo_logradouro integer NOT NULL DEFAULT 3,
  codificado boolean DEFAULT true,
  CONSTRAINT tb_logradouro_representada_pkey PRIMARY KEY (id),
  CONSTRAINT id_tipo_logradouro FOREIGN KEY (id_tipo_logradouro) REFERENCES vendas.tb_tipo_logradouro (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
alter table vendas.tb_representada add id_logradouro_representada integer default null;
ALTER TABLE vendas.tb_representada add constraint id_logradouro_representada foreign key (id_logradouro_representada) references vendas.tb_logradouro_representada (id);

create sequence vendas.seq_logradouro_cliente_id increment by 1 minvalue 1 no maxvalue start with 1;
create sequence vendas.seq_logradouro_pedido_id increment by 1 minvalue 1 no maxvalue start with 1;
create sequence vendas.seq_logradouro_usuario_id increment by 1 minvalue 1 no maxvalue start with 1;
create sequence vendas.seq_logradouro_representada_id increment by 1 minvalue 1 no maxvalue start with 1;


CREATE TABLE vendas.tb_logradouro_transportadora (
  id integer NOT NULL,
  cep character varying(8),
  endereco character varying(500),
  numero character varying(20),
  complemento character varying(250),
  bairro character varying(50),
  cidade character varying(50),
  uf character varying(2),
  pais character varying(50),
  id_tipo_logradouro integer NOT NULL DEFAULT 3,
  codificado boolean DEFAULT true,
  CONSTRAINT tb_logradouro_transportadora_pkey PRIMARY KEY (id),
  CONSTRAINT id_tipo_logradouro FOREIGN KEY (id_tipo_logradouro) REFERENCES vendas.tb_tipo_logradouro (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
alter table vendas.tb_transportadora add id_logradouro_transportadora integer default null;
ALTER TABLE vendas.tb_transportadora add constraint id_logradouro_transportadora foreign key (id_logradouro_transportadora) references vendas.tb_logradouro_transportadora (id);
create sequence vendas.seq_logradouro_transportadora_id increment by 1 minvalue 1 no maxvalue start with 1;


CREATE TABLE vendas.tb_logradouro_contato (
  id integer NOT NULL,
  cep character varying(8),
  endereco character varying(500),
  numero character varying(20),
  complemento character varying(250),
  bairro character varying(50),
  cidade character varying(50),
  uf character varying(2),
  pais character varying(50),
  id_tipo_logradouro integer NOT NULL DEFAULT 3,
  codificado boolean DEFAULT true,
  CONSTRAINT tb_logradouro_contato_pkey PRIMARY KEY (id),
  CONSTRAINT id_tipo_logradouro FOREIGN KEY (id_tipo_logradouro) REFERENCES vendas.tb_tipo_logradouro (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
alter table vendas.tb_contato add id_logradouro_contato integer default null;
ALTER TABLE vendas.tb_contato add constraint id_logradouro_contato foreign key (id_logradouro_contato) references vendas.tb_logradouro_contato (id);
create sequence vendas.seq_logradouro_contato_id increment by 1 minvalue 1 no maxvalue start with 1;


ALTER TABLE enderecamento.tb_cidade ALTER COLUMN cod_ibge DROP NOT NULL;

ALTER TABLE vendas.tb_transportadora drop constraint id_logradouro;
ALTER TABLE vendas.tb_contato drop constraint id_logradouro;
ALTER TABLE vendas.tb_transportadora drop id_logradouro;
ALTER TABLE vendas.tb_contato drop id_logradouro;
ALTER TABLE vendas.tb_logradouro_cliente drop constraint id_logradouro_cliente;

alter table vendas.tb_item_pedido add peso numeric(9,2) default 0;
alter table vendas.tb_pedido add observacao_producao varchar(800) default null;
insert into vendas.tb_situacao_pedido (id, descricao) values (12, 'ORCAMENTO DIGITACAO');

alter table vendas.tb_logradouro_cliente add codigo_municipio varchar(10) default null;
alter table vendas.tb_logradouro_contato add codigo_municipio varchar(10) default null;
alter table vendas.tb_logradouro_pedido add codigo_municipio varchar(10) default null;
alter table vendas.tb_logradouro_representada add codigo_municipio varchar(10) default null;
alter table vendas.tb_logradouro_transportadora  add codigo_municipio varchar(10) default null;
alter table vendas.tb_logradouro_usuario add codigo_municipio varchar(10) default null;
alter table vendas.tb_cliente ALTER COLUMN email SET DATA TYPE varchar(500);
alter table vendas.tb_cliente add data_nascimento date default null;
alter table vendas.tb_pedido add valor_frete numeric(10,2) default 0;
alter table vendas.tb_pedido add validade integer default 0;

insert into vendas.tb_situacao_pedido (id, descricao) values (13, 'ORCAMENTO ACEITO');
alter table vendas.tb_pedido add id_orcamento integer default null;
insert into vendas.tb_situacao_pedido (id, descricao) values (14, 'ORCAMENTO CANCELADO');
create index idx_pedido_id_orcamento on vendas.tb_pedido (id_orcamento);
alter table vendas.tb_contato alter column nome set data type varchar(80) ;

alter table vendas.tb_usuario add email_copia varchar(250) default null;


create table vendas.tb_tipo_pagamento (
	id integer not null,
	descricao varchar(50)
);

create table vendas.tb_pagamento (
	id integer not null,
	id_fornecedor integer default null,
	id_item_pedido integer default null,
	id_pedido integer default null,
	numero_nf integer default null,
	parcela integer default null,
	total_parcelas integer default null,
	valor_nf numeric(10, 2) default 0,
	valor numeric(10, 2) default 0,
	valor_credito_icms numeric(10, 2) default 0,
	modalidade_frete integer default null,
	data_emissao date default null,
	data_vencimento date not null,
	data_recebimento date default null,
	descricao varchar(200),
	quantidade_item integer default 0,
	quantidade_total integer default 0,
	sequencial_item integer default 0,
	id_tipo_pagamento integer not null,
	liquidado boolean default false,
	nome_fornecedor varchar(150) default null
);
ALTER TABLE vendas.tb_tipo_pagamento ADD PRIMARY KEY (id);
ALTER TABLE vendas.tb_pagamento ADD PRIMARY KEY (id);
ALTER TABLE vendas.tb_pagamento ADD CONSTRAINT id_tipo_pagamento FOREIGN KEY (id_tipo_pagamento ) REFERENCES vendas.tb_tipo_pagamento (id);

create sequence vendas.seq_pagamento_id increment by 1 minvalue 1 no maxvalue start with 1;

create index idx_pagamento_id_fornecedor on vendas.tb_pagamento (id_fornecedor);
create index idx_pagamento_id_item_pedido on vendas.tb_pagamento (id_item_pedido);
create index idx_pagamento_id_pedido on vendas.tb_pagamento (id_pedido);
create index idx_pagamento_numero_nf on vendas.tb_pagamento (numero_nf);

insert into vendas.tb_tipo_pagamento values (0, 'FOLHA_PAGAMENTO');
insert into vendas.tb_tipo_pagamento values (1, 'INSUMO');
insert into vendas.tb_tipo_pagamento values (2, 'DESPESAS_FIXAS');
insert into vendas.tb_tipo_pagamento values (3, 'ACAO_JUDICIAL');
insert into vendas.tb_tipo_pagamento values (4, 'EQUIPAMENTO');
insert into vendas.tb_tipo_pagamento values (5, 'EVENTUALIDADE');
insert into vendas.tb_tipo_pagamento values (6, 'IMPOSTO');

alter table vendas.tb_nfe_duplicata add id_cliente integer default null;
create index idx_nfe_duplicata_id_cliente on vendas.tb_nfe_duplicata (id_cliente);
alter table vendas.tb_nfe_duplicata add parcela integer default null;
alter table vendas.tb_nfe_duplicata add total_parcelas integer default null;
alter table vendas.tb_nfe_duplicata add codigo_banco varchar(5) default null;
alter table vendas.tb_nfe_duplicata add nome_banco varchar(30) default null;
create index idx_nfe_duplicata_codigo_banco on vendas.tb_nfe_duplicata (codigo_banco);

insert into vendas.tb_situacao_duplicata values (3, 'CARTORIO');
insert into vendas.tb_situacao_duplicata values (4, 'PROTESTADO');
insert into vendas.tb_situacao_duplicata values (5, 'A_VISTA');

insert into vendas.tb_tipo_pagamento values (7, 'PRESTACAO_SERVICO');
insert into vendas.tb_tipo_pagamento values (8, 'FRETE');
insert into vendas.tb_tipo_pagamento values (9, 'BENEFICIOS_FOLHA');
insert into vendas.tb_tipo_pagamento values (10, 'ICMS');
insert into vendas.tb_tipo_pagamento values (11, 'BANCOS_EMPRESTIMOS_JUROS');

alter table vendas.tb_usuario add comissionado_simples boolean default false;

create table vendas.tb_tipo_operacao_estoque (
	id integer not null,
	descricao varchar(50) not null
);
ALTER TABLE vendas.tb_tipo_operacao_estoque ADD PRIMARY KEY (id);

insert into vendas.tb_tipo_operacao_estoque values (0, 'ENTRADA_PEDIDO_COMPRA');
insert into vendas.tb_tipo_operacao_estoque values (1, 'SAIDA_PEDIDO_VENDA');
insert into vendas.tb_tipo_operacao_estoque values (2, 'SAIDA_DEVOLUCAO_COMPRA');
insert into vendas.tb_tipo_operacao_estoque values (3, 'ENTRADA_DEVOLUCAO_VENDA');
insert into vendas.tb_tipo_operacao_estoque values (4, 'ENTRADA_MANUAL');
insert into vendas.tb_tipo_operacao_estoque values (5, 'SAIDA_MANUAL');
insert into vendas.tb_tipo_operacao_estoque values (6, 'ALTERACAO_MANUAL_VALOR');
insert into vendas.tb_tipo_operacao_estoque values (7, 'CONFIGURACAO_MANUAL');

create table vendas.tb_registro_estoque (
	id integer not null,
	id_item_estoque integer not null,
	id_item_pedido integer default null,
	id_pedido integer default null,
	id_tipo_operacao integer not null,
	id_usuario integer default null,
	data_operacao timestamp not null,
	nome_usuario varchar(50) default null,
	quantidade_anterior integer not null,
	quantidade_item integer default null,
	quantidade_posterior integer not null,
	sequencial_item_pedido integer default null,
	valor_anterior numeric(9, 2) default null,
	valor_posterior numeric(9, 2) default null
);
ALTER TABLE vendas.tb_registro_estoque ADD PRIMARY KEY (id);
ALTER TABLE vendas.tb_registro_estoque ADD CONSTRAINT id_tipo_operacao FOREIGN KEY (id_tipo_operacao) REFERENCES vendas.tb_tipo_operacao_estoque(id);
create index idx_registro_estoque_id_pedido on vendas.tb_registro_estoque (id_pedido);
create index idx_registro_estoque_id_usuario on vendas.tb_registro_estoque (id_usuario);
create index idx_registro_estoque_id_item_estoque on vendas.tb_registro_estoque (id_item_estoque);
create index idx_registro_estoque_id_item_pedido on vendas.tb_registro_estoque (id_item_pedido);

create sequence vendas.seq_registro_estoque_id increment by 1 minvalue 1 no maxvalue start with 1;

INSERT INTO VENDAS.TB_CONFIGURACAO_SISTEMA VALUES ('EXPIRACAO_REGISTRO_ESTOQUE_MESES', '4');

drop table IF EXISTS vendas.tb_cfop cascade;

create table vendas.tb_cfop (
	codigo integer not null,
	descricao varchar(400) not null 
);
ALTER TABLE vendas.tb_cfop ADD PRIMARY KEY (codigo);

insert into vendas.tb_cfop (codigo, descricao) values (1101, 'Compra p/ industrializacao ou producao rural');
insert into vendas.tb_cfop (codigo, descricao) values (1102, 'Compra p/ comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (1111, 'Compra p/ industrializacao de mercadoria recebida anteriormente em consignacao industrial');
insert into vendas.tb_cfop (codigo, descricao) values (1113, 'Compra p/ comercializacao, de mercadoria recebida anteriormente em consignacao mercantil');
insert into vendas.tb_cfop (codigo, descricao) values (1116, 'Compra p/ industrializacao ou producao rural originada de encomenda p/ recebimento futuro');
insert into vendas.tb_cfop (codigo, descricao) values (1117, 'Compra p/ comercializacao originada de encomenda p/ recebimento futuro');
insert into vendas.tb_cfop (codigo, descricao) values (1118, 'Compra de mercadoria p/ comercializacao pelo adquirente originario, entregue pelo vendedor remetente ao destinatario, em venda a ordem.');
insert into vendas.tb_cfop (codigo, descricao) values (1120, 'Compra p/ industrializacao, em venda a ordem, ja recebida do vendedor remetente');
insert into vendas.tb_cfop (codigo, descricao) values (1121, 'Compra p/ comercializacao, em venda a ordem, ja recebida do vendedor remetente');
insert into vendas.tb_cfop (codigo, descricao) values (1122, 'Compra p/ industrializacao em que a mercadoria foi remetida pelo fornecedor ao industrializador sem transitar pelo estabelecimento adquirente');
insert into vendas.tb_cfop (codigo, descricao) values (1124, 'Industrializacao efetuada por outra empresa');
insert into vendas.tb_cfop (codigo, descricao) values (1125, 'Industrializacao efetuada por outra empresa quando a mercadoria remetida p/ utilizacao no processo de industrializacao nao transitou pelo estabelecimento adquirente da mercadoria');
insert into vendas.tb_cfop (codigo, descricao) values (1126, 'Compra p/ utilizacao na prestacao de servico sujeita ao ICMS');
insert into vendas.tb_cfop (codigo, descricao) values (1128, 'Compra p/ utilizacao na prestacao de servico sujeita ao ISSQN');
insert into vendas.tb_cfop (codigo, descricao) values (1151, 'Transferencia p/ industrializacao ou producao rural');
insert into vendas.tb_cfop (codigo, descricao) values (1152, 'Transferencia p/ comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (1153, 'Transferencia de energia eletrica p/ distribuicao');
insert into vendas.tb_cfop (codigo, descricao) values (1154, 'Transferencia p/ utilizacao na prestacao de servico');
insert into vendas.tb_cfop (codigo, descricao) values (1201, 'Devolucao de venda de producao do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (1202, 'Devolucao de venda de mercadoria adquirida ou recebida de terceiros');
insert into vendas.tb_cfop (codigo, descricao) values (1203, 'Devolucao de venda de producao do estabelecimento, destinada a ZFM ou ALC');
insert into vendas.tb_cfop (codigo, descricao) values (1204, 'Devolucao de venda de mercadoria adquirida ou recebida de terceiros, destinada a ZFM ou ALC');
insert into vendas.tb_cfop (codigo, descricao) values (1205, 'Anulacao de valor relativo a prestacao de servico de comunicacao');
insert into vendas.tb_cfop (codigo, descricao) values (1206, 'Anulacao de valor relativo a prestacao de servico de transporte');
insert into vendas.tb_cfop (codigo, descricao) values (1207, 'Anulacao de valor relativo a venda de energia eletrica');
insert into vendas.tb_cfop (codigo, descricao) values (1208, 'Devolucao de producao do estabelecimento, remetida em transferencia');
insert into vendas.tb_cfop (codigo, descricao) values (1209, 'Devolucao de mercadoria adquirida ou recebida de terceiros, remetida em transferencia');
insert into vendas.tb_cfop (codigo, descricao) values (1212, 'Devolucao de venda no mercado interno de mercadoria industrializada e insumo importado sob o Regime Aduaneiro Especial de Entreposto Industrial (Recof-Sped)');
insert into vendas.tb_cfop (codigo, descricao) values (1251, 'Compra de energia eletrica p/ distribuicao ou comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (1252, 'Compra de energia eletrica por estabelecimento industrial');
insert into vendas.tb_cfop (codigo, descricao) values (1253, 'Compra de energia eletrica por estabelecimento comercial');
insert into vendas.tb_cfop (codigo, descricao) values (1254, 'Compra de energia eletrica por estabelecimento prestador de servico de transporte');
insert into vendas.tb_cfop (codigo, descricao) values (1255, 'Compra de energia eletrica por estabelecimento prestador de servico de comunicacao');
insert into vendas.tb_cfop (codigo, descricao) values (1256, 'Compra de energia eletrica por estabelecimento de produtor rural');
insert into vendas.tb_cfop (codigo, descricao) values (1257, 'Compra de energia eletrica p/ consumo por demanda contratada');
insert into vendas.tb_cfop (codigo, descricao) values (1301, 'Aquisicao de servico de comunicacao p/ execucao de servico da mesma natureza');
insert into vendas.tb_cfop (codigo, descricao) values (1302, 'Aquisicao de servico de comunicacao por estabelecimento industrial');
insert into vendas.tb_cfop (codigo, descricao) values (1303, 'Aquisicao de servico de comunicacao por estabelecimento comercial');
insert into vendas.tb_cfop (codigo, descricao) values (1304, 'Aquisicao de servico de comunicacao por estabelecimento de prestador de servico de transporte');
insert into vendas.tb_cfop (codigo, descricao) values (1305, 'Aquisicao de servico de comunicacao por estabelecimento de geradora ou de distribuidora de energia eletrica');
insert into vendas.tb_cfop (codigo, descricao) values (1306, 'Aquisicao de servico de comunicacao por estabelecimento de produtor rural');
insert into vendas.tb_cfop (codigo, descricao) values (1351, 'Aquisicao de servico de transporte p/ execucao de servico da mesma natureza');
insert into vendas.tb_cfop (codigo, descricao) values (1352, 'Aquisicao de servico de transporte por estabelecimento industrial');
insert into vendas.tb_cfop (codigo, descricao) values (1353, 'Aquisicao de servico de transporte por estabelecimento comercial');
insert into vendas.tb_cfop (codigo, descricao) values (1354, 'Aquisicao de servico de transporte por estabelecimento de prestador de servico de comunicacao');
insert into vendas.tb_cfop (codigo, descricao) values (1355, 'Aquisicao de servico de transporte por estabelecimento de geradora ou de distribuidora de energia eletrica');
insert into vendas.tb_cfop (codigo, descricao) values (1356, 'Aquisicao de servico de transporte por estabelecimento de produtor rural');
insert into vendas.tb_cfop (codigo, descricao) values (1360, 'Aquisicao de servico de transporte por contribuinte-substituto em relacao ao servico de transporte');
insert into vendas.tb_cfop (codigo, descricao) values (1401, 'Compra p/ industrializacao ou producao rural de mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (1403, 'Compra p/ comercializacao em operacao com mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (1406, 'Compra de bem p/ o ativo imobilizado cuja mercadoria esta sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (1407, 'Compra de mercadoria p/ uso ou consumo cuja mercadoria esta sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (1408, 'Transferencia p/ industrializacao ou producao rural de mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (1409, 'Transferencia p/ comercializacao em operacao com mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (1410, 'Devolucao de venda de mercadoria, de producao do estabelecimento, sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (1411, 'Devolucao de venda de mercadoria adquirida ou recebida de terceiros em operacao com mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (1414, 'Retorno de mercadoria de producao do estabelecimento, remetida p/ venda fora do estabelecimento, sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (1415, 'Retorno de mercadoria adquirida ou recebida de terceiros, remetida p/ venda fora do estabelecimento em operacao com mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (1451, 'Retorno de animal do estabelecimento produtor');
insert into vendas.tb_cfop (codigo, descricao) values (1452, 'Retorno de insumo nao utilizado na producao');
insert into vendas.tb_cfop (codigo, descricao) values (1501, 'Entrada de mercadoria recebida com fim especifico de exportacao');
insert into vendas.tb_cfop (codigo, descricao) values (1503, 'Entrada decorrente de devolucao de produto, de fabricacao do estabelecimento, remetido com fim especifico de exportacao');
insert into vendas.tb_cfop (codigo, descricao) values (1504, 'Entrada decorrente de devolucao de mercadoria remetida com fim especifico de exportacao, adquirida ou recebida de terceiros');
insert into vendas.tb_cfop (codigo, descricao) values (1505, 'Entrada decorrente de devolucao simbolica de mercadoria remetida p/ formacao de lote de exportacao, de produto industrializado ou produzido pelo proprio estabelecimento.');
insert into vendas.tb_cfop (codigo, descricao) values (1506, 'Entrada decorrente de devolucao simbolica de mercadoria, adquirida ou recebida de terceiros, remetida p/ formacao de lote de exportacao.');
insert into vendas.tb_cfop (codigo, descricao) values (1551, 'Compra de bem p/ o ativo imobilizado');
insert into vendas.tb_cfop (codigo, descricao) values (1552, 'Transferencia de bem do ativo imobilizado');
insert into vendas.tb_cfop (codigo, descricao) values (1553, 'Devolucao de venda de bem do ativo imobilizado');
insert into vendas.tb_cfop (codigo, descricao) values (1554, 'Retorno de bem do ativo imobilizado remetido p/ uso fora do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (1555, 'Entrada de bem do ativo imobilizado de terceiro, remetido p/ uso no estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (1556, 'Compra de material p/ uso ou consumo');
insert into vendas.tb_cfop (codigo, descricao) values (1557, 'Transferencia de material p/ uso ou consumo');
insert into vendas.tb_cfop (codigo, descricao) values (1601, 'Recebimento, por transferencia, de credito de ICMS');
insert into vendas.tb_cfop (codigo, descricao) values (1602, 'Recebimento, por transferencia, de saldo credor do ICMS, de outro estabelecimento da mesma empresa, p/ compensacao de saldo devedor do imposto. ');
insert into vendas.tb_cfop (codigo, descricao) values (1603, 'Ressarcimento de ICMS retido por substituicao tributaria');
insert into vendas.tb_cfop (codigo, descricao) values (1604, 'Lancamento do credito relativo a compra de bem p/ o ativo imobilizado');
insert into vendas.tb_cfop (codigo, descricao) values (1605, 'Recebimento, por transferencia, de saldo devedor do ICMS de outro estabelecimento da mesma empresa');
insert into vendas.tb_cfop (codigo, descricao) values (1651, 'Compra de combustivel ou lubrificante p/ industrializacao subsequente');
insert into vendas.tb_cfop (codigo, descricao) values (1652, 'Compra de combustivel ou lubrificante p/ comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (1653, 'Compra de combustivel ou lubrificante por consumidor ou usuario final');
insert into vendas.tb_cfop (codigo, descricao) values (1658, 'Transferencia de combustivel ou lubrificante p/ industrializacao');
insert into vendas.tb_cfop (codigo, descricao) values (1659, 'Transferencia de combustivel ou lubrificante p/ comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (1660, 'Devolucao de venda de combustivel ou lubrificante destinados a industrializacao subsequente');
insert into vendas.tb_cfop (codigo, descricao) values (1661, 'Devolucao de venda de combustivel ou lubrificante destinados a comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (1662, 'Devolucao de venda de combustivel ou lubrificante destinados a consumidor ou usuario final');
insert into vendas.tb_cfop (codigo, descricao) values (1663, 'Entrada de combustivel ou lubrificante p/ armazenagem');
insert into vendas.tb_cfop (codigo, descricao) values (1664, 'Retorno de combustivel ou lubrificante remetidos p/ armazenagem');
insert into vendas.tb_cfop (codigo, descricao) values (1901, 'Entrada p/ industrializacao por encomenda');
insert into vendas.tb_cfop (codigo, descricao) values (1902, 'Retorno de mercadoria remetida p/ industrializacao por encomenda');
insert into vendas.tb_cfop (codigo, descricao) values (1903, 'Entrada de mercadoria remetida p/ industrializacao e nao aplicada no referido processo');
insert into vendas.tb_cfop (codigo, descricao) values (1904, 'Retorno de remessa p/ venda fora do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (1905, 'Entrada de mercadoria recebida p/ deposito em deposito fechado ou armazem geral');
insert into vendas.tb_cfop (codigo, descricao) values (1906, 'Retorno de mercadoria remetida p/ deposito fechado ou armazem geral');
insert into vendas.tb_cfop (codigo, descricao) values (1907, 'Retorno simbolico de mercadoria remetida p/ deposito fechado ou armazem geral');
insert into vendas.tb_cfop (codigo, descricao) values (1908, 'Entrada de bem por conta de contrato de comodato');
insert into vendas.tb_cfop (codigo, descricao) values (1909, 'Retorno de bem remetido por conta de contrato de comodato');
insert into vendas.tb_cfop (codigo, descricao) values (1910, 'Entrada de bonificacao, doacao ou brinde');
insert into vendas.tb_cfop (codigo, descricao) values (1911, 'Entrada de amostra gratis');
insert into vendas.tb_cfop (codigo, descricao) values (1912, 'Entrada de mercadoria ou bem recebido p/ demonstracao');
insert into vendas.tb_cfop (codigo, descricao) values (1913, 'Retorno de mercadoria ou bem remetido p/ demonstracao');
insert into vendas.tb_cfop (codigo, descricao) values (1914, 'Retorno de mercadoria ou bem remetido p/ exposicao ou feira');
insert into vendas.tb_cfop (codigo, descricao) values (1915, 'Entrada de mercadoria ou bem recebido p/ conserto ou reparo');
insert into vendas.tb_cfop (codigo, descricao) values (1916, 'Retorno de mercadoria ou bem remetido p/ conserto ou reparo');
insert into vendas.tb_cfop (codigo, descricao) values (1917, 'Entrada de mercadoria recebida em consignacao mercantil ou industrial');
insert into vendas.tb_cfop (codigo, descricao) values (1918, 'Devolucao de mercadoria remetida em consignacao mercantil ou industrial');
insert into vendas.tb_cfop (codigo, descricao) values (1919, 'Devolucao simbolica de mercadoria vendida ou utilizada em processo industrial, remetida anteriormente em consignacao mercantil ou industrial');
insert into vendas.tb_cfop (codigo, descricao) values (1920, 'Entrada de vasilhame ou sacaria');
insert into vendas.tb_cfop (codigo, descricao) values (1921, 'Retorno de vasilhame ou sacaria');
insert into vendas.tb_cfop (codigo, descricao) values (1922, 'Lancamento efetuado a titulo de simples faturamento decorrente de compra p/ recebimento futuro');
insert into vendas.tb_cfop (codigo, descricao) values (1923, 'Entrada de mercadoria recebida do vendedor remetente, em venda a ordem');
insert into vendas.tb_cfop (codigo, descricao) values (1924, 'Entrada p/ industrializacao por conta e ordem do adquirente da mercadoria, quando esta nao transitar pelo estabelecimento do adquirente');
insert into vendas.tb_cfop (codigo, descricao) values (1925, 'Retorno de mercadoria remetida p/ industrializacao por conta e ordem do adquirente da mercadoria, quando esta nao transitar pelo estabelecimento do adquirente');
insert into vendas.tb_cfop (codigo, descricao) values (1926, 'Lancamento efetuado a titulo de reclassificacao de mercadoria decorrente de formacao de kit ou de sua desagregacao');
insert into vendas.tb_cfop (codigo, descricao) values (1931, 'Lancamento efetuado pelo tomador do servico de transporte, quando a responsabilidade de retencao do imposto for atribuida ao remetente ou alienante da mercadoria, pelo servico de transporte realizado por transportador autonomo ou por transportador nao-inscrito na UF onde se tenha iniciado o servico.');
insert into vendas.tb_cfop (codigo, descricao) values (1932, 'Aquisicao de servico de transporte iniciado em UF diversa daquela onde esteja inscrito o prestador');
insert into vendas.tb_cfop (codigo, descricao) values (1933, 'Aquisicao de servico tributado pelo Imposto sobre Servicos de Qualquer Natureza');
insert into vendas.tb_cfop (codigo, descricao) values (1934, 'Entrada simbolica de mercadoria recebida p/ deposito fechado ou armazem geral');
insert into vendas.tb_cfop (codigo, descricao) values (1949, 'Outra entrada de mercadoria ou prestacao de servico nao especificada');
insert into vendas.tb_cfop (codigo, descricao) values (2101, 'Compra p/ industrializacao ou producao rural');
insert into vendas.tb_cfop (codigo, descricao) values (2102, 'Compra p/ comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (2111, 'Compra p/ industrializacao de mercadoria recebida anteriormente em consignacao industrial');
insert into vendas.tb_cfop (codigo, descricao) values (2113, 'Compra p/ comercializacao, de mercadoria recebida anteriormente em consignacao mercantil');
insert into vendas.tb_cfop (codigo, descricao) values (2116, 'Compra p/ industrializacao ou producao rural originada de encomenda p/ recebimento futuro');
insert into vendas.tb_cfop (codigo, descricao) values (2117, 'Compra p/ comercializacao originada de encomenda p/ recebimento futuro');
insert into vendas.tb_cfop (codigo, descricao) values (2118, 'Compra de mercadoria p/ comercializacao pelo adquirente originario, entregue pelo vendedor remetente ao destinatario, em venda a ordem');
insert into vendas.tb_cfop (codigo, descricao) values (2120, 'Compra p/ industrializacao, em venda a ordem, ja recebida do vendedor remetente');
insert into vendas.tb_cfop (codigo, descricao) values (2121, 'Compra p/ comercializacao, em venda a ordem, ja recebida do vendedor remetente');
insert into vendas.tb_cfop (codigo, descricao) values (2122, 'Compra p/ industrializacao em que a mercadoria foi remetida pelo fornecedor ao industrializador sem transitar pelo estabelecimento adquirente');
insert into vendas.tb_cfop (codigo, descricao) values (2124, 'Industrializacao efetuada por outra empresa');
insert into vendas.tb_cfop (codigo, descricao) values (2125, 'Industrializacao efetuada por outra empresa quando a mercadoria remetida p/ utilizacao no processo de industrializacao nao transitou pelo estabelecimento adquirente da mercadoria');
insert into vendas.tb_cfop (codigo, descricao) values (2126, 'Compra p/ utilizacao na prestacao de servico sujeita ao ICMS');
insert into vendas.tb_cfop (codigo, descricao) values (2128, 'Compra p/ utilizacao na prestacao de servico sujeita ao ISSQN');
insert into vendas.tb_cfop (codigo, descricao) values (2151, 'Transferencia p/ industrializacao ou producao rural');
insert into vendas.tb_cfop (codigo, descricao) values (2152, 'Transferencia p/ comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (2153, 'Transferencia de energia eletrica p/ distribuicao');
insert into vendas.tb_cfop (codigo, descricao) values (2154, 'Transferencia p/ utilizacao na prestacao de servico');
insert into vendas.tb_cfop (codigo, descricao) values (2201, 'Devolucao de venda de producao do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (2202, 'Devolucao de venda de mercadoria adquirida ou recebida de terceiros');
insert into vendas.tb_cfop (codigo, descricao) values (2203, 'Devolucao de venda de producao do estabelecimento destinada a ZFM ou ALC');
insert into vendas.tb_cfop (codigo, descricao) values (2204, 'Devolucao de venda de mercadoria adquirida ou recebida de terceiros, destinada a ZFM ou ALC');
insert into vendas.tb_cfop (codigo, descricao) values (2205, 'Anulacao de valor relativo a prestacao de servico de comunicacao');
insert into vendas.tb_cfop (codigo, descricao) values (2206, 'Anulacao de valor relativo a prestacao de servico de transporte');
insert into vendas.tb_cfop (codigo, descricao) values (2207, 'Anulacao de valor relativo a venda de energia eletrica');
insert into vendas.tb_cfop (codigo, descricao) values (2208, 'Devolucao de producao do estabelecimento, remetida em transferencia.');
insert into vendas.tb_cfop (codigo, descricao) values (2209, 'Devolucao de mercadoria adquirida ou recebida de terceiros e remetida em transferencia');
insert into vendas.tb_cfop (codigo, descricao) values (2212, 'Devolucao de venda no mercado interno de mercadoria industrializada e insumo importado sob o Regime Aduaneiro Especial de Entreposto Industrial (Recof-Sped)');
insert into vendas.tb_cfop (codigo, descricao) values (2251, 'Compra de energia eletrica p/ distribuicao ou comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (2252, 'Compra de energia eletrica por estabelecimento industrial');
insert into vendas.tb_cfop (codigo, descricao) values (2253, 'Compra de energia eletrica por estabelecimento comercial');
insert into vendas.tb_cfop (codigo, descricao) values (2254, 'Compra de energia eletrica por estabelecimento prestador de servico de transporte');
insert into vendas.tb_cfop (codigo, descricao) values (2255, 'Compra de energia eletrica por estabelecimento prestador de servico de comunicacao');
insert into vendas.tb_cfop (codigo, descricao) values (2256, 'Compra de energia eletrica por estabelecimento de produtor rural');
insert into vendas.tb_cfop (codigo, descricao) values (2257, 'Compra de energia eletrica p/ consumo por demanda contratada');
insert into vendas.tb_cfop (codigo, descricao) values (2301, 'Aquisicao de servico de comunicacao p/ execucao de servico da mesma natureza');
insert into vendas.tb_cfop (codigo, descricao) values (2302, 'Aquisicao de servico de comunicacao por estabelecimento industrial');
insert into vendas.tb_cfop (codigo, descricao) values (2303, 'Aquisicao de servico de comunicacao por estabelecimento comercial');
insert into vendas.tb_cfop (codigo, descricao) values (2304, 'Aquisicao de servico de comunicacao por estabelecimento de prestador de servico de transporte');
insert into vendas.tb_cfop (codigo, descricao) values (2305, 'Aquisicao de servico de comunicacao por estabelecimento de geradora ou de distribuidora de energia eletrica');
insert into vendas.tb_cfop (codigo, descricao) values (2306, 'Aquisicao de servico de comunicacao por estabelecimento de produtor rural');
insert into vendas.tb_cfop (codigo, descricao) values (2351, 'Aquisicao de servico de transporte p/ execucao de servico da mesma natureza');
insert into vendas.tb_cfop (codigo, descricao) values (2352, 'Aquisicao de servico de transporte por estabelecimento industrial');
insert into vendas.tb_cfop (codigo, descricao) values (2353, 'Aquisicao de servico de transporte por estabelecimento comercial');
insert into vendas.tb_cfop (codigo, descricao) values (2354, 'Aquisicao de servico de transporte por estabelecimento de prestador de servico de comunicacao');
insert into vendas.tb_cfop (codigo, descricao) values (2355, 'Aquisicao de servico de transporte por estabelecimento de geradora ou de distribuidora de energia eletrica');
insert into vendas.tb_cfop (codigo, descricao) values (2356, 'Aquisicao de servico de transporte por estabelecimento de produtor rural');
insert into vendas.tb_cfop (codigo, descricao) values (2401, 'Compra p/ industrializacao ou producao rural de mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (2403, 'Compra p/ comercializacao em operacao com mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (2406, 'Compra de bem p/ o ativo imobilizado cuja mercadoria esta sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (2407, 'Compra de mercadoria p/ uso ou consumo cuja mercadoria esta sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (2408, 'Transferencia p/ industrializacao ou producao rural de mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (2409, 'Transferencia p/ comercializacao em operacao com mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (2410, 'Devolucao de venda de producao do estabelecimento, quando o produto sujeito a ST');
insert into vendas.tb_cfop (codigo, descricao) values (2411, 'Devolucao de venda de mercadoria adquirida ou recebida de terceiros em operacao com mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (2414, 'Retorno de producao do estabelecimento, remetida p/ venda fora do estabelecimento, quando o produto sujeito a ST');
insert into vendas.tb_cfop (codigo, descricao) values (2415, 'Retorno de mercadoria adquirida ou recebida de terceiros, remetida p/ venda fora do estabelecimento em operacao com mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (2501, 'Entrada de mercadoria recebida com fim especifico de exportacao');
insert into vendas.tb_cfop (codigo, descricao) values (2503, 'Entrada decorrente de devolucao de produto industrializado pelo estabelecimento, remetido com fim especifico de exportacao');
insert into vendas.tb_cfop (codigo, descricao) values (2504, 'Entrada decorrente de devolucao de mercadoria remetida com fim especifico de exportacao, adquirida ou recebida de terceiros');
insert into vendas.tb_cfop (codigo, descricao) values (2505, 'Entrada decorrente de devolucao simbolica de mercadoria remetida p/ formacao de lote de exportacao, de produto industrializado ou produzido pelo proprio estabelecimento.');
insert into vendas.tb_cfop (codigo, descricao) values (2506, 'Entrada decorrente de devolucao simbolica de mercadoria, adquirida ou recebida de terceiros, remetida p/ formacao de lote de exportacao.');
insert into vendas.tb_cfop (codigo, descricao) values (2551, 'Compra de bem p/ o ativo imobilizado');
insert into vendas.tb_cfop (codigo, descricao) values (2552, 'Transferencia de bem do ativo imobilizado');
insert into vendas.tb_cfop (codigo, descricao) values (2553, 'Devolucao de venda de bem do ativo imobilizado');
insert into vendas.tb_cfop (codigo, descricao) values (2554, 'Retorno de bem do ativo imobilizado remetido p/ uso fora do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (2555, 'Entrada de bem do ativo imobilizado de terceiro, remetido p/ uso no estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (2556, 'Compra de material p/ uso ou consumo');
insert into vendas.tb_cfop (codigo, descricao) values (2557, 'Transferencia de material p/ uso ou consumo');
insert into vendas.tb_cfop (codigo, descricao) values (2603, 'Ressarcimento de ICMS retido por substituicao tributaria');
insert into vendas.tb_cfop (codigo, descricao) values (2651, 'Compra de combustivel ou lubrificante p/ industrializacao subsequente');
insert into vendas.tb_cfop (codigo, descricao) values (2652, 'Compra de combustivel ou lubrificante p/ comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (2653, 'Compra de combustivel ou lubrificante por consumidor ou usuario final');
insert into vendas.tb_cfop (codigo, descricao) values (2658, 'Transferencia de combustivel ou lubrificante p/ industrializacao');
insert into vendas.tb_cfop (codigo, descricao) values (2659, 'Transferencia de combustivel ou lubrificante p/ comercializacao ');
insert into vendas.tb_cfop (codigo, descricao) values (2660, 'Devolucao de venda de combustivel ou lubrificante destinados a industrializacao subsequente');
insert into vendas.tb_cfop (codigo, descricao) values (2661, 'Devolucao de venda de combustivel ou lubrificante destinados a comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (2662, 'Devolucao de venda de combustivel ou lubrificante destinados a consumidor ou usuario final');
insert into vendas.tb_cfop (codigo, descricao) values (2663, 'Entrada de combustivel ou lubrificante p/ armazenagem');
insert into vendas.tb_cfop (codigo, descricao) values (2664, 'Retorno de combustivel ou lubrificante remetidos p/ armazenagem');
insert into vendas.tb_cfop (codigo, descricao) values (2901, 'Entrada p/ industrializacao por encomenda');
insert into vendas.tb_cfop (codigo, descricao) values (2902, 'Retorno de mercadoria remetida p/ industrializacao por encomenda');
insert into vendas.tb_cfop (codigo, descricao) values (2903, 'Entrada de mercadoria remetida p/ industrializacao e nao aplicada no referido processo');
insert into vendas.tb_cfop (codigo, descricao) values (2904, 'Retorno de remessa p/ venda fora do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (2905, 'Entrada de mercadoria recebida p/ deposito em deposito fechado ou armazem geral');
insert into vendas.tb_cfop (codigo, descricao) values (2906, 'Retorno de mercadoria remetida p/ deposito fechado ou armazem geral');
insert into vendas.tb_cfop (codigo, descricao) values (2907, 'Retorno simbolico de mercadoria remetida p/ deposito fechado ou armazem geral');
insert into vendas.tb_cfop (codigo, descricao) values (2908, 'Entrada de bem por conta de contrato de comodato');
insert into vendas.tb_cfop (codigo, descricao) values (2909, 'Retorno de bem remetido por conta de contrato de comodato');
insert into vendas.tb_cfop (codigo, descricao) values (2910, 'Entrada de bonificacao, doacao ou brinde');
insert into vendas.tb_cfop (codigo, descricao) values (2911, 'Entrada de amostra gratis');
insert into vendas.tb_cfop (codigo, descricao) values (2912, 'Entrada de mercadoria ou bem recebido p/ demonstracao');
insert into vendas.tb_cfop (codigo, descricao) values (2913, 'Retorno de mercadoria ou bem remetido p/ demonstracao');
insert into vendas.tb_cfop (codigo, descricao) values (2914, 'Retorno de mercadoria ou bem remetido p/ exposicao ou feira');
insert into vendas.tb_cfop (codigo, descricao) values (2915, 'Entrada de mercadoria ou bem recebido p/ conserto ou reparo');
insert into vendas.tb_cfop (codigo, descricao) values (2916, 'Retorno de mercadoria ou bem remetido p/ conserto ou reparo');
insert into vendas.tb_cfop (codigo, descricao) values (2917, 'Entrada de mercadoria recebida em consignacao mercantil ou industrial');
insert into vendas.tb_cfop (codigo, descricao) values (2918, 'Devolucao de mercadoria remetida em consignacao mercantil ou industrial');
insert into vendas.tb_cfop (codigo, descricao) values (2919, 'Devolucao simbolica de mercadoria vendida ou utilizada em processo industrial, remetida anteriormente em consignacao mercantil ou industrial');
insert into vendas.tb_cfop (codigo, descricao) values (2920, 'Entrada de vasilhame ou sacaria');
insert into vendas.tb_cfop (codigo, descricao) values (2921, 'Retorno de vasilhame ou sacaria');
insert into vendas.tb_cfop (codigo, descricao) values (2922, 'Lancamento efetuado a titulo de simples faturamento decorrente de compra p/ recebimento futuro');
insert into vendas.tb_cfop (codigo, descricao) values (2923, 'Entrada de mercadoria recebida do vendedor remetente, em venda a ordem');
insert into vendas.tb_cfop (codigo, descricao) values (2924, 'Entrada p/ industrializacao por conta e ordem do adquirente da mercadoria, quando esta nao transitar pelo estabelecimento do adquirente');
insert into vendas.tb_cfop (codigo, descricao) values (2925, 'Retorno de mercadoria remetida p/ industrializacao por conta e ordem do adquirente da mercadoria, quando esta nao transitar pelo estabelecimento do adquirente');
insert into vendas.tb_cfop (codigo, descricao) values (2931, 'Lancamento efetuado pelo tomador do servico de transporte, quando a responsabilidade de retencao do imposto for atribuida ao remetente ou alienante da mercadoria, pelo servico de transporte realizado por transportador autonomo ou por transportador nao-inscrito na UF onde se tenha iniciado o servico  ');
insert into vendas.tb_cfop (codigo, descricao) values (2932, 'Aquisicao de servico de transporte iniciado em UF diversa daquela onde esteja inscrito o prestador ');
insert into vendas.tb_cfop (codigo, descricao) values (2933, 'Aquisicao de servico tributado pelo Imposto Sobre Servicos de Qualquer Natureza');
insert into vendas.tb_cfop (codigo, descricao) values (2934, 'Entrada simbolica de mercadoria recebida p/ deposito fechado ou armazem geral');
insert into vendas.tb_cfop (codigo, descricao) values (2949, 'Outra entrada de mercadoria ou prestacao de servico nao especificado');
insert into vendas.tb_cfop (codigo, descricao) values (3101, 'Compra p/ industrializacao ou producao rural');
insert into vendas.tb_cfop (codigo, descricao) values (3102, 'Compra p/ comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (3126, 'Compra p/ utilizacao na prestacao de servico sujeita ao ICMS');
insert into vendas.tb_cfop (codigo, descricao) values (3127, 'Compra p/ industrializacao sob o regime de drawback ');
insert into vendas.tb_cfop (codigo, descricao) values (3128, 'Compra p/ utilizacao na prestacao de servico sujeita ao ISSQN');
insert into vendas.tb_cfop (codigo, descricao) values (3129, 'Compra para industrializacao sob o Regime Aduaneiro Especial de Entreposto Industrial (Recof-Sped)');
insert into vendas.tb_cfop (codigo, descricao) values (3201, 'Devolucao de venda de producao do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (3202, 'Devolucao de venda de mercadoria adquirida ou recebida de terceiros');
insert into vendas.tb_cfop (codigo, descricao) values (3205, 'Anulacao de valor relativo a prestacao de servico de comunicacao');
insert into vendas.tb_cfop (codigo, descricao) values (3206, 'Anulacao de valor relativo a prestacao de servico de transporte');
insert into vendas.tb_cfop (codigo, descricao) values (3207, 'Anulacao de valor relativo a venda de energia eletrica');
insert into vendas.tb_cfop (codigo, descricao) values (3211, 'Devolucao de venda de producao do estabelecimento sob o regime de drawback ');
insert into vendas.tb_cfop (codigo, descricao) values (3212, 'Devolucao de venda no mercado externo de mercadoria industrializada sob o Regime Aduaneiro Especial de Entreposto Industrial (Recof-Sped)');
insert into vendas.tb_cfop (codigo, descricao) values (3251, 'Compra de energia eletrica p/ distribuicao ou comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (3301, 'Aquisicao de servico de comunicacao p/ execucao de servico da mesma natureza');
insert into vendas.tb_cfop (codigo, descricao) values (3351, 'Aquisicao de servico de transporte p/ execucao de servico da mesma natureza');
insert into vendas.tb_cfop (codigo, descricao) values (3352, 'Aquisicao de servico de transporte por estabelecimento industrial');
insert into vendas.tb_cfop (codigo, descricao) values (3353, 'Aquisicao de servico de transporte por estabelecimento comercial');
insert into vendas.tb_cfop (codigo, descricao) values (3354, 'Aquisicao de servico de transporte por estabelecimento de prestador de servico de comunicacao');
insert into vendas.tb_cfop (codigo, descricao) values (3355, 'Aquisicao de servico de transporte por estabelecimento de geradora ou de distribuidora de energia eletrica');
insert into vendas.tb_cfop (codigo, descricao) values (3356, 'Aquisicao de servico de transporte por estabelecimento de produtor rural');
insert into vendas.tb_cfop (codigo, descricao) values (3503, 'Devolucao de mercadoria exportada que tenha sido recebida com fim especifico de exportacao');
insert into vendas.tb_cfop (codigo, descricao) values (3551, 'Compra de bem p/ o ativo imobilizado');
insert into vendas.tb_cfop (codigo, descricao) values (3553, 'Devolucao de venda de bem do ativo imobilizado');
insert into vendas.tb_cfop (codigo, descricao) values (3556, 'Compra de material p/ uso ou consumo');
insert into vendas.tb_cfop (codigo, descricao) values (3651, 'Compra de combustivel ou lubrificante p/ industrializacao subsequente');
insert into vendas.tb_cfop (codigo, descricao) values (3652, 'Compra de combustivel ou lubrificante p/ comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (3653, 'Compra de combustivel ou lubrificante por consumidor ou usuario final');
insert into vendas.tb_cfop (codigo, descricao) values (3930, 'Lancamento efetuado a titulo de entrada de bem sob amparo de regime especial aduaneiro de admissao temporaria');
insert into vendas.tb_cfop (codigo, descricao) values (3949, 'Outra entrada de mercadoria ou prestacao de servico nao especificado');
insert into vendas.tb_cfop (codigo, descricao) values (5101, 'Venda de producao do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (5102, 'Venda de mercadoria adquirida ou recebida de terceiros');
insert into vendas.tb_cfop (codigo, descricao) values (5103, 'Venda de producao do estabelecimento efetuada fora do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (5104, 'Venda de mercadoria adquirida ou recebida de terceiros, efetuada fora do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (5105, 'Venda de producao do estabelecimento que nao deva por ele transitar');
insert into vendas.tb_cfop (codigo, descricao) values (5106, 'Venda de mercadoria adquirida ou recebida de terceiros, que nao deva por ele transitar ');
insert into vendas.tb_cfop (codigo, descricao) values (5109, 'Venda de producao do estabelecimento destinada a ZFM ou ALC');
insert into vendas.tb_cfop (codigo, descricao) values (5110, 'Venda de mercadoria, adquirida ou recebida de terceiros, destinada a ZFM ou ALC');
insert into vendas.tb_cfop (codigo, descricao) values (5111, 'Venda de producao do estabelecimento remetida anteriormente em consignacao industrial');
insert into vendas.tb_cfop (codigo, descricao) values (5112, 'Venda de mercadoria adquirida ou recebida de terceiros remetida anteriormente em consignacao industrial');
insert into vendas.tb_cfop (codigo, descricao) values (5113, 'Venda de producao do estabelecimento remetida anteriormente em consignacao mercantil');
insert into vendas.tb_cfop (codigo, descricao) values (5114, 'Venda de mercadoria adquirida ou recebida de terceiros remetida anteriormente em consignacao mercantil');
insert into vendas.tb_cfop (codigo, descricao) values (5115, 'Venda de mercadoria adquirida ou recebida de terceiros, recebida anteriormente em consignacao mercantil');
insert into vendas.tb_cfop (codigo, descricao) values (5116, 'Venda de producao do estabelecimento originada de encomenda p/ entrega futura');
insert into vendas.tb_cfop (codigo, descricao) values (5117, 'Venda de mercadoria adquirida ou recebida de terceiros, originada de encomenda p/ entrega futura');
insert into vendas.tb_cfop (codigo, descricao) values (5118, 'Venda de producao do estabelecimento entregue ao destinatario por conta e ordem do adquirente originario, em venda a ordem');
insert into vendas.tb_cfop (codigo, descricao) values (5119, 'Venda de mercadoria adquirida ou recebida de terceiros entregue ao destinatario por conta e ordem do adquirente originario, em venda a ordem');
insert into vendas.tb_cfop (codigo, descricao) values (5120, 'Venda de mercadoria adquirida ou recebida de terceiros entregue ao destinatario pelo vendedor remetente, em venda a ordem');
insert into vendas.tb_cfop (codigo, descricao) values (5122, 'Venda de producao do estabelecimento remetida p/ industrializacao, por conta e ordem do adquirente, sem transitar pelo estabelecimento do adquirente');
insert into vendas.tb_cfop (codigo, descricao) values (5123, 'Venda de mercadoria adquirida ou recebida de terceiros remetida p/ industrializacao, por conta e ordem do adquirente, sem transitar pelo estabelecimento do adquirente');
insert into vendas.tb_cfop (codigo, descricao) values (5124, 'Industrializacao efetuada p/ outra empresa');
insert into vendas.tb_cfop (codigo, descricao) values (5125, 'Industrializacao efetuada p/ outra empresa quando a mercadoria recebida p/ utilizacao no processo de industrializacao nao transitar pelo estabelecimento adquirente da mercadoria');
insert into vendas.tb_cfop (codigo, descricao) values (5129, 'Venda de insumo importado e de mercadoria industrializada sob o amparo do Regime Aduaneiro Especial de Entreposto Industrial (Recof-Sped)');
insert into vendas.tb_cfop (codigo, descricao) values (5151, 'Transferencia de producao do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (5152, 'Transferencia de mercadoria adquirida ou recebida de terceiros');
insert into vendas.tb_cfop (codigo, descricao) values (5153, 'Transferencia de energia eletrica');
insert into vendas.tb_cfop (codigo, descricao) values (5155, 'Transferencia de producao do estabelecimento, que nao deva por ele transitar');
insert into vendas.tb_cfop (codigo, descricao) values (5156, 'Transferencia de mercadoria adquirida ou recebida de terceiros, que nao deva por ele transitar');
insert into vendas.tb_cfop (codigo, descricao) values (5201, 'Devolucao de compra p/ industrializacao ou producao rural');
insert into vendas.tb_cfop (codigo, descricao) values (5202, 'Devolucao de compra p/ comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (5205, 'Anulacao de valor relativo a aquisicao de servico de comunicacao');
insert into vendas.tb_cfop (codigo, descricao) values (5206, 'Anulacao de valor relativo a aquisicao de servico de transporte');
insert into vendas.tb_cfop (codigo, descricao) values (5207, 'Anulacao de valor relativo a compra de energia eletrica');
insert into vendas.tb_cfop (codigo, descricao) values (5208, 'Devolucao de mercadoria recebida em transferencia p/ industrializacao ou producao rural ');
insert into vendas.tb_cfop (codigo, descricao) values (5209, 'Devolucao de mercadoria recebida em transferencia p/ comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (5210, 'Devolucao de compra p/ utilizacao na prestacao de servico');
insert into vendas.tb_cfop (codigo, descricao) values (5251, 'Venda de energia eletrica p/ distribuicao ou comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (5252, 'Venda de energia eletrica p/ estabelecimento industrial');
insert into vendas.tb_cfop (codigo, descricao) values (5253, 'Venda de energia eletrica p/ estabelecimento comercial');
insert into vendas.tb_cfop (codigo, descricao) values (5254, 'Venda de energia eletrica p/ estabelecimento prestador de servico de transporte');
insert into vendas.tb_cfop (codigo, descricao) values (5255, 'Venda de energia eletrica p/ estabelecimento prestador de servico de comunicacao');
insert into vendas.tb_cfop (codigo, descricao) values (5256, 'Venda de energia eletrica p/ estabelecimento de produtor rural');
insert into vendas.tb_cfop (codigo, descricao) values (5257, 'Venda de energia eletrica p/ consumo por demanda contratada');
insert into vendas.tb_cfop (codigo, descricao) values (5258, 'Venda de energia eletrica a nao contribuinte');
insert into vendas.tb_cfop (codigo, descricao) values (5301, 'Prestacao de servico de comunicacao p/ execucao de servico da mesma natureza');
insert into vendas.tb_cfop (codigo, descricao) values (5302, 'Prestacao de servico de comunicacao a estabelecimento industrial');
insert into vendas.tb_cfop (codigo, descricao) values (5303, 'Prestacao de servico de comunicacao a estabelecimento comercial');
insert into vendas.tb_cfop (codigo, descricao) values (5304, 'Prestacao de servico de comunicacao a estabelecimento de prestador de servico de transporte');
insert into vendas.tb_cfop (codigo, descricao) values (5305, 'Prestacao de servico de comunicacao a estabelecimento de geradora ou de distribuidora de energia eletrica');
insert into vendas.tb_cfop (codigo, descricao) values (5306, 'Prestacao de servico de comunicacao a estabelecimento de produtor rural');
insert into vendas.tb_cfop (codigo, descricao) values (5307, 'Prestacao de servico de comunicacao a nao contribuinte');
insert into vendas.tb_cfop (codigo, descricao) values (5351, 'Prestacao de servico de transporte p/ execucao de servico da mesma natureza');
insert into vendas.tb_cfop (codigo, descricao) values (5352, 'Prestacao de servico de transporte a estabelecimento industrial');
insert into vendas.tb_cfop (codigo, descricao) values (5353, 'Prestacao de servico de transporte a estabelecimento comercial');
insert into vendas.tb_cfop (codigo, descricao) values (5354, 'Prestacao de servico de transporte a estabelecimento de prestador de servico de comunicacao');
insert into vendas.tb_cfop (codigo, descricao) values (5355, 'Prestacao de servico de transporte a estabelecimento de geradora ou de distribuidora de energia eletrica');
insert into vendas.tb_cfop (codigo, descricao) values (5356, 'Prestacao de servico de transporte a estabelecimento de produtor rural');
insert into vendas.tb_cfop (codigo, descricao) values (5357, 'Prestacao de servico de transporte a nao contribuinte');
insert into vendas.tb_cfop (codigo, descricao) values (5359, 'Prestacao de servico de transporte a contribuinte ou a nao-contribuinte, quando a mercadoria transportada esteja dispensada de emissao de Nota Fiscal  ');
insert into vendas.tb_cfop (codigo, descricao) values (5360, 'Prestacao de servico de transporte a contribuinte-substituto em relacao ao servico de transporte');
insert into vendas.tb_cfop (codigo, descricao) values (5401, 'Venda de producao do estabelecimento quando o produto esteja sujeito a ST');
insert into vendas.tb_cfop (codigo, descricao) values (5402, 'Venda de producao do estabelecimento de produto sujeito a ST, em operacao entre contribuintes substitutos do mesmo produto');
insert into vendas.tb_cfop (codigo, descricao) values (5403, 'Venda de mercadoria, adquirida ou recebida de terceiros, sujeita a ST, na condicao de contribuinte-substituto');
insert into vendas.tb_cfop (codigo, descricao) values (5405, 'Venda de mercadoria, adquirida ou recebida de terceiros, sujeita a ST, na condicao de contribuinte-substituido');
insert into vendas.tb_cfop (codigo, descricao) values (5408, 'Transferencia de producao do estabelecimento quando o produto sujeito a ST');
insert into vendas.tb_cfop (codigo, descricao) values (5409, 'Transferencia de mercadoria adquirida ou recebida de terceiros em operacao com mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (5410, 'Devolucao de compra p/ industrializacao de mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (5411, 'Devolucao de compra p/ comercializacao em operacao com mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (5412, 'Devolucao de bem do ativo imobilizado, em operacao com mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (5413, 'Devolucao de mercadoria destinada ao uso ou consumo, em operacao com mercadoria sujeita a ST.');
insert into vendas.tb_cfop (codigo, descricao) values (5414, 'Remessa de producao do estabelecimento p/ venda fora do estabelecimento, quando o produto sujeito a ST');
insert into vendas.tb_cfop (codigo, descricao) values (5415, 'Remessa de mercadoria adquirida ou recebida de terceiros p/ venda fora do estabelecimento, em operacao com mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (5451, 'Remessa de animal e de insumo p/ estabelecimento produtor');
insert into vendas.tb_cfop (codigo, descricao) values (5501, 'Remessa de producao do estabelecimento, com fim especifico de exportacao');
insert into vendas.tb_cfop (codigo, descricao) values (5502, 'Remessa de mercadoria adquirida ou recebida de terceiros, com fim especifico de exportacao');
insert into vendas.tb_cfop (codigo, descricao) values (5503, 'Devolucao de mercadoria recebida com fim especifico de exportacao');
insert into vendas.tb_cfop (codigo, descricao) values (5504, 'Remessa de mercadoria p/ formacao de lote de exportacao, de produto industrializado ou produzido pelo proprio estabelecimento.');
insert into vendas.tb_cfop (codigo, descricao) values (5505, 'Remessa de mercadoria, adquirida ou recebida de terceiros, p/ formacao de lote de exportacao.');
insert into vendas.tb_cfop (codigo, descricao) values (5551, 'Venda de bem do ativo imobilizado');
insert into vendas.tb_cfop (codigo, descricao) values (5552, 'Transferencia de bem do ativo imobilizado');
insert into vendas.tb_cfop (codigo, descricao) values (5553, 'Devolucao de compra de bem p/ o ativo imobilizado');
insert into vendas.tb_cfop (codigo, descricao) values (5554, 'Remessa de bem do ativo imobilizado p/ uso fora do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (5555, 'Devolucao de bem do ativo imobilizado de terceiro, recebido p/ uso no estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (5556, 'Devolucao de compra de material de uso ou consumo');
insert into vendas.tb_cfop (codigo, descricao) values (5557, 'Transferencia de material de uso ou consumo');
insert into vendas.tb_cfop (codigo, descricao) values (5601, 'Transferencia de credito de ICMS acumulado');
insert into vendas.tb_cfop (codigo, descricao) values (5602, 'Transferencia de saldo credor do ICMS, p/ outro estabelecimento da mesma empresa, destinado a compensacao de saldo devedor do ICMS');
insert into vendas.tb_cfop (codigo, descricao) values (5603, 'Ressarcimento de ICMS retido por substituicao tributaria');
insert into vendas.tb_cfop (codigo, descricao) values (5605, 'Transferencia de saldo devedor do ICMS de outro estabelecimento da mesma empresa  ');
insert into vendas.tb_cfop (codigo, descricao) values (5606, 'Utilizacao de saldo credor do ICMS p/ extincao por compensacao de debitos fiscais');
insert into vendas.tb_cfop (codigo, descricao) values (5651, 'Venda de combustivel ou lubrificante de producao do estabelecimento destinados a industrializacao subsequente');
insert into vendas.tb_cfop (codigo, descricao) values (5652, 'Venda de combustivel ou lubrificante, de producao do estabelecimento, destinados a comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (5653, 'Venda de combustivel ou lubrificante, de producao do estabelecimento, destinados a consumidor ou usuario final');
insert into vendas.tb_cfop (codigo, descricao) values (5654, 'Venda de combustivel ou lubrificante, adquiridos ou recebidos de terceiros, destinados a industrializacao subsequente');
insert into vendas.tb_cfop (codigo, descricao) values (5655, 'Venda de combustivel ou lubrificante, adquiridos ou recebidos de terceiros, destinados a comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (5656, 'Venda de combustivel ou lubrificante, adquiridos ou recebidos de terceiros, destinados a consumidor ou usuario final');
insert into vendas.tb_cfop (codigo, descricao) values (5657, 'Remessa de combustivel ou lubrificante, adquiridos ou recebidos de terceiros, p/ venda fora do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (5658, 'Transferencia de combustivel ou lubrificante de producao do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (5659, 'Transferencia de combustivel ou lubrificante adquiridos ou recebidos de terceiros');
insert into vendas.tb_cfop (codigo, descricao) values (5660, 'Devolucao de compra de combustivel ou lubrificante adquiridos p/ industrializacao subsequente');
insert into vendas.tb_cfop (codigo, descricao) values (5661, 'Devolucao de compra de combustivel ou lubrificante adquiridos p/ comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (5662, 'Devolucao de compra de combustivel ou lubrificante adquiridos por consumidor ou usuario final');
insert into vendas.tb_cfop (codigo, descricao) values (5663, 'Remessa p/ armazenagem de combustivel ou lubrificante');
insert into vendas.tb_cfop (codigo, descricao) values (5664, 'Retorno de combustivel ou lubrificante recebidos p/ armazenagem');
insert into vendas.tb_cfop (codigo, descricao) values (5665, 'Retorno simbolico de combustivel ou lubrificante recebidos p/ armazenagem');
insert into vendas.tb_cfop (codigo, descricao) values (5666, 'Remessa, por conta e ordem de terceiros, de combustivel ou lubrificante recebidos p/ armazenagem');
insert into vendas.tb_cfop (codigo, descricao) values (5667, 'Venda de combustivel ou lubrificante a consumidor ou usuario final estabelecido em outra UF');
insert into vendas.tb_cfop (codigo, descricao) values (5901, 'Remessa p/ industrializacao por encomenda');
insert into vendas.tb_cfop (codigo, descricao) values (5902, 'Retorno de mercadoria utilizada na industrializacao por encomenda');
insert into vendas.tb_cfop (codigo, descricao) values (5903, 'Retorno de mercadoria recebida p/ industrializacao e nao aplicada no referido processo');
insert into vendas.tb_cfop (codigo, descricao) values (5904, 'Remessa p/ venda fora do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (5905, 'Remessa p/ deposito fechado ou armazem geral');
insert into vendas.tb_cfop (codigo, descricao) values (5906, 'Retorno de mercadoria depositada em deposito fechado ou armazem geral');
insert into vendas.tb_cfop (codigo, descricao) values (5907, 'Retorno simbolico de mercadoria depositada em deposito fechado ou armazem geral');
insert into vendas.tb_cfop (codigo, descricao) values (5908, 'Remessa de bem por conta de contrato de comodato');
insert into vendas.tb_cfop (codigo, descricao) values (5909, 'Retorno de bem recebido por conta de contrato de comodato');
insert into vendas.tb_cfop (codigo, descricao) values (5910, 'Remessa em bonificacao, doacao ou brinde');
insert into vendas.tb_cfop (codigo, descricao) values (5911, 'Remessa de amostra gratis');
insert into vendas.tb_cfop (codigo, descricao) values (5912, 'Remessa de mercadoria ou bem p/ demonstracao');
insert into vendas.tb_cfop (codigo, descricao) values (5913, 'Retorno de mercadoria ou bem recebido p/ demonstracao');
insert into vendas.tb_cfop (codigo, descricao) values (5914, 'Remessa de mercadoria ou bem p/ exposicao ou feira');
insert into vendas.tb_cfop (codigo, descricao) values (5915, 'Remessa de mercadoria ou bem p/ conserto ou reparo');
insert into vendas.tb_cfop (codigo, descricao) values (5916, 'Retorno de mercadoria ou bem recebido p/ conserto ou reparo');
insert into vendas.tb_cfop (codigo, descricao) values (5917, 'Remessa de mercadoria em consignacao mercantil ou industrial');
insert into vendas.tb_cfop (codigo, descricao) values (5918, 'Devolucao de mercadoria recebida em consignacao mercantil ou industrial');
insert into vendas.tb_cfop (codigo, descricao) values (5919, 'Devolucao simbolica de mercadoria vendida ou utilizada em processo industrial, recebida anteriormente em consignacao mercantil ou industrial');
insert into vendas.tb_cfop (codigo, descricao) values (5920, 'Remessa de vasilhame ou sacaria');
insert into vendas.tb_cfop (codigo, descricao) values (5921, 'Devolucao de vasilhame ou sacaria');
insert into vendas.tb_cfop (codigo, descricao) values (5922, 'Lancamento efetuado a titulo de simples faturamento decorrente de venda p/ entrega futura');
insert into vendas.tb_cfop (codigo, descricao) values (5923, 'Remessa de mercadoria por conta e ordem de terceiros, em venda a ordem ou em operacoes com armazem geral ou deposito fechado.');
insert into vendas.tb_cfop (codigo, descricao) values (5924, 'Remessa p/ industrializacao por conta e ordem do adquirente da mercadoria, quando esta nao transitar pelo estabelecimento do adquirente');
insert into vendas.tb_cfop (codigo, descricao) values (5925, 'Retorno de mercadoria recebida p/ industrializacao por conta e ordem do adquirente da mercadoria, quando aquela nao transitar pelo estabelecimento do adquirente');
insert into vendas.tb_cfop (codigo, descricao) values (5926, 'Lancamento efetuado a titulo de reclassificacao de mercadoria decorrente de formacao de kit ou de sua desagregacao');
insert into vendas.tb_cfop (codigo, descricao) values (5927, 'Lancamento efetuado a titulo de baixa de estoque decorrente de perda, roubo ou deterioracao');
insert into vendas.tb_cfop (codigo, descricao) values (5928, 'Lancamento efetuado a titulo de baixa de estoque decorrente do encerramento da atividade da empresa');
insert into vendas.tb_cfop (codigo, descricao) values (5929, 'Lancamento efetuado em decorrencia de emissao de documento fiscal relativo a operacao ou prestacao tambem registrada em equipamento Emissor de Cupom Fiscal - ECF');
insert into vendas.tb_cfop (codigo, descricao) values (5931, 'Lancamento efetuado em decorrencia da responsabilidade de retencao do imposto por substituicao tributaria, atribuida ao remetente ou alienante da mercadoria, pelo servico de transporte realizado por transportador autonomo ou por transportador nao inscrito na UF onde iniciado o servico');
insert into vendas.tb_cfop (codigo, descricao) values (5932, 'Prestacao de servico de transporte iniciada em UF diversa daquela onde inscrito o prestador');
insert into vendas.tb_cfop (codigo, descricao) values (5933, 'Prestacao de servico tributado pelo Imposto Sobre Servicos de Qualquer Natureza');
insert into vendas.tb_cfop (codigo, descricao) values (5934, 'Remessa simbolica de mercadoria depositada em armazem geral ou deposito fechado.');
insert into vendas.tb_cfop (codigo, descricao) values (5949, 'Outra saida de mercadoria ou prestacao de servico nao especificado');
insert into vendas.tb_cfop (codigo, descricao) values (6101, 'Venda de producao do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (6102, 'Venda de mercadoria adquirida ou recebida de terceiros');
insert into vendas.tb_cfop (codigo, descricao) values (6103, 'Venda de producao do estabelecimento, efetuada fora do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (6104, 'Venda de mercadoria adquirida ou recebida de terceiros, efetuada fora do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (6105, 'Venda de producao do estabelecimento que nao deva por ele transitar');
insert into vendas.tb_cfop (codigo, descricao) values (6106, 'Venda de mercadoria adquirida ou recebida de terceiros, que nao deva por ele transitar');
insert into vendas.tb_cfop (codigo, descricao) values (6107, 'Venda de producao do estabelecimento, destinada a nao contribuinte');
insert into vendas.tb_cfop (codigo, descricao) values (6108, 'Venda de mercadoria adquirida ou recebida de terceiros, destinada a nao contribuinte');
insert into vendas.tb_cfop (codigo, descricao) values (6109, 'Venda de producao do estabelecimento destinada a ZFM ou ALC');
insert into vendas.tb_cfop (codigo, descricao) values (6110, 'Venda de mercadoria, adquirida ou recebida de terceiros, destinada a ZFM ou ALC');
insert into vendas.tb_cfop (codigo, descricao) values (6111, 'Venda de producao do estabelecimento remetida anteriormente em consignacao industrial');
insert into vendas.tb_cfop (codigo, descricao) values (6112, 'Venda de mercadoria adquirida ou recebida de Terceiros remetida anteriormente em consignacao industrial');
insert into vendas.tb_cfop (codigo, descricao) values (6113, 'Venda de producao do estabelecimento remetida anteriormente em consignacao mercantil');
insert into vendas.tb_cfop (codigo, descricao) values (6114, 'Venda de mercadoria adquirida ou recebida de terceiros remetida anteriormente em consignacao mercantil');
insert into vendas.tb_cfop (codigo, descricao) values (6115, 'Venda de mercadoria adquirida ou recebida de terceiros, recebida anteriormente em consignacao mercantil');
insert into vendas.tb_cfop (codigo, descricao) values (6116, 'Venda de producao do estabelecimento originada de encomenda p/ entrega futura');
insert into vendas.tb_cfop (codigo, descricao) values (6117, 'Venda de mercadoria adquirida ou recebida de terceiros, originada de encomenda p/ entrega futura');
insert into vendas.tb_cfop (codigo, descricao) values (6118, 'Venda de producao do estabelecimento entregue ao destinatario por conta e ordem do adquirente originario, em venda a ordem');
insert into vendas.tb_cfop (codigo, descricao) values (6119, 'Venda de mercadoria adquirida ou recebida de terceiros entregue ao destinatario por conta e ordem do adquirente originario, em venda a ordem');
insert into vendas.tb_cfop (codigo, descricao) values (6120, 'Venda de mercadoria adquirida ou recebida de terceiros entregue ao destinatario pelo vendedor remetente, em venda a ordem');
insert into vendas.tb_cfop (codigo, descricao) values (6122, 'Venda de producao do estabelecimento remetida p/ industrializacao, por conta e ordem do adquirente, sem transitar pelo estabelecimento do adquirente');
insert into vendas.tb_cfop (codigo, descricao) values (6123, 'Venda de mercadoria adquirida ou recebida de terceiros remetida p/ industrializacao, por conta e ordem do adquirente, sem transitar pelo estabelecimento do adquirente');
insert into vendas.tb_cfop (codigo, descricao) values (6124, 'Industrializacao efetuada p/ outra empresa');
insert into vendas.tb_cfop (codigo, descricao) values (6125, 'Industrializacao efetuada p/ outra empresa quando a mercadoria recebida p/ utilizacao no processo de industrializacao nao transitar pelo estabelecimento adquirente da mercadoria');
insert into vendas.tb_cfop (codigo, descricao) values (6129, 'Venda de insumo importado e de mercadoria industrializada sob o amparo do Regime Aduaneiro Especial de Entreposto Industrial (Recof-Sped)');
insert into vendas.tb_cfop (codigo, descricao) values (6151, 'Transferencia de producao do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (6152, 'Transferencia de mercadoria adquirida ou recebida de terceiros');
insert into vendas.tb_cfop (codigo, descricao) values (6153, 'Transferencia de energia eletrica');
insert into vendas.tb_cfop (codigo, descricao) values (6155, 'Transferencia de producao do estabelecimento, que nao deva por ele transitar');
insert into vendas.tb_cfop (codigo, descricao) values (6156, 'Transferencia de mercadoria adquirida ou recebida de terceiros, que nao deva por ele transitar');
insert into vendas.tb_cfop (codigo, descricao) values (6201, 'Devolucao de compra p/ industrializacao ou producao rural');
insert into vendas.tb_cfop (codigo, descricao) values (6202, 'Devolucao de compra p/ comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (6205, 'Anulacao de valor relativo a aquisicao de servico de comunicacao');
insert into vendas.tb_cfop (codigo, descricao) values (6206, 'Anulacao de valor relativo a aquisicao de servico de transporte');
insert into vendas.tb_cfop (codigo, descricao) values (6207, 'Anulacao de valor relativo a compra de energia eletrica');
insert into vendas.tb_cfop (codigo, descricao) values (6208, 'Devolucao de mercadoria recebida em transferencia p/ industrializacao ou producao rural ');
insert into vendas.tb_cfop (codigo, descricao) values (6209, 'Devolucao de mercadoria recebida em transferencia p/ comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (6210, 'Devolucao de compra p/ utilizacao na prestacao de servico');
insert into vendas.tb_cfop (codigo, descricao) values (6251, 'Venda de energia eletrica p/ distribuicao ou comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (6252, 'Venda de energia eletrica p/ estabelecimento industrial');
insert into vendas.tb_cfop (codigo, descricao) values (6253, 'Venda de energia eletrica p/ estabelecimento comercial');
insert into vendas.tb_cfop (codigo, descricao) values (6254, 'Venda de energia eletrica p/ estabelecimento prestador de servico de transporte');
insert into vendas.tb_cfop (codigo, descricao) values (6255, 'Venda de energia eletrica p/ estabelecimento prestador de servico de comunicacao');
insert into vendas.tb_cfop (codigo, descricao) values (6256, 'Venda de energia eletrica p/ estabelecimento de produtor rural');
insert into vendas.tb_cfop (codigo, descricao) values (6257, 'Venda de energia eletrica p/ consumo por demanda contratada');
insert into vendas.tb_cfop (codigo, descricao) values (6258, 'Venda de energia eletrica a nao contribuinte');
insert into vendas.tb_cfop (codigo, descricao) values (6301, 'Prestacao de servico de comunicacao p/ execucao de servico da mesma natureza');
insert into vendas.tb_cfop (codigo, descricao) values (6302, 'Prestacao de servico de comunicacao a estabelecimento industrial');
insert into vendas.tb_cfop (codigo, descricao) values (6303, 'Prestacao de servico de comunicacao a estabelecimento comercial');
insert into vendas.tb_cfop (codigo, descricao) values (6304, 'Prestacao de servico de comunicacao a estabelecimento de prestador de servico de transporte');
insert into vendas.tb_cfop (codigo, descricao) values (6305, 'Prestacao de servico de comunicacao a estabelecimento de geradora ou de distribuidora de energia eletrica');
insert into vendas.tb_cfop (codigo, descricao) values (6306, 'Prestacao de servico de comunicacao a estabelecimento de produtor rural');
insert into vendas.tb_cfop (codigo, descricao) values (6307, 'Prestacao de servico de comunicacao a nao contribuinte');
insert into vendas.tb_cfop (codigo, descricao) values (6351, 'Prestacao de servico de transporte p/ execucao de servico da mesma natureza');
insert into vendas.tb_cfop (codigo, descricao) values (6352, 'Prestacao de servico de transporte a estabelecimento industrial');
insert into vendas.tb_cfop (codigo, descricao) values (6353, 'Prestacao de servico de transporte a estabelecimento comercial');
insert into vendas.tb_cfop (codigo, descricao) values (6354, 'Prestacao de servico de transporte a estabelecimento de prestador de servico de comunicacao');
insert into vendas.tb_cfop (codigo, descricao) values (6355, 'Prestacao de servico de transporte a estabelecimento de geradora ou de distribuidora de energia eletrica');
insert into vendas.tb_cfop (codigo, descricao) values (6356, 'Prestacao de servico de transporte a estabelecimento de produtor rural');
insert into vendas.tb_cfop (codigo, descricao) values (6357, 'Prestacao de servico de transporte a nao contribuinte');
insert into vendas.tb_cfop (codigo, descricao) values (6359, 'Prestacao de servico de transporte a contribuinte ou a nao-contribuinte, quando a mercadoria transportada esteja dispensada de emissao de Nota Fiscal  ');
insert into vendas.tb_cfop (codigo, descricao) values (6360, 'Prestacao de servico de transporte a contribuinte substituto em relacao ao servico de transporte  ');
insert into vendas.tb_cfop (codigo, descricao) values (6401, 'Venda de producao do estabelecimento quando o produto sujeito a ST');
insert into vendas.tb_cfop (codigo, descricao) values (6402, 'Venda de producao do estabelecimento de produto sujeito a ST, em operacao entre contribuintes substitutos do mesmo produto');
insert into vendas.tb_cfop (codigo, descricao) values (6403, 'Venda de mercadoria adquirida ou recebida de terceiros em operacao com mercadoria sujeita a ST, na condicao de contribuinte substituto');
insert into vendas.tb_cfop (codigo, descricao) values (6404, 'Venda de mercadoria sujeita a ST, cujo imposto ja tenha sido retido anteriormente');
insert into vendas.tb_cfop (codigo, descricao) values (6408, 'Transferencia de producao do estabelecimento quando o produto sujeito a ST');
insert into vendas.tb_cfop (codigo, descricao) values (6409, 'Transferencia de mercadoria adquirida ou recebida de terceiros, sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (6410, 'Devolucao de compra p/ industrializacao ou ptroducao rural quando a mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (6411, 'Devolucao de compra p/ comercializacao em operacao com mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (6412, 'Devolucao de bem do ativo imobilizado, em operacao com mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (6413, 'Devolucao de mercadoria destinada ao uso ou consumo, em operacao com mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (6414, 'Remessa de producao do estabelecimento p/ venda fora do estabelecimento, quando o produto sujeito a ST');
insert into vendas.tb_cfop (codigo, descricao) values (6415, 'Remessa de mercadoria adquirida ou recebida de terceiros p/ venda fora do estabelecimento, quando a referida racao com mercadoria sujeita a ST');
insert into vendas.tb_cfop (codigo, descricao) values (6501, 'Remessa de producao do estabelecimento, com fim especifico de exportacao');
insert into vendas.tb_cfop (codigo, descricao) values (6502, 'Remessa de mercadoria adquirida ou recebida de terceiros, com fim especifico de exportacao');
insert into vendas.tb_cfop (codigo, descricao) values (6503, 'Devolucao de mercadoria recebida com fim especifico de exportacao');
insert into vendas.tb_cfop (codigo, descricao) values (6504, 'Remessa de mercadoria p/ formacao de lote de exportacao, de produto industrializado ou produzido pelo proprio estabelecimento.');
insert into vendas.tb_cfop (codigo, descricao) values (6505, 'Remessa de mercadoria, adquirida ou recebida de terceiros, p/ formacao de lote de exportacao.');
insert into vendas.tb_cfop (codigo, descricao) values (6551, 'Venda de bem do ativo imobilizado');
insert into vendas.tb_cfop (codigo, descricao) values (6552, 'Transferencia de bem do ativo imobilizado');
insert into vendas.tb_cfop (codigo, descricao) values (6553, 'Devolucao de compra de bem p/ o ativo imobilizado');
insert into vendas.tb_cfop (codigo, descricao) values (6554, 'Remessa de bem do ativo imobilizado p/ uso fora do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (6555, 'Devolucao de bem do ativo imobilizado de terceiro, recebido p/ uso no estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (6556, 'Devolucao de compra de material de uso ou consumo');
insert into vendas.tb_cfop (codigo, descricao) values (6557, 'Transferencia de material de uso ou consumo');
insert into vendas.tb_cfop (codigo, descricao) values (6603, 'Ressarcimento de ICMS retido por substituicao tributaria');
insert into vendas.tb_cfop (codigo, descricao) values (6651, 'Venda de combustivel ou lubrificante, de producao do estabelecimento, destinados a industrializacao subsequente');
insert into vendas.tb_cfop (codigo, descricao) values (6652, 'Venda de combustivel ou lubrificante, de producao do estabelecimento, destinados a comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (6653, 'Venda de combustivel ou lubrificante, de producao do estabelecimento, destinados a consumidor ou usuario final');
insert into vendas.tb_cfop (codigo, descricao) values (6654, 'Venda de combustivel ou lubrificante, adquiridos ou recebidos de terceiros, destinados a industrializacao subsequente');
insert into vendas.tb_cfop (codigo, descricao) values (6655, 'Venda de combustivel ou lubrificante, adquiridos ou recebidos de terceiros, destinados a comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (6656, 'Venda de combustivel ou lubrificante, adquiridos ou recebidos de terceiros, destinados a consumidor ou usuario final');
insert into vendas.tb_cfop (codigo, descricao) values (6657, 'Remessa de combustivel ou lubrificante, adquiridos ou recebidos de terceiros, p/ venda fora do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (6658, 'Transferencia de combustivel ou lubrificante de producao do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (6659, 'Transferencia de combustivel ou lubrificante adquiridos ou recebidos de terceiros');
insert into vendas.tb_cfop (codigo, descricao) values (6660, 'Devolucao de compra de combustivel ou lubrificante adquiridos p/ industrializacao subsequente');
insert into vendas.tb_cfop (codigo, descricao) values (6661, 'Devolucao de compra de combustivel ou lubrificante adquiridos p/ comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (6662, 'Devolucao de compra de combustivel ou lubrificante adquiridos por consumidor ou usuario final');
insert into vendas.tb_cfop (codigo, descricao) values (6663, 'Remessa p/ armazenagem de combustivel ou lubrificante');
insert into vendas.tb_cfop (codigo, descricao) values (6664, 'Retorno de combustivel ou lubrificante recebidos p/ armazenagem');
insert into vendas.tb_cfop (codigo, descricao) values (6665, 'Retorno simbolico de combustivel ou lubrificante recebidos p/ armazenagem');
insert into vendas.tb_cfop (codigo, descricao) values (6666, 'Remessa, por conta e ordem de terceiros, de combustivel ou lubrificante recebidos p/ armazenagem');
insert into vendas.tb_cfop (codigo, descricao) values (6667, 'Venda de combustivel ou lubrificante a consumidor ou usuario final estabelecido em outra UF diferente da que ocorrer o consumo');
insert into vendas.tb_cfop (codigo, descricao) values (6901, 'Remessa p/ industrializacao por encomenda');
insert into vendas.tb_cfop (codigo, descricao) values (6902, 'Retorno de mercadoria utilizada na industrializacao por encomenda');
insert into vendas.tb_cfop (codigo, descricao) values (6903, 'Retorno de mercadoria recebida p/ industrializacao e nao aplicada no referido processo');
insert into vendas.tb_cfop (codigo, descricao) values (6904, 'Remessa p/ venda fora do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (6905, 'Remessa p/ deposito fechado ou armazem geral');
insert into vendas.tb_cfop (codigo, descricao) values (6906, 'Retorno de mercadoria depositada em deposito fechado ou armazem geral');
insert into vendas.tb_cfop (codigo, descricao) values (6907, 'Retorno simbolico de mercadoria depositada em deposito fechado ou armazem geral');
insert into vendas.tb_cfop (codigo, descricao) values (6908, 'Remessa de bem por conta de contrato de comodato');
insert into vendas.tb_cfop (codigo, descricao) values (6909, 'Retorno de bem recebido por conta de contrato de comodato');
insert into vendas.tb_cfop (codigo, descricao) values (6910, 'Remessa em bonificacao, doacao ou brinde');
insert into vendas.tb_cfop (codigo, descricao) values (6911, 'Remessa de amostra gratis');
insert into vendas.tb_cfop (codigo, descricao) values (6912, 'Remessa de mercadoria ou bem p/ demonstracao');
insert into vendas.tb_cfop (codigo, descricao) values (6913, 'Retorno de mercadoria ou bem recebido p/ demonstracao');
insert into vendas.tb_cfop (codigo, descricao) values (6914, 'Remessa de mercadoria ou bem p/ exposicao ou feira');
insert into vendas.tb_cfop (codigo, descricao) values (6915, 'Remessa de mercadoria ou bem p/ conserto ou reparo');
insert into vendas.tb_cfop (codigo, descricao) values (6916, 'Retorno de mercadoria ou bem recebido p/ conserto ou reparo');
insert into vendas.tb_cfop (codigo, descricao) values (6917, 'Remessa de mercadoria em consignacao mercantil ou industrial');
insert into vendas.tb_cfop (codigo, descricao) values (6918, 'Devolucao de mercadoria recebida em consignacao mercantil ou industrial');
insert into vendas.tb_cfop (codigo, descricao) values (6919, 'Devolucao simbolica de mercadoria vendida ou utilizada em processo industrial, recebida anteriormente em consignacao mercantil ou industrial');
insert into vendas.tb_cfop (codigo, descricao) values (6920, 'Remessa de vasilhame ou sacaria');
insert into vendas.tb_cfop (codigo, descricao) values (6921, 'Devolucao de vasilhame ou sacaria');
insert into vendas.tb_cfop (codigo, descricao) values (6922, 'Lancamento efetuado a titulo de simples faturamento decorrente de venda p/ entrega futura');
insert into vendas.tb_cfop (codigo, descricao) values (6923, 'Remessa de mercadoria por conta e ordem de terceiros, em venda a ordem ou em operacoes com armazem geral ou deposito fechado');
insert into vendas.tb_cfop (codigo, descricao) values (6924, 'Remessa p/ industrializacao por conta e ordem do adquirente da mercadoria, quando esta nao transitar pelo estabelecimento do adquirente');
insert into vendas.tb_cfop (codigo, descricao) values (6925, 'Retorno de mercadoria recebida p/ industrializacao por conta e ordem do adquirente da mercadoria, quando aquela nao transitar pelo estabelecimento do adquirente');
insert into vendas.tb_cfop (codigo, descricao) values (6929, 'Lancamento efetuado em decorrencia de emissao de documento fiscal relativo a operacao ou prestacao tambem registrada em equipamento Emissor de Cupom Fiscal - ECF');
insert into vendas.tb_cfop (codigo, descricao) values (6931, 'Lancamento efetuado em decorrencia da responsabilidade de retencao do imposto por substituicao tributaria, atribuida ao remetente ou alienante da mercadoria, pelo servico de transporte realizado por transportador autonomo ou por transportador nao inscrito na UF onde iniciado o servico');
insert into vendas.tb_cfop (codigo, descricao) values (6932, 'Prestacao de servico de transporte iniciada em UF diversa daquela onde inscrito o prestador');
insert into vendas.tb_cfop (codigo, descricao) values (6933, 'Prestacao de servico tributado pelo Imposto Sobre Servicos de Qualquer Natureza ');
insert into vendas.tb_cfop (codigo, descricao) values (6934, 'Remessa simbolica de mercadoria depositada em armazem geral ou deposito fechado');
insert into vendas.tb_cfop (codigo, descricao) values (6949, 'Outra saida de mercadoria ou prestacao de servico nao especificado');
insert into vendas.tb_cfop (codigo, descricao) values (7101, 'Venda de producao do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (7102, 'Venda de mercadoria adquirida ou recebida de terceiros');
insert into vendas.tb_cfop (codigo, descricao) values (7105, 'Venda de producao do estabelecimento, que nao deva por ele transitar');
insert into vendas.tb_cfop (codigo, descricao) values (7106, 'Venda de mercadoria adquirida ou recebida de terceiros, que nao deva por ele transitar');
insert into vendas.tb_cfop (codigo, descricao) values (7127, 'Venda de producao do estabelecimento sob o regime de drawback ');
insert into vendas.tb_cfop (codigo, descricao) values (7129, 'Venda de producao do estabelecimento ao mercado externo de mercadoria industrializada sob o amparo do Regime Aduaneiro Especial de Entreposto Industrial (Recof-Sped)');
insert into vendas.tb_cfop (codigo, descricao) values (7201, 'Devolucao de compra p/ industrializacao ou producao rural');
insert into vendas.tb_cfop (codigo, descricao) values (7202, 'Devolucao de compra p/ comercializacao');
insert into vendas.tb_cfop (codigo, descricao) values (7205, 'Anulacao de valor relativo a aquisicao de servico de comunicacao');
insert into vendas.tb_cfop (codigo, descricao) values (7206, 'Anulacao de valor relativo a aquisicao de servico de transporte');
insert into vendas.tb_cfop (codigo, descricao) values (7207, 'Anulacao de valor relativo a compra de energia eletrica');
insert into vendas.tb_cfop (codigo, descricao) values (7210, 'Devolucao de compra p/ utilizacao na prestacao de servico');
insert into vendas.tb_cfop (codigo, descricao) values (7211, 'Devolucao de compras p/ industrializacao sob o regime de drawback ');
insert into vendas.tb_cfop (codigo, descricao) values (7212, 'Devolucao de compras para industrializacao sob o regime de Regime Aduaneiro Especial de Entreposto Industrial (Recof-Sped)');
insert into vendas.tb_cfop (codigo, descricao) values (7251, 'Venda de energia eletrica p/ o exterior');
insert into vendas.tb_cfop (codigo, descricao) values (7301, 'Prestacao de servico de comunicacao p/ execucao de servico da mesma natureza');
insert into vendas.tb_cfop (codigo, descricao) values (7358, 'Prestacao de servico de transporte');
insert into vendas.tb_cfop (codigo, descricao) values (7501, 'Exportacao de mercadorias recebidas com fim especifico de exportacao');
insert into vendas.tb_cfop (codigo, descricao) values (7551, 'Venda de bem do ativo imobilizado');
insert into vendas.tb_cfop (codigo, descricao) values (7553, 'Devolucao de compra de bem p/ o ativo imobilizado');
insert into vendas.tb_cfop (codigo, descricao) values (7556, 'Devolucao de compra de material de uso ou consumo');
insert into vendas.tb_cfop (codigo, descricao) values (7651, 'Venda de combustivel ou lubrificante de producao do estabelecimento');
insert into vendas.tb_cfop (codigo, descricao) values (7654, 'Venda de combustivel ou lubrificante adquiridos ou recebidos de terceiros');
insert into vendas.tb_cfop (codigo, descricao) values (7667, 'Venda de combustivel ou lubrificante a consumidor ou usuario final');
insert into vendas.tb_cfop (codigo, descricao) values (7930, 'Lancamento efetuado a titulo de devolucao de bem cuja entrada tenha ocorrido sob amparo de regime especial aduaneiro de admissao temporaria');
insert into vendas.tb_cfop (codigo, descricao) values (7949, 'Outra saida de mercadoria ou prestacao de servico nao especificado');

drop schema IF EXISTS crm cascade;
create schema crm;
create table crm.tb_categoria_negociacao (
	id integer not null,
	descricao varchar (50) not null
);
ALTER TABLE crm.tb_categoria_negociacao ADD PRIMARY KEY (id);

insert into crm.tb_categoria_negociacao values (0, 'PROPOSTA_CLIENTE');
insert into crm.tb_categoria_negociacao values (1, 'PRIMEIRO_CONTATO');
insert into crm.tb_categoria_negociacao values (2, 'POTENCIAIS');
insert into crm.tb_categoria_negociacao values (3, 'PROJETOS');
insert into crm.tb_categoria_negociacao values (4, 'PROVAVEIS');
insert into crm.tb_categoria_negociacao values (5, 'ESPECIAIS');

create table crm.tb_situacao_negociacao (
	id integer not null,
	descricao varchar (50) not null
);
ALTER TABLE crm.tb_situacao_negociacao ADD PRIMARY KEY (id);

insert into crm.tb_situacao_negociacao values (0, 'ABERTO');
insert into crm.tb_situacao_negociacao values (1, 'ACEITO');
insert into crm.tb_situacao_negociacao values (2, 'ANDAMENTO');
insert into crm.tb_situacao_negociacao values (3, 'CANCELADO');

create table crm.tb_tipo_nao_fechamento (
	id integer not null,
	descricao varchar (50) not null
);
ALTER TABLE crm.tb_tipo_nao_fechamento ADD PRIMARY KEY (id);

insert into crm.tb_tipo_nao_fechamento values (0, 'OK');
insert into crm.tb_tipo_nao_fechamento values (1, 'PRECO');
insert into crm.tb_tipo_nao_fechamento values (2, 'FRETE');
insert into crm.tb_tipo_nao_fechamento values (3, 'FORMA_PAGAMENTO');
insert into crm.tb_tipo_nao_fechamento values (4, 'PRAZO_ENTREGA');
insert into crm.tb_tipo_nao_fechamento values (5, 'OUTROS');

create table crm.tb_negociacao (
	id integer not null,
	id_cliente integer not null,
	id_orcamento integer not null,
	id_situacao_negociacao integer not null,
	id_tipo_nao_fechamento integer not null,
	id_vendedor integer not null,
	id_categoria_negociacao integer not null,
	observacao varchar(1000) default null,
	data_encerramento date default null,
	nome_cliente varchar(150) not null,
	nome_contato varchar(100) default null,
	telefone_contato varchar(15) default null,
	indice_conversao_valor numeric(15, 5) default 0,
	indice_conversao_quantidade numeric(15, 5) default 0
);
ALTER TABLE crm.tb_negociacao ADD PRIMARY KEY (id);
ALTER TABLE crm.tb_negociacao ADD CONSTRAINT id_situacao_negociacao FOREIGN KEY (id_situacao_negociacao) REFERENCES crm.tb_situacao_negociacao(id);
ALTER TABLE crm.tb_negociacao ADD CONSTRAINT id_categoria_negociacao FOREIGN KEY (id_categoria_negociacao) REFERENCES crm.tb_categoria_negociacao(id);
ALTER TABLE crm.tb_negociacao ADD CONSTRAINT id_tipo_nao_fechamento  FOREIGN KEY (id_tipo_nao_fechamento) REFERENCES crm.tb_tipo_nao_fechamento (id);
ALTER TABLE crm.tb_negociacao ADD CONSTRAINT id_orcamento FOREIGN KEY (id_orcamento) REFERENCES vendas.tb_pedido(id);
create index idx_negociacao_id_vendedor on crm.tb_negociacao (id_vendedor);
create index idx_negociacao_id_cliente on crm.tb_negociacao (id_cliente);

create sequence crm.seq_negociacao_id increment by 1 minvalue 1 no maxvalue start with 1;

create table crm.tb_indicador_cliente (
	id_cliente integer not null,
	indice_conversao_valor numeric(15, 5) not null default 0,
	indice_conversao_quantidade numeric(15, 5) not null default 0,
	valor_medio numeric(15, 5) not null default 0,
	quantidade_orcamentos integer not null default 0,
	quantidade_vendas integer not null default 0,
	valor_vendas numeric(15, 5) not null default 0,
	valor_orcamentos numeric(15, 5) not null default 0
);
ALTER TABLE crm.tb_indicador_cliente ADD PRIMARY KEY (id_cliente);

create index idx_nfe_pedido_numero_associado on vendas.tb_nfe_pedido (numero_associado);
drop index if exists vendas.idx_nfe_pedido_id_pedido ;
drop index if exists vendas.idx_pedido_id_pedido ;
drop index if exists vendas.idx_pedido_nfe_numero_triang;

alter table vendas.tb_nfe_pedido add constraint id_pedido foreign key (id_pedido ) references vendas.tb_pedido (id);
