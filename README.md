# desafio-contas-a-pagar
Desafio Backend

Este é um aplicativo Java Spring boot, está dockerizado o app e o banco de dados.

Para executar a aplicação bastar executar um comando para buildar o projeto:

mvn clean package -DskipTests

Depois para subir a aplicação no docker basta utilizar o comando:

docker-compose up --build

(já está configurado para orquestrar e subir a base de dados primeiro, depois a aplicação)



Na raíz do projeto existe uma pasta chamada "ARQUIVOS DE TESTE" lá contém uma coleção de requisições para testar a aplicação! Pode ser importada no POSTMAN.
Também contém um arquivo CSV com algumas "Contas a pagar" para cadastrar na base de dados.

Qualquer dúvida estou à disposição!
