package com.example.ArticleSearch.service;

import com.example.ArticleSearch.model.Article;

public interface ArticleService {

    void addArticle();

    Article getArticle(String title);

}
