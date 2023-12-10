# Objetivo

A aplicação eleicaows e realiza um gerenciamento de votação, onde cada associado possui um voto e as decisões são tomadas em assembleias, por votação.
Sendo disponibilizado recursos para:
 - Cadastrar uma nova pauta.
 - Abrir uma sessão de votação em uma pauta (devera ser informado o tempo em minutos que a sessão de votação deve ficar aberta ou 1 minuto por padrao)
 - Receber votos dos associados em pautas (os votos permitidos serao apenas 'Sim'/'Não'. Cada associado é identificado por um cpf único e pode votar apenas uma vez por pauta)
 - Contabilizar os votos e informar o resultado da votação de cada pauta.
 - Apos a pauta finalizada e notifica a fila pra proseguimento no processo eleitoral.

# Detalhes Técnicos

A eleicaows é desenvolvido com as seguintes tecnologias:

- java 17
- spring-boot 3.2
- lombok
- rabbitmq cloud heroku
- schedule
- mysql cloud heroku
- feign 
- spring data jpa
- interface swagger

# Execução

A api esta armazenada no heroku pelo site https://eleicaows-f202a66b2e1b.herokuapp.com/eleicao/swagger-ui/index.html
sendo possivel acionar o seu swagger para identificação dos metodos e parametros de entrada e saida