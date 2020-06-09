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
insert into tb_forma_material values (5, 'PC', 'PE�A', 0.0);

insert into tb_situacao_pedido values (0, 'PEDIDO EM DIGITA��O');
insert into tb_situacao_pedido values (1, 'OR�AMENTO');
insert into tb_situacao_pedido values (2, 'PEDIDO ENVIADO');
insert into tb_situacao_pedido values (3, 'PEDIDO CANCELADO');

insert into tb_finalidade_pedido values ('INDUSTRIALIZACAO', 'INSDUSTRIALIZA��O');
insert into tb_finalidade_pedido values ('CONSUMO', 'CONSUMO');
insert into tb_finalidade_pedido values ('REVENDA', 'REVENDA');

insert into tb_tipo_entrega values (0, 'CIF', 'PEDIDO ENTREGUE NO CLIENTE');
insert into tb_tipo_entrega values (1, 'CIF_TRANS', 'PEDIDO ENTREGUE NA TRANPORTADORA PARA REDESPACHO');
insert into tb_tipo_entrega values (2, 'FOB', 'PEDIDO RETIRADO PELO CLIENTE');

insert into tb_tipo_venda values (0, 'K', 'VENDA POR KILO');
insert into tb_tipo_venda values (1, 'P', 'VENDA POR PE�A');

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
insert into vendas.tb_finalidade_pedido(id, descricao) values ('DEVOLUCAO', 'DEVOLU��O');
insert into vendas.tb_finalidade_pedido(id, descricao) values ('AMOSTRA_GRATIS', 'AMOSTRA GR�TIS');
insert into vendas.tb_finalidade_pedido(id, descricao) values ('REMESSA_INDUSTRIALIZACAO', 'REMESSA P/ INDUSTRIALIZA��O');
insert into vendas.tb_finalidade_pedido(id, descricao) values ('REMESSA_CONSERTO', 'REMESSA P/ CONSERTO');
insert into vendas.tb_finalidade_pedido(id, descricao) values ('REMESSA_ANALISE', 'REMESSA P/ AN�LISE');
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
