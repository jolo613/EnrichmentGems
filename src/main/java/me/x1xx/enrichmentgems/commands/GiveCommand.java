package me.x1xx.enrichmentgems.commands;

import com.azortis.azortislib.command.Command;
import com.azortis.azortislib.command.CommandInjector;
import com.azortis.azortislib.command.builders.CommandBuilder;
import com.azortis.azortislib.command.executors.ICommandExecutor;
import com.azortis.azortislib.command.executors.ITabCompleter;
import com.azortis.azortislib.utils.FormatUtil;
import me.x1xx.enrichmentgems.EnrichmentGems;
import me.x1xx.enrichmentgems.gems.Gem;
import me.x1xx.enrichmentgems.gems.GemType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GiveCommand implements ICommandExecutor, ITabCompleter {

    public GiveCommand(EnrichmentGems gems) {
        Command command = new CommandBuilder()
                .setName("egem")
                .setDescription("The main command for egems")
                .setUsage("/egem")
                .setExecutor(this)
                .setTabCompleter(this)
                .setPlugin(gems)
                .build();
        CommandInjector.injectCommand("egems", command, true);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("egems.give") || !commandSender.isOp()) {
            commandSender.sendMessage(FormatUtil.color("&dEGems> &cYou do not have permission to use this command!"));
            return true;
        }
        if (strings.length <= 4) {
            commandSender.sendMessage(FormatUtil.color("&dEGems> &b/egem give (player) (type) (tier) [amount]"));
            return true;
        } else if (strings.length == 5) {
            Player p = Bukkit.getPlayer(strings[1]);
            if (p == null || !p.isOnline()) {
                commandSender.sendMessage(FormatUtil.color("&dEGems> &bThe player specified is offline!"));
                return true;
            }
            GemType type = GemType.fromString(strings[2]);
            if (type == null) {
                commandSender.sendMessage(FormatUtil.color("&dEGems> &bThe gem type specified is not known!"));
                return true;
            }
            Gem gem = type.getGem();
            int tier;
            try {
                tier = Integer.parseInt(strings[3]);
            } catch (NumberFormatException e) {
                commandSender.sendMessage(FormatUtil.color("&dEGems> &bThe tier specified is not a number!"));
                return true;
            }
            if (tier > gem.getMaxTier()) {
                tier = gem.getMaxTier();
            }
            if (tier < 1) tier = 1;
            int amount;
            try {
                amount = Integer.parseInt(strings[4]);
            } catch (NumberFormatException e) {
                commandSender.sendMessage(FormatUtil.color("&dEGems> &bThe amount specified is not a number!"));
                return true;
            }
            gem.addGems(amount, tier, p);
            commandSender.sendMessage(FormatUtil.color("&dEGems> &bAdded &ex" + amount + "&b of tier &e" + tier
                    + " " + type + " &bto player&e " + p.getName()));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings, Location location) {
        if (commandSender.hasPermission("egems.give") || commandSender.isOp()) {
            if (strings.length <= 1) return Collections.singletonList("give");
            if (strings.length == 2) {
                List<String> players = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
                return StringUtil.copyPartialMatches(strings[1], players, new ArrayList<>());
            }
            if (strings.length == 3) {
                List<String> gems = Arrays.stream(GemType.values()).map(GemType::toString).collect(Collectors.toList());
                return StringUtil.copyPartialMatches(strings[2], gems, new ArrayList<>());
            }
            if (strings.length == 4) {
                GemType type = GemType.fromString(strings[2]);
                if (type == null) return null;
                int maxTier = type.getGem().getMaxTier();
                List<String> values = new ArrayList<>();
                for (int i = 1; i <= maxTier; i++) {
                    values.add(i + "");
                }
                return values;
            }
        }
        return null;
    }
}
