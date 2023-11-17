package org.example;
import org.apache.commons.lang3.StringUtils;
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
                if (event.split(" ").length == 2){
                    Object o = null;
                    try {
                        o = new JSONParser().parse(new FileReader("user_data.json"));
                    } catch (IOException | ParseException e) {
                        throw new RuntimeException(e);
                    }
                    JSONObject j = (JSONObject) o;
                    String zone = (String) j.get(event.split(" ")[1]);
                    String hour = String.valueOf(ZonedDateTime.now(ZoneId.of(zone)).getHour());
                    String minute = String.valueOf(ZonedDateTime.now(ZoneId.of(zone)).getMinute());
                    String seconds = String.valueOf(ZonedDateTime.now(ZoneId.of(zone)).getSecond());
                    System.out.println(zone);
                    return ("time: " + LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute), Integer.parseInt(seconds)));

                }
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

                String[] zones = {"GMT", "CET", "EET", "Europe/Amsterdam", "Europe/Moscow", "Asia/Dubai", "Asia/Riyadh", "Asia/Kolkata", "Asia/Dhaka",
                        "Asia/Bangkok", "Asia/Shanghai", "Asia/Tokyo", "Australia/Sydney", "Pacific/Auckland",
                        "Pacific/Honolulu", "America/Anchorage", "America/Denver", "America/Chicago",
                        "Europe/Lisbon", "Atlantic/Azores"};
                if (event.split(" ").length <= 2 && event.split(" ")[1].equals("zones")) {
                    System.out.println("here we are :0");
                    return StringUtils.join(zones, "\n");
                }
                System.out.print(event.split(" ").length <= 2 );

                if (event.split(" ").length <= 2){
                    return "try &new @<someone> <zone>";
                }


                if (event.split(" ")[1].startsWith("@", 1)) {
                    System.out.println("works to so far");

                    List list_zones = Arrays.asList(zones);
                    if (!list_zones.contains(event.split(" ")[2])){
                        return "it needs to contain a valit zone\ndo <&new zones> for the list";
                    }
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
//            case "test":
//                Button button1 = Button.primary("hello_again", "Hello Again");
//                Button button2 = Button.primary("lmfao", "LMFAO");
//
//                // Create ActionRow with buttons
//                ActionRow actionRow = ActionRow.of(button1, button2);
//
//                // Send message with buttons
//                MessageAction messageAction = event.getMessage().reply("Hello with buttons").addActionRow(actionRow);
//                Message message = messageAction.complete();
//
//                // Print messages to command line
//                System.out.println("Sent mss 1: " + message.getContentRaw());
//                System.out.println("Sent mss 2: " + message.getId());
//
//                // Return null as the response has been sent via buttons
//                return null;
//        }



            default:
                return null;
        }
    }
}
