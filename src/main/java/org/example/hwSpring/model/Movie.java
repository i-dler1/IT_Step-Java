package org.example.hwSpring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    private Long id;
    private String title;
    private Integer year;
    private String genre;
    private Double rating;
}
