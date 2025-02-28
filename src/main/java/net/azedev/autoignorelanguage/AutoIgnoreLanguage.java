package net.azedev.autoignorelanguage;

import net.azedev.autoignorelanguage.modules.ModuleAutoIgnoreLanguage;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

import java.io.IOException;

public class AutoIgnoreLanguage extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Auto Ignore");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Auto Ignore Language");

        // Modules
        try {
            Modules.get().add(new ModuleAutoIgnoreLanguage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "net.azedev.autoignorelanguage";
    }
}
