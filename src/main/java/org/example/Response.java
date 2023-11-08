package org.example;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.github.dhiraj072.randomwordgenerator.RandomWordGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;


public class Response {
    public static String getResponse(String event, String id) {

        switch ((event.split(" "))[0]) {
            case "&hello":
                return "hello, there";

            case "&bye":
                return "cya";

            case "&time":
                String hour = String.valueOf(ZonedDateTime.now(ZoneId.of("Europe/Amsterdam")).getHour());
                String minute = String.valueOf(ZonedDateTime.now(ZoneId.of("Europe/Amsterdam")).getMinute());
                String seconds = String.valueOf(ZonedDateTime.now(ZoneId.of("Europe/Amsterdam")).getSecond());
                return ("time: " + LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute), Integer.parseInt(seconds)));

            case "&kill":
                Object o = null;
                try {
                    o = new JSONParser().parse(new FileReader("settings.json"));
                } catch (IOException | ParseException e) {
                    throw new RuntimeException(e);
                }
                JSONObject j = (JSONObject) o;
                List mods_id = (List) j.get("mods");
                if (mods_id.contains(id)) {
                    System.exit(0);
                } else {
                    return "```diff\n" +
                            "- you need mod to use this command\n" +
                            "```";
                }
            case "&word":
                return RandomWordGenerator.getRandomWord();
            case "&new":
                if (event.split(" ").length <= 2){
                    return "try &new @<someone> <zone>";
                }
                if (event.split(" ")[1].startsWith("@", 1)) {
                    System.out.println("works to so far");

                    File jsonFile = new File("user_data.json");
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        JsonNode rootNode = objectMapper.readTree(jsonFile);
                        if (rootNode.isObject()) {
                            ObjectNode objectNode = (ObjectNode) rootNode;
                            objectNode.put(event.split(" ")[1], event.split(" ")[2]);
                            objectMapper.writeValue(jsonFile, rootNode);
                            System.out.println("done");
                        } else {
                            System.err.println("The JSON data in the file is not an object.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(event);


            default:
                return null;
        }
    }
}
