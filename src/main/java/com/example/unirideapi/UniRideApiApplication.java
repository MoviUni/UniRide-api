package com.example.unirideapi;

import com.example.unirideapi.model.Conductor;
import com.example.unirideapi.model.Ruta;
import com.example.unirideapi.model.enums.EstadoRuta;
import com.example.unirideapi.repository.RutaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SpringBootApplication
public class UniRideApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniRideApiApplication.class, args);
    }

}
