package mynews;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Optional;

public class CreateNews {
    public static void main(String[] args) {

        System.out.println("뉴스 API를 이용해 News데이터를 데이터베이스에 입력하기");

        String databaseUrl = "jdbc:mysql://localhost:3306/my_news?serverTimezone=Asia/Seoul";
        String user = "root";
        String password = "1234";

        DatabaseConnector dbConnector = DatabaseConnector.getInstance();

        // https://newsapi.org/v2/top-headlines?country=kr&category=business&apiKey=dfb3950cea6649149158a8d7f241a9a6

        Gson gson = new Gson();
        try {
            dbConnector.init(databaseUrl, user, password);
            NewsConfig config = gson.fromJson(new FileReader("src/mynews/news_config.json"), NewsConfig.class);

            NewsDB newsDB = new NewsDB(dbConnector);
            long countryId = newsDB.queryCountryId(config.country());
            // System.out.println(countryId);
            long categoryId = newsDB.queryCategoryId(config.category());
            // System.out.println(categoryId);

            // News API URL을 만든다.
            String url = String.format("https://newsapi.org/v2/top-headlines?country=%s&category=%s&apiKey=%s", config.country(), config.category(), config.key());

            NewsJsonParser newsClient = new NewsJsonParser();
            newsClient.loadFromUrl(url);

            for (Article article : newsClient.getMyNews().getArticles()) {
                long sourceId = newsDB.querySourceId(article.getSource().getName());

                String sql = String.format("insert into article(author, title, description, url, url_to_image, published_at, country_id, category_id, source_id) " +
                                "values('%s','%s','%s','%s','%s',str_to_date('%s','%%Y-%%m-%%dT%%H:%%i:%%sZ'),%d,%d,%d);",
                        Optional.ofNullable(article.getAuthor()).orElse("").replace("'", "\\'"),
                        Optional.ofNullable(article.getTitle()).orElse("").replace("'", "\\'"),
                        Optional.ofNullable(article.getDescription()).orElse("").replace("'", "\\'"),
                        URLEncoder.encode(article.getUrl(), StandardCharsets.UTF_8),
                        URLEncoder.encode(article.getUrlToImage(), StandardCharsets.UTF_8),
                        article.getPublishedAt(),
                        countryId,
                        categoryId,
                        sourceId);

                if (dbConnector.getStatement().executeUpdate(sql) == 0) {
                    System.out.println("Insert SQL Error : " + article.getTitle());
                }

            }

        } catch (FileNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                dbConnector.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
