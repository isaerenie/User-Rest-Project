package com.works.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;
    @Length(min = 2, max = 30, message = "Karakter uzunluğu minimum 2 maksimum 30 olmalıdır.")
    private String name;
    @Length(min = 2, max = 30, message = "Karakter uzunluğu minimum 2 maksimum 30 olmalıdır.")
    private String surname;

    @Column(unique = true, length = 150)
    private String email;

    private int age;
    @Column(length = 32)
    private String password;

}
