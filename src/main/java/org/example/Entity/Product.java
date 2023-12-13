package org.example.Entity;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    private Photo photo;
    private Integer count = 0;
    private Double price;

}


