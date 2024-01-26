package mynews;

import com.google.gson.Gson;
import lombok.Getter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Getter
public class NewsJsonParser extends BasicJsonParser {

    private MyNews myNews;

    public NewsJsonParser() {
        myNews = null;
    }

    public void loadFromUrl(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::parse)
                .join();

        System.out.println(url);
        System.out.println("뉴스 API 요청을 보냈습니다.");
        System.out.println("=========================");
    }

    private void parse(String s) {

        System.out.println("News Data를 받았습니다.");
        System.out.println("=========================");
        // System.out.println(s);

        Gson gson = new Gson();
        myNews = gson.fromJson(s, MyNews.class);

        // for (Article article : myNews.getArticles()) {
        //     System.out.println("작성자 : " + article.getAuthor());
        //     System.out.println("타이틀 : " + article.getTitle());
        //     System.out.println("요약 : " + article.getDescription());
        //     System.out.println("원문기사 : " + article.getUrl());
        //     System.out.println("원문이미지 : " + article.getUrlToImage());
        //     System.out.println("발행일 : " + article.getPublishedAt());
        //     System.out.println("출처 : " + article.getSource().getName());
        // }
    }
}
