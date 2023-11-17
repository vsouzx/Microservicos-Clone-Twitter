# Clone Twitter com Microsservicos [![My Skills](https://skillicons.dev/icons?i=twitter,spring)](https://skillicons.dev)  

<p>Backend do "clone" do Twitter, usando arquitetura de microsservicos com Spring.</p>
<p>O <b>frontend</b> está sendo feito em Angular, pelo <a href="https://github.com/souzxvini?tab=repositories">Vinicius Souza</a> !</p>


# Serviços implementados:
<p><b>ConfigServer</b> ✔️</p> 
<p><b>ConfigServer Repository</b>✔️ <a href="https://github.com/vsouzx/Microservicos-Clone-Twitter-Repository"> Link do repositorio</a></p>
<p><b>Discovery</b> ✔️</p>
<p><b>Gateway</b> (com Spring WebFlux) ✔️</p>
<p>Micro Serviço de <b>autenticação</b> (com Redis) ✔️</p>
<p>Micro Serviço de <b>gerenciamento de contas</b> (Criar conta, seguir/bloquear/silenciar outra conta, deixar a conta privada)✔️</p>
<p>Micro Serviço de <b>email </b>(sendo um Consumer Kafka) ✔️</p>
<p>Micro Serviço de <b>feed </b>(posts, retweets, likes, comentarios, timeline, for you, etc) ✔️ </p>
<p>Micro Serviço de <b>notificações</b> ✔️ (Server-Sent Events)</p>
<p>Micro Serviço de <b>DM (Chat)</b> ✔️ (Websocket)</p>

# Design Patterns usados:
<p>Singleton</p>
<p>Strategy</p>
<p>Factory</p>
<p>Builder</p>
<p>Data Transfer Objects</p>

# Tecnologias usadas:
<p>Java</p>
<p>Spring Boot (Security, JPA, Web)</p>
<p>Spring Cloud Netflix (Config Server, Discovery, Gateway)</p>
<p>Spring Cloud Open Feign</p>
<p>Spring WebSockets</p>
<p>Server-Sent Events</p>
<p>Redis</p>
<p>Docker</p>
<p>SQL</p>
<p>Mensageria com Apache Kafka</p>
<p>AWS SDK Bucket S3</p>
