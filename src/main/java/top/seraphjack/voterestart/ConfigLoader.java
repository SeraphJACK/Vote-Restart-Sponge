package top.seraphjack.voterestart;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;

public class ConfigLoader {
    public static float votePercentage;

    public ConfigLoader() throws IOException {
        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setPath(VoteRestart.INSTANCE.cfgPath).build();
        ConfigurationNode root = loader.load();
        votePercentage = root.getNode("vote", "votePercentage").getFloat(-1.0F);
        if (votePercentage == -1.0F) {
            root.getNode("vote","votePercentage").setValue(0.5F);
            votePercentage = 0.5F;
        }
        loader.save(root);
    }
}
