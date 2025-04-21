package br.com.victorbarberino.portfolio.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectData {
    private UUID projectId;
    private String title;
    private String description;
    private String image;
    private List<String> tags;
}
