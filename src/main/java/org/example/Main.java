

package org.example;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;


public class Main extends ListenerAdapter {
    public static void main(String[] args) throws IOException, ParseException {
        Object o = new JSONParser().parse(new FileReader("settings.json"));
        JSONObject j = (JSONObject) o;

        String token = (String) j.get("token");

        try {
            JDA jda = JDABuilder.createDefault(token)
                    .addEventListeners(new Main())
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.getAuthor().isBot() && event.getMessage().getContentRaw().trim().startsWith("&", 0)) {
            String response = Response.getResponse(event.getMessage().getContentRaw(), event.getMessage().getAuthor().getId());
            if (response != null) {
                event.getChannel().sendMessage(response).queue();
            }
        }
    }
}
