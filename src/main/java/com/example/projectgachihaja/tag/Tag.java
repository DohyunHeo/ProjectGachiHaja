package com.example.projectgachihaja.tag;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;
}
