package Utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T jsonToData(String body, Class<T> clazz) {
        try {
            return mapper.readValue(body, clazz);
        } catch (IOException e) {
            throw new RuntimeException("IOException while parsing the result");
        }
    }

    public static String dataToJson(Object data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (IOException e) {
            throw new RuntimeException("IOEXception while mapping object (" + data + ") to JSON. " + e.getMessage());
        }
    }
}
