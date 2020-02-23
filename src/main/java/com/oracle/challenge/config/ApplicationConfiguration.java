package com.oracle.challenge.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("/messages.properties")
public class ApplicationConfiguration {}