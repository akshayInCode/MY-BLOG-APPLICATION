package com.myblog.entity;


import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

    @Entity
    @Table(name = "roles")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class Role {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        @ManyToMany(mappedBy = "roles")
        private Set<User> users = new HashSet<>();


    }



