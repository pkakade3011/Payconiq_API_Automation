package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JsonUtils {

	public static Map<String, Object> readJsonFields(String file) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> mapObject = null;
		File jsonFile = loadJsonSchemaFile(file);

		try {
			mapObject = mapper.readValue(jsonFile, new TypeReference<Map<String, Object>>() {});
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return mapObject;
	}

	private static File loadJsonSchemaFile(String fileName) {
		String baseFilePath = "src/main/resources/jsonSchema/";
		File jsonFile = new File(baseFilePath + fileName);
		return jsonFile;
	}

}
