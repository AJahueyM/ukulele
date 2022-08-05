package dev.arbjerg.ukulele.data

import org.springframework.boot.context.properties.ConfigurationProperties

private fun ToRegex(text: String): Regex {
    return Regex(text, RegexOption.IGNORE_CASE)
}

@ConfigurationProperties("replies")
class Replies(
    val list: List<Pair<Regex, String>> = listOf(
        //Users
        Pair(ToRegex("<@168194489343672322>"), "Who dares summon the devil!"),
        Pair(ToRegex("<@308674959557918732>"), "Who dares summon le potato."),
        Pair(ToRegex("<@190624436913831938>"), "Who dares summon the kawaii-lord!"),
        //Roles
        Pair(ToRegex("<@825551873473904650>"), "Wait, that's still a game?"),
        Pair(ToRegex("<@870480609720557598>"), "Of course, how original of you guys"),
        Pair(ToRegex("<@1004028718823510046>"), "I bet I could single-handedly beat all of you at Tetris"),
        Pair(ToRegex("<@820420579172679681>"), "I should be the chef... I've only burnt down about four houses..."),
        //Expressions
        Pair(ToRegex("D:"), "D:"),
        Pair(ToRegex("""\._\."""), "Good ol' Albert."),
        Pair(ToRegex("nya"), "Stop you fucking degenerate"),
        Pair(ToRegex("uwu"), "did someone say uwuᵘʷᵘ oh frick ᵘʷᵘ ᵘʷᵘᵘʷᵘ ᵘʷᵘ ᵘʷᵘ ᵘʷᵘ ᵘʷᵘ ᵘʷᵘ frick sorry guysᵘʷᵘ ᵘʷᵘ ᵘʷᵘ ᵘʷᵘᵘʷᵘ ᵘʷᵘ sorry im dropping ᵘʷᵘ my uwus all over the ᵘʷᵘ place ᵘʷᵘ ᵘʷᵘ ᵘʷᵘ sorry"),
        //Emojis
        Pair(ToRegex(":NiaFlip:"), "Looks like someone's salty over here"),
        Pair(ToRegex(":NiaSmug:"), "#TODO"),
        Pair(ToRegex(":NiaHUH:"), "#TODO"),
        Pair(ToRegex(":NiaBashYouUp:"), "#TODO"),
        Pair(ToRegex(":HoVProcessing:"), "#TODO"),
        //Other
        Pair(ToRegex("coletas?|twintails?"), "#TODO"),
        Pair(ToRegex("peli ?rojas?|red ?head"), "#TODO"),
        Pair(ToRegex("ojos? ?rojos?|red ?eye[ds]?"), "#TODO"),
        Pair(ToRegex("chicas? gato|cat ?girls?"), "#TODO"),
        Pair(ToRegex("chicas? zorro|fox ?girls?|kitsune"), "#TODO"),
        Pair(ToRegex("chicas? tanque|tank ?girls?"), "#TODO"),
        Pair(ToRegex("chicas? avion|plane ?girls?"), "#TODO"),
        Pair(ToRegex("chicas? barco|ship ?girls?"), "#TODO"),
        Pair(ToRegex("yuri|lesbiana?s?"), "#TODO"),
        Pair(ToRegex("madok(a|ita)"), "#TODO"),
        Pair(ToRegex("sad($| +|(de|[ln]))|depress[ia].*"), "#TODO"),
        Pair(ToRegex("il+egal"), "#TODO"),
        Pair(ToRegex("goofy"), "#TODO"),
        Pair(ToRegex("metal ?gear ?solid|mgs"), "#TODO")
    )
)