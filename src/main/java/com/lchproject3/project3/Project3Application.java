package com.lchproject3.project3;


//import com.lchproject3.project3.global.discord.DiscordProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
//@EnableConfigurationProperties(DiscordProperties.class)
public class Project3Application {

	public static void main(String[] args) {

		SpringApplication.run(Project3Application.class, args);
	}

}
