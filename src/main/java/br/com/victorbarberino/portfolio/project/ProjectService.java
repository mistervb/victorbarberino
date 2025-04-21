package br.com.victorbarberino.portfolio.project;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {
    public List<ProjectData> getAllProjects() {
        return new ArrayList<>(List.of(new ProjectData(UUID.randomUUID(), "Sistema Financeiro", "Desenvolvimento de sistema para gestão financeira utilizando Java e Spring Boot.", "https://via.placeholder.com/400x300", List.of("Java", "Spring Boot", "PostgreSQL"))));
    }
}
