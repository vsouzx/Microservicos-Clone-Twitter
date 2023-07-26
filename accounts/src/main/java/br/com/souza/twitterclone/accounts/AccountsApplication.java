package br.com.souza.twitterclone.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;

@EnableDiscoveryClient
@EnableKafka
@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class }) //para reduzir tamanho do log inicial
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
