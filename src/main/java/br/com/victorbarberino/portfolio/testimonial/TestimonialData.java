package br.com.victorbarberino.portfolio.testimonial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestimonialData {
    private UUID testimonialId;
    private String text;
    private String author;
}
