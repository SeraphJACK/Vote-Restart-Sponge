package top.seraphjack.voterestart;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class CommandLoader {
    public static CommandSpec COMMAND_VOTE;
    public static CommandSpec COMMAND_DEVOTE;

    /**
     * Load commands
     */
    CommandLoader() {
        COMMAND_VOTE = CommandSpec.builder()
                .description(Text.of("Vote to restart"))
                .executor((src, args) -> {
                    if (src instanceof Player) {
                        if (VoteRestart.VOTE_MANAGER.vote((Player) src))
                            return CommandResult.success();
                        else
                            throw new CommandException(Text.of("You're already voted."));
                    }
                    return CommandResult.empty();
                })
                .permission("voterestart.vote")
                .build();
        COMMAND_DEVOTE = CommandSpec.builder()
                .description(Text.of("Devote to restart"))
                .executor(((src, args) -> {
                    if (src instanceof Player) {
                        if (VoteRestart.VOTE_MANAGER.deVote((Player) src))
                            return CommandResult.success();
                        else
                            throw new CommandException(Text.of("You cannot devote in case you haven't voted"));
                    }
                    return CommandResult.empty();
                }))
                .permission("voterestart.devote")
                .build();
        Sponge.getCommandManager().register(VoteRestart.INSTANCE, COMMAND_VOTE, "vote");
        Sponge.getCommandManager().register(VoteRestart.INSTANCE, COMMAND_DEVOTE, "devote");
    }
}
