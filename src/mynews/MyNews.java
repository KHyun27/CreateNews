package mynews;

import lombok.Data;

@Data
public class MyNews {

    private String status;
    private String totalResults;
    private Article[] articles;    // 배열

}
