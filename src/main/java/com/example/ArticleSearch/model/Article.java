package com.example.ArticleSearch.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(schema = "public", name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_timestamp")
    private long createTimestamp;

    @Column(name = "timestamp")
    private long timestamp;

    @Column(name = "language")
    private String language;

    @Column(name = "wiki")
    private String wiki;

    @Column(name = "title")
    private String title;

    @Column(name = "auxiliary_text", length = 10485760)
    private String auxiliaryText;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "article_category",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;
}
