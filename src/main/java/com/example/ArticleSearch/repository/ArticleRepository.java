package com.example.ArticleSearch.repository;

import com.example.ArticleSearch.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Article findByTitle(String title);

}
