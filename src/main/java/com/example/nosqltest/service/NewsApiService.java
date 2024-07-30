package com.example.nosqltest.service;

import com.example.nosqltest.model.Article;
import com.example.nosqltest.model.News;
import com.example.nosqltest.repository.ArticleRepository;
import com.example.nosqltest.repository.NewsRepository;
import com.google.gson.Gson;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class NewsApiService {
    @Value("${newsApiKey}")
    private String apiKey;

    private NewsApiClient newsApiClient;
    private final Gson gson;

    private final NewsRepository newsRepository;
    private final ArticleRepository articleRepository;


    @PostConstruct
    public void init(){
        newsApiClient = new NewsApiClient(apiKey);
    }

    public String getEverything(){
        CompletableFuture<String> future = new CompletableFuture<>();
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("apple")
                        .from("2024-07-04")
                        .to("2024-07-04")
                        .sortBy("popularity")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        response.getArticles().forEach(apiArticle -> {
                            Article article = new Article();
                            article.setTitle(apiArticle.getTitle());
                            article.setDescription(apiArticle.getDescription());
                            article.setAuthor(apiArticle.getAuthor());
                            article.setContent(apiArticle.getContent());
                            articleRepository.save(article);
                            future.complete("News saved successfully");
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );
        try {
            return future.get(); // 결과 반환
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return "Failed to save articles";
        }
    }

    public String getTopHeadlines(){
        CompletableFuture<String> future = new CompletableFuture<>();
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .country("kr")
                        .category("health")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        response.getArticles().forEach(apiNews -> {
                            News news = new News();
                            news.setTitle(apiNews.getTitle());
                            news.setDescription(apiNews.getDescription());
                            news.setUrl(apiNews.getUrl());
                            news.setUrlToImage(apiNews.getUrlToImage());
                            news.setContent(apiNews.getContent());
                            newsRepository.save(news); // MongoDB에 기사 저장
                        });
                        future.complete("News saved successfully");
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        future.completeExceptionally(throwable);
                    }
                }
        );
        try {
            return future.get(); // 결과 반환
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return "Failed to save articles";
        }
    }

    public String getArticle(){
        List<Article> articles = articleRepository.findAll();
        return gson.toJson(articles);
    }
}
