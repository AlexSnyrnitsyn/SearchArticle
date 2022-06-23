package com.example.ArticleSearch.service;

import com.example.ArticleSearch.model.Article;
import com.example.ArticleSearch.model.Category;
import com.example.ArticleSearch.repository.ArticleRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private JSONParser parser = new JSONParser();
    private static final String DATA_FILE = "C:\\Users\\Alexandro\\Desktop\\wikiDump\\ruwikiquote-20220613-cirrussearch-general.json";


    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public void addArticle() {

        List<JSONObject> jsonObject = new ArrayList<>();

        parseFile().stream().forEach(line -> {
            try {
                jsonObject.add((JSONObject) parser.parse(line));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        for (JSONObject elem : jsonObject){
            if (elem.containsKey("timestamp")) {
                Article article = new Article();
                String auxiliaryText = elem.get("auxiliary_text") == null ? "" : elem.get("auxiliary_text").toString();
                article.setAuxiliaryText(auxiliaryText);
                article.setTitle((String) elem.get("title"));
                article.setCreateTimestamp(transformToUnixTime(elem.get("create_timestamp").toString()));
                article.setWiki((String) elem.get("wiki"));
                article.setTimestamp(transformToUnixTime(elem.get("timestamp").toString()));
                article.setLanguage((String) elem.get("language"));
                article.setCategories(parseCategory((JSONArray) elem.get("category")));
                articleRepository.save(article);
            }
        }
    }

    @Override
    public Article getArticle(String title) {
        return articleRepository.findByTitle(title);
    }


    private List<String> parseFile() {
        List<String> dataList = new ArrayList<>();

        try {
            dataList = Files.readAllLines(Paths.get(DATA_FILE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dataList;
    }

    private long transformToUnixTime(String timestamp) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(timestamp);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date.getTime()/1000;
    }

    private List<Category> parseCategory(JSONArray categoryArray){
        List<Category> categoryList = new ArrayList<>();
        categoryArray.forEach(item -> {
            Category category = new Category();
            category.setName((String) item);
            categoryList.add(category);
        });
        return categoryList;
    }
}
