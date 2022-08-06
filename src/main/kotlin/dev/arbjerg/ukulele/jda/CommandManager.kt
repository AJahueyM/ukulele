package dev.arbjerg.ukulele.jda

import dev.arbjerg.ukulele.config.BotProps
import dev.arbjerg.ukulele.data.GuildPropertiesService
import dev.arbjerg.ukulele.data.Replies
import dev.arbjerg.ukulele.jda.CommandContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.EmbedType
import net.dv8tion.jda.api.MessageBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.random.Random
import com.microsoft.azure.cognitiveservices.vision.computervision.*
import com.microsoft.azure.cognitiveservices.vision.computervision.implementation.ComputerVisionImpl
import com.microsoft.azure.cognitiveservices.vision.computervision.models.*

@Service
class CommandManager(
        private val contextBeans: CommandContext.Beans,
        private val guildProperties: GuildPropertiesService,
        private val botProps: BotProps,
        private val replies: Replies,
        commands: Collection<Command>
) {
    private val subscriptionKey = botProps.azureToken
    private val endpoint = botProps.azureEndpoint
    private val compVisClient: ComputerVisionClient

    private final val registry: Map<String, Command>
    private val log: Logger = LoggerFactory.getLogger(CommandManager::class.java)

    init {
        compVisClient = authenticate(subscriptionKey, endpoint)
        val map = mutableMapOf<String, Command>()
        commands.forEach { c ->
            map[c.name] = c
            c.aliases.forEach { map[it] = c }
        }
        registry = map
        log.info("Registered ${commands.size} commands with ${registry.size} names")
        @Suppress("LeakingThis")
        contextBeans.commandManager = this
    }
    
    fun authenticate(subscriptionKey: String, endpoint: String): ComputerVisionClient{
        return ComputerVisionManager.authenticate(subscriptionKey).withEndpoint(endpoint)
    }

    fun describeRemoteImage(url: String?): String {
        var features = ArrayList<VisualFeatureTypes>()
        features.add(VisualFeatureTypes.TAGS)
        features.add(VisualFeatureTypes.DESCRIPTION)
        features.add(VisualFeatureTypes.ADULT)
        features.add(VisualFeatureTypes.BRANDS)
        features.add(VisualFeatureTypes.CATEGORIES)
        features.add(VisualFeatureTypes.COLOR)
        features.add(VisualFeatureTypes.FACES)
        features.add(VisualFeatureTypes.OBJECTS)

        try{
            //var description = compVisClient.computerVision().describeImage().withUrl(url).withVisualFeatures(features).execute()
            //var captions = description.captions()
            var analyze = compVisClient.computerVision().analyzeImage().withUrl(url).withVisualFeatures(features).execute()
            var captions = analyze.description().captions()

            /* log.info("TAGS")
            for(tag in analyze.tags()){
                log.info("${tag.confidence()}: ${tag.name()}")
            }
            log.info("ADULT")
            log.info("${analyze.adult().adultScore()}: ${analyze.adult().isAdultContent()}")
            log.info("BRANDS")
            for(tag in analyze.brands()){
                log.info("${tag.confidence()}: ${tag.name()}")
            }
            log.info("CATEGORIES")
            for(tag in analyze.categories()){
                log.info("${tag.score()}: ${tag.name()}")
            }
            log.info("FACES")
            for(tag in analyze.faces()){
                log.info("${tag.age()}: ${tag.gender()}")
            }
            log.info("CAPTIONS")
            for(caption in captions){
                log.info("${caption.confidence()}: ${caption.text()}")
            } */

            if (captions.size == 1) {
                var selectedCaption = captions[0]
                var answer = selectedCaption.text()

                log.info("Analyzed image to be ${answer} with ${selectedCaption.confidence()*100}% certainty")
                return answer
            } else if (captions.size > 0) {
                var selectedCaption = captions[Random.nextInt(captions.size - 1)]
                var answer = selectedCaption.text()

                log.info("Analyzed image to be ${answer} with ${selectedCaption.confidence()*100}% certainty")
                return answer
            } else {
                return "my brain is completely empty right now, ask me later"
            }
        }

        catch (e: Exception) {
            log.info("Failed at analyzing: ${e.message}")
            return "WHAT THE FUCK IS THAT, MY EYES ARE BURNING, WHY WOULD YOU DO THIS?!"
        }
    }

    operator fun get(commandName: String) = registry[commandName]

    fun getCommands() = registry.values.distinct()

    private val artWording = listOf(
            "Is that the mythical %s ?",
            "Am I wrong or is that %s ?",
            "Holy crap, you can't just post %s you weirdo",
            "What the hell is wrong with you, why are you showing us %s ?",
            "Oh my favorite, %s",
            "Heck yes, I love %s",
            "I don't even know what im looking at... is it %s or something weirder?",
            "Should I be seeing %s right now?"
    )

    fun respondImage(url: String?, channel: TextChannel, message: Message){
        var selectedWording = artWording[Random.nextInt(artWording.size - 1)]
        var description = selectedWording.format(describeRemoteImage(url))
        log.info("Trying to understand art from ${message.contentRaw}")
        var msg = MessageBuilder().append(description).setTTS(true).build()
        channel.sendMessage(msg).queue()
    }

    fun onMessage(guild: Guild, channel: TextChannel, member: Member, message: Message) {
        GlobalScope.launch {
            val guildProperties = guildProperties.getAwait(guild.idLong)
            val prefix = guildProperties.prefix ?: botProps.prefix

            val name: String
            val trigger: String

            //search if message contains any image embeds
            var embeds = message.getEmbeds()
            for(embed in embeds){
                var site = embed.getSiteProvider()
                if(site?.getName() == "Tenor"){
                    var url = embed.getUrl()
                    var text = url?.replace("https://tenor.com/view", "")
                    var msg = MessageBuilder().append("wow is that an animated ${text}").setTTS(true).build()
                    channel.sendMessage(msg).queue()
                }
                if(embed.getType() == EmbedType.IMAGE){
                    respondImage(embed?.getUrl(), channel, message)
                }
            }

            //search if message contains any image files
            var attachments = message.getAttachments()
            for(attachment in attachments){
                //log.info("Attachment: ${attachment.getContentType()}")
                if(attachment.isImage()){
                    respondImage(attachment?.getProxyUrl(), channel, message)
                }
            }

            // match result: a mention of us at the beginning
            val mention = Regex("^(<@!?${guild.getSelfMember().getId()}>\\s*)").find(message.contentRaw)?.value
            if (mention != null) {
                val commandText = message.contentRaw.drop(mention.length)
                if (commandText.isEmpty()) {
                    channel.sendMessage("The prefix here is `${prefix}`, or just mention me followed by a command.").queue()
                    return@launch
                }

                name = commandText.trim().takeWhile { !it.isWhitespace() }
                trigger = mention + name
            } else if (message.contentRaw.startsWith(prefix)) {
                name = message.contentRaw.drop(prefix.length)
                        .takeWhile { !it.isWhitespace() }
                trigger = prefix + name
            } else {
                for(reply in replies.list){
                    if(reply.first.containsMatchIn(message.contentRaw.toLowerCase())){
                        log.info("Replying ${reply.second} to ${message.contentRaw}")
                        var msg = MessageBuilder().append(reply.second).setTTS(true).build()
                        channel.sendMessage(msg).queue()
                    }
                }
                return@launch
            }

            val command = registry[name] ?: return@launch
            val ctx = CommandContext(contextBeans, guildProperties, guild, channel, member, message, command, prefix, trigger)

            log.info("Invocation: ${message.contentRaw}")
            command.invoke0(ctx)
        }
    }

}
