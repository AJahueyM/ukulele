package dev.arbjerg.ukulele.jda

import dev.arbjerg.ukulele.data.Replies
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class ReplyListener() : ListenerAdapter() {
    private val replies: Replies = Replies()

    private val log: Logger = LoggerFactory.getLogger(EventHandler::class.java)

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (!event.author.isBot) {
            val channel = event.channel
            val member = event.member
            val message = event.message

            for(reply in replies.list){
                if(reply.first.containsMatchIn(message.contentRaw.toLowerCase())){
                    log.info("Replying ${reply.second} to ${message.contentRaw}")
                    var msg = MessageBuilder().append(reply.second).setTTS(true).build()
                    channel.sendMessage(msg).queue()
                }
            }
    }
    }

}