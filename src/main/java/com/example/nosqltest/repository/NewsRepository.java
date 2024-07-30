package com.example.nosqltest.repository;

import com.example.nosqltest.model.News;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NewsRepository extends MongoRepository<News, String> {
}
