package com.app.java.WebAppJava.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Topic {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String content;
    private String type;
    private String videoUrl;
}
