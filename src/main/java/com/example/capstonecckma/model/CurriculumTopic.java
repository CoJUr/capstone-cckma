package com.example.capstonecckma.model;

import javax.persistence.*;

@Entity
@Table(name = "curriculum_topic")
public class CurriculumTopic {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(columnDefinition = "INT UNSIGNED NOT NULL")
    private long id;
    @Column(length = 100, nullable = false, unique = true)
    private String title;
    @Column(length = 100, nullable = false, unique = true)
    private String description;

    public CurriculumTopic() {
    }

    public CurriculumTopic(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CurriculumTopic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}