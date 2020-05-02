package bio.discord.dbiobot;

import bio.discord.dbio.Dbio;
import bio.discord.dbio.entities.User;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class DbioBot implements MessageCreateListener
{

    public static void main(String[] args)
    {
        String token = System.getenv("DISCORD_BOT_TOKEN");

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        api.addMessageCreateListener(new DbioBot());
    }

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent)
    {
        if(!messageCreateEvent.getMessageAuthor().isRegularUser())
            return;

        String content = messageCreateEvent.getMessageContent();

        if(!content.startsWith(".info"))
            return;

        Dbio.getUserDetails(messageCreateEvent.getMessageAuthor().getIdAsString()).thenAcceptAsync(user ->
                user.ifPresent(theUser -> sendUserInfo(theUser, messageCreateEvent)));
    }

    private void sendUserInfo(User user, MessageCreateEvent event)
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
