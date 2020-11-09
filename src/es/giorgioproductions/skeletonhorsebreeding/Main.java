package es.giorgioproductions.skeletonhorsebreeding;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§fSkeletonHorseBreeding v"+getDescription().getVersion()+" §8was loaded successfully");
        getServer().getPluginManager().registerEvents(new SkeletonHorseBreedListener(this), this);
    }
}
