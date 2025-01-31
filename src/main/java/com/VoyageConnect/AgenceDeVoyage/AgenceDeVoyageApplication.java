package com.VoyageConnect.AgenceDeVoyage;

import com.VoyageConnect.AgenceDeVoyage.entity.Destination;

import com.VoyageConnect.AgenceDeVoyage.entity.Role;
import com.VoyageConnect.AgenceDeVoyage.repository.RoleRepository;
import com.VoyageConnect.AgenceDeVoyage.repository.DestinationRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
@EnableTransactionManagement // Add this annotation
public class AgenceDeVoyageApplication implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final DestinationRepository destinationRepository;

    @Autowired
    private DataSource dataSource;

    public AgenceDeVoyageApplication(RoleRepository roleRepository,DestinationRepository destinationRepository) {
        this.roleRepository = roleRepository;
        this.destinationRepository = destinationRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(AgenceDeVoyageApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Base de données connectée avec succès!");
            System.out.println("URL: " + connection.getMetaData().getURL());
            System.out.println("Username: " + connection.getMetaData().getUserName());
            
            // Insertion des rôles si nécessaire
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role("ROLE_ADMIN"));
                roleRepository.save(new Role("ROLE_CLIENT"));
                System.out.println("Roles insérés avec succès!");
            } else {
                System.out.println("Les roles existent déjà.");
            }
        } catch (Exception e) {
            System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
