package top.seraphjack.voterestart;

import com.google.inject.Inject;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Path;

@Plugin(id = VoteRestart.ID, name = "Vote Restart", version = "0.0.1-SNAPSHOT", description = "Vote-Restart plugin version.")
public class VoteRestart {
    public static final String ID = "voterestart";
    public static RestartManager VOTE_MANAGER;
    public static VoteRestart INSTANCE;

    @Inject
    @DefaultConfig(sharedRoot = true)
    Path cfgPath;

    @Listener
    public void onServerStart(GameStartedServerEvent e) throws IOException {
        INSTANCE = this;
        VOTE_MANAGER = new RestartManager();
        new CommandLoader();
        new ConfigLoader();
    }
}
