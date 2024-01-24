package mynews;

import lombok.Getter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Getter
public class BasicJsonParser {

    private final StringBuilder builder = new StringBuilder();

    public void loadFromFile(String fileName) throws IOException, FileNotFoundException {
        try (FileReader reader = new FileReader(fileName)) {
            char[] buffer = new char[1024];
            int readCount = 0;

            while ((readCount = reader.read(buffer)) != -1) {
                builder.append(buffer, 0, readCount);
            }
        }
    }
}
