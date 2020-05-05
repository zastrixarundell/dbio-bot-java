# DBIO bot example
A JavaCord bot as an example for the DBIO library.

[![Build Status](https://travis-ci.com/zastrixarundell/dbio-bot.svg?branch=master)](https://travis-ci.com/zastrixarundell/dbio)

![discord.bio homepage](https://raw.githubusercontent.com/zastrixarundell/dbio/master/assets/home.png)

## So, where's the code?
This bot was a made as an single-class example which sends the discord.bio information of an user/command-caller to the channel where the `.info` command is called. The code can be found [here](https://github.com/zastrixarundell/dbio-bot/blob/master/src/main/java/bio/discord/dbiobot/DbioBot.java).

If you're too lazy to click that, you can see it here:
```java
public class DbioBot

{
    public static void main(String[] args)
    {
        String token = System.getenv("DISCORD_BOT_TOKEN");

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        api.addMessageCreateListener(messageCreateEvent -> {
            if(!messageCreateEvent.getMessageAuthor().isRegularUser())
                return;

            String content = messageCreateEvent.getMessageContent();

            if(!content.startsWith(".info"))
                return;

            Dbio.getUserDetails(messageCreateEvent.getMessageAuthor().getIdAsString()).thenAcceptAsync(user ->
                    user.ifPresent(theUser -> sendUserInfo(theUser, messageCreateEvent)));
        });
    }

    private static void sendUserInfo(User user, MessageCreateEvent event)
    {
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(user.getDiscordInformation().getFullUserUsername())
                .setThumbnail(user.getDiscordInformation().getAvatarUrl("png"))
                .setDescription(user.getSettings().getDescription())
                .addInlineField("Location", user.getSettings().getLocation())
                .addInlineField("Gender", user.getSettings().getGender().name())
                .addInlineField("Occupation", user.getSettings().getOccupation());

        event.getChannel().sendMessage(builder);
    }
}
```
