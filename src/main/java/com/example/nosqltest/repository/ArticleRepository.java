package com.example.nosqltest.repository;

import com.example.nosqltest.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleRepository extends MongoRepository<Article, String> {
}
