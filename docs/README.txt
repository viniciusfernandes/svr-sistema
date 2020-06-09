========== CRIACAO DO BANCO DE DADOS - ESQUEMA ENDERECAMENTO =======================================

1) Devemos construir o esquema de enderecamento via  "php-pg-admin"
2) Utilizar o "wiscp" para copiar os arquivos para a constru��o do esquema "criacao_tabela_enderecamento.sql" e "update_tabela_enderecamento.sql"
via para o diretorio /home
3) Abriremos uma conexao SSH via putty para execu��o do script via terminal do PostgreSQL. Para isso vamos utilizar 
host=184.107.24.87, porta=22, usuario=root, senha=XnJ@!1uK
4) Ir ate o diretorio /home e l� estarao os arquivos "criacao_tabela_enderecamento.sql" e "update_tabela_enderecamento.sql"
5) Alternar para o usuario "postgres", para isso rodar o comando "su postgres"
5.1) Criar o banco de dados atrav�s do comando CREATE DATABASE <meu banco de dados>;
5.2) Alternar para o banco de dados que acabou de criar atrav�s do comando \c <meu banco de dados>;
5.3) Para executar os scripts de cria��o do banco devemos utilzar \i <meu arquivo.sql>
5.4) � muito importante executar o comando especificando o caminho at� o diret�rio utilizando o slash ("/")
6) Abrir o terminal rodando o "psql" e executar a importacao atraves do comando \i <arquivo-enderecamento.sql> \encoding UTF-8,
	lembrando que o caminho do arquivo deve ser crescrito atrav�s de barras invertidas "/".
7) E depois o comando para atualizar as tabelas \i <arquivo-vendas.sql> \encoding UTF-8
8) sair
9) EXEMPLO:
drop schema vendas cascade;
drop schema enderecamento cascade;
create schema vendas;
create schema enderecamento;
\i C:/Users/vinicius/ambiente_trabalho/temp/dump.sql \encoding UTF-8;

========== PROBLEMAS RECORRENTES =======================================
1) Conflitos com vers�es do java: certifique-se de que os compiladores para exportar o projeto est�o na vers�o 6 da jdk. J� o projeto com os testes
	unit�rios devem ser compilados e executados com a vers�o 8 da jdk para que a API de Mock execute.
2) Teste unit�rio: certifique-se que esse projeto cont�m a jdk na biblioteca, pois sua execu��o n�o ocorrer� se no lugar da 
	jdk contiver a jre.
2) O sistema n�o efetua login: em alguns momentos o banco de dados apresenta algum problema de socket e n�o conseguimos efetuar a comunica��o 
	com o banco, o que fica claro quando tentamos acessar o sistema via o browser. Temos que reiniciar ou reinstalar o banco.
3) Problemas lan�ando exce��o do tipo Spring, VRaptor, etc: Verificar se os projetos est�o compilando ou com algum problema de configura��o.
Fechar os projetos e abri-los novamente e tente fechar e abrir novamente o Eclipse, funcionou da ultima vez que o problema surgiu. 
com.caelum.vraptor.proxy.ProxyInvocationException: org.springframework.beans.factory.BeanCurrentlyInCreationException

=========== COMANDOS PROMPT WINDOWS ===================
1) Para remover arquivos recursivamente
for /d /r . %d in (<padrao do nomedo arquivo>) do @if exist "%d" rd /s/q "%d"

================ CONFUIGURACAO DE ENVIO DE EMAIL DO HOTMAIL ==================
Use as configura��es a seguir no seu aplicativo de email. 

    Servidor de entrada (POP3)
        Endere�o do servidor: pop-mail.outlook.com
        Porta: 995
        Conex�o criptografada: SSL 
    Servidor de sa�da (SMTP)
        Endere�o do servidor: smtp-mail.outlook.com
        Porta: 25 (ou 587 se a 25 estiver bloqueada)
        Autentica��o: Sim
        Conex�o criptografada: TLS 
    Nome de usu�rio: seu endere�o de email
    Senha: sua senha
    email.setTLS(true);