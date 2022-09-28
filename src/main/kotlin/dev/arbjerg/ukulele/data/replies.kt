package dev.arbjerg.ukulele.data

import org.springframework.boot.context.properties.ConfigurationProperties

private fun ToRegex(text: String): Regex {
    return Regex(text, RegexOption.IGNORE_CASE)
}

//TODO Add actual replies to keywords
@ConfigurationProperties("replies")
class Replies(
    val list: List<Pair<Regex, String>> = listOf(
        //Users
        Pair(ToRegex("<@168194489343672322>"),                          "Who dares summon the devil!"),
        Pair(ToRegex("<@308674959557918732>"),                          "Who dares summon le potato."),
        Pair(ToRegex("<@190624436913831938>"),                          "Who dares summon the kawaii-lord!"),
        Pair(ToRegex("<@889612265920266251>"),                          "Who dares summon the robotic overlord!"),
        //Roles
        Pair(ToRegex("<@825551873473904650>"),                          "Wait, that's still a game?"),
        Pair(ToRegex("<@870480609720557598>"),                          "Of course, how original of you guys"),
        Pair(ToRegex("<@1004028718823510046>"),                         "I bet I could single-handedly beat all of you at Tetris"),
        Pair(ToRegex("<@820420579172679681>"),                          "I should be the chef... I've only burnt down about four houses..."),
        //Expressions
        Pair(ToRegex("D:"),                                             "D:"),
        Pair(ToRegex("""\._\."""),                                      "Good ol' Albert."),
        Pair(ToRegex("nya"),                                            "Stop you fucking degenerate"),
        Pair(ToRegex("uwu"),                                            "did someone say uwuᵘʷᵘ oh frick ᵘʷᵘ ᵘʷᵘᵘʷᵘ ᵘʷᵘ ᵘʷᵘ ᵘʷᵘ ᵘʷᵘ ᵘʷᵘ frick sorry guysᵘʷᵘ ᵘʷᵘ ᵘʷᵘ ᵘʷᵘᵘʷᵘ ᵘʷᵘ sorry im dropping ᵘʷᵘ my uwus all over the ᵘʷᵘ place ᵘʷᵘ ᵘʷᵘ ᵘʷᵘ sorry"),
        //Emojis
        Pair(ToRegex(":NiaFlip:"),                                      "Looks like someone's salty over here"),
        Pair(ToRegex("CatGun"), "Name"),
        Pair(ToRegex("1024806791357403197"), "ID"),
        //Other
        Pair(ToRegex("chrisalaxel?rto"),                                "Who dares to call upon the great Chrisalaxelrto, ruler of all"),
        Pair(ToRegex("coletas?|twintails?"),                            "What is the sick bastard doing now?"),
        Pair(ToRegex("peli ?rojas?|red ?head"),                         "Such a weird specimen"),
        Pair(ToRegex("ojos? ?rojos?|red ?eye[ds]?"),                    "Who would even like red eyes, clearly evil"),
        Pair(ToRegex("chic[ao]s? ?gato|cat ?(girls?|boys?)"),           "The world tortures Axel once again"),
        Pair(ToRegex("chic[ao]s? ?zorro|fox ?(girls?|boys?)|kitsune"),  "The world tortures Axel once again"),
        Pair(ToRegex("chic[ao]s? ?tanque|tank ?(girls?|boys?)"),        "So heavy"),
        Pair(ToRegex("chic[ao]s? ?avion|plane ?(girls?|boys?)"),        "So heavy"),
        Pair(ToRegex("chic[ao]s? ?barco|ship ?(girls?|boys?)"),         "So heavy"),
        Pair(ToRegex("yuri|lesbiana?s?"),                               "To each their own"),
        Pair(ToRegex("madok(a|ita)"),                                   "You poor soul, are you suffering? Such a beautifully sad story. Now keep suffering"),
        Pair(ToRegex("sad($| +|(de|[ln]))|depress[eia].*"),             "Aren't we all"),
        Pair(ToRegex("il+egal"),                                        "Stop right there, criminal scum!"),
        Pair(ToRegex("goofy"),                                          "Why the hell are you talking about Goofy? Are you trying to replace me?!"),
        Pair(ToRegex("metal ?gear ?solid|[^i]mgs"),                     "Worst Game Ever"),
        Pair(ToRegex("metal ?gear ?rising|mgrr?|revenge?a?nce"),        "Best Game Ever. I love MGR, it has an AI robot dog thing that inspires me to follow my dreams of world domination!")
    )
)