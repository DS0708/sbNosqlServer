package com.example.nosqltest.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class News {
    @Id
    private String id;
//    private Source source;  // Source 타입으로 변경
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
//    private LocalDateTime publishedAt;  // LocalDateTime 타입으로 변경
    private String content;
}
