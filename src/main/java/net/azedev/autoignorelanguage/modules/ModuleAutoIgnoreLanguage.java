package net.azedev.autoignorelanguage.modules;

import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.azedev.autoignorelanguage.AutoIgnoreLanguage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ModuleAutoIgnoreLanguage extends Module {
    private final LanguageDetector detector;
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
    private final SettingGroup sgLang = this.settings.createGroup("Languages");

    private final Setting<Boolean> ignoreSpanish = sgLang.add(new BoolSetting.Builder()
        .name("ignore-spanish")
        .description("Ignore messages in Spanish.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> ignoreTurkish = sgLang.add(new BoolSetting.Builder()
        .name("ignore-turkish")
        .description("Ignore messages in Turkish.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> ignorePolish = sgLang.add(new BoolSetting.Builder()
        .name("ignore-polish")
        .description("Ignore messages in Polish.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> ignoreSwedish = sgLang.add(new BoolSetting.Builder()
        .name("ignore-swedish")
        .description("Ignore messages in Swedish.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> ignoreFrench = sgLang.add(new BoolSetting.Builder()
        .name("ignore-french")
        .description("Ignore messages in French.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> ignoreRussian = sgLang.add(new BoolSetting.Builder()
        .name("ignore-russian")
        .description("Ignore messages in Russian.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> ignoreArabic = sgLang.add(new BoolSetting.Builder()
        .name("ignore-arabic")
        .description("Ignore messages in Arabic.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> ignoreHindi = sgLang.add(new BoolSetting.Builder()
        .name("ignore-hindi")
        .description("Ignore messages in Hindi.")
        .defaultValue(false)
        .build()
    );

    public ModuleAutoIgnoreLanguage() throws IOException {
        super(AutoIgnoreLanguage.CATEGORY, "auto-ignore-language", "Automatically ignores selected languages in chat");

        List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();
        detector = LanguageDetectorBuilder.create(NgramExtractors.standard())
            .withProfiles(languageProfiles)
            .build();
    }

    @EventHandler
    private void onMessageReceive(ReceiveMessageEvent event) {
        Text message = event.getMessage();
        String messageText = message.getString().trim();

        System.out.println("Received: " + messageText);

        int separatorIndex = messageText.indexOf("»");
        if (separatorIndex == -1) return;

        String username = messageText.substring(0, separatorIndex).trim();
        String chatMessage = messageText.substring(separatorIndex + 2).trim();

        username = username.replaceAll("\\[.*?\\]", "").trim();

        if (chatMessage.length() < 5) return;
        Optional<LdLocale> detectedLanguage = detector.detect(chatMessage.toLowerCase()).toJavaUtil();

        if (detectedLanguage.isPresent()) {
            String detectedLangCode = detectedLanguage.get().getLanguage();
            System.out.println("Detected Language: " + detectedLangCode);

            if ((ignoreSpanish.get() && detectedLangCode.equals("es")) ||
                (ignoreTurkish.get() && detectedLangCode.equals("tr")) ||
                (ignorePolish.get() && detectedLangCode.equals("pl")) ||
                (ignoreFrench.get() && detectedLangCode.equals("fr")) ||
                (ignoreRussian.get() && detectedLangCode.equals("ru")) ||
                (ignoreArabic.get() && detectedLangCode.equals("ar")) ||
                (ignoreHindi.get() && detectedLangCode.equals("hi")) ||
                (ignoreSwedish.get() && detectedLangCode.equals("sv"))) {

                System.out.println("Chat message is in an ignored language!");

                if (!username.isEmpty()) {
                    MinecraftClient.getInstance().player.networkHandler.sendChatCommand("ignore " + username);
                    System.out.println("Ignored user: " + username);
                }

                event.cancel();
            }
        }
    }
}
