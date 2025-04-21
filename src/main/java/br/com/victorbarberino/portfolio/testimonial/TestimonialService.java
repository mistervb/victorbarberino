package br.com.victorbarberino.portfolio.testimonial;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TestimonialService {
    public List<TestimonialData> getAllTestimonials() {
        return new ArrayList<>(List.of(new TestimonialData(UUID.randomUUID(), "Victor desenvolveu uma solução excepcional para nossa empresa. Sua expertise técnica e profissionalismo são notáveis.", "João Silva, CEO TechCorp")));
    }
}
