package me.kleinerminer.townyplots;

import java.io.File;
import java.io.IOException;

import me.kleinerminer.townyplots.listeners.BlockBreakListener;
import me.kleinerminer.townyplots.listeners.BlockPlaceListener;
import me.kleinerminer.townyplots.listeners.ClickListener;
import me.kleinerminer.townyplots.listeners.SignChangeListener;
import me.kleinerminer.townyplots.building.Building;
import me.kleinerminer.townyplots.handlers.BuildingHandler;
import me.kleinerminer.townyplots.handlers.ConfigHandler;
import me.kleinerminer.townyplots.handlers.FlatfileHandler;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.db.TownyDataSource;
import com.palmergames.bukkit.towny.object.TownyUniverse;



public class TownyPlots extends JavaPlugin {
	File configuration = new File("plugins/TownyPlots/", "config.yml");
	public File flatfile = new File("plugins/TownyPlots/", "flatfile.yml");
	public FileConfiguration config = YamlConfiguration.loadConfiguration(configuration);
	public FileConfiguration plotdata = YamlConfiguration.loadConfiguration(flatfile);
	public Economy economy;
	
	public ConfigHandler configHandler = null;
	public FlatfileHandler flatfilehandler = null;
	public BuildingHandler buildinghandler = null;
	public Building buildings[] = new Building[1500];
	public TownyDataSource townydatasource;
	public Player[] playersRegisteringChests = new Player[100];
	public int plotSize;
	
	
	@Override
	public void onEnable() {
		configHandler = new ConfigHandler(this);
		buildinghandler = new BuildingHandler(this);
		townydatasource = TownyUniverse.getDataSource();
		loadConfig();
		registerListeners();
		registerCommands();
		setupEconomy();
		flatfilehandler = new FlatfileHandler(this);
		plotSize = config.getInt("plotSize");
		if(config.getBoolean("useTownyPlotSize")) {
			plotSize = TownySettings.getTownBlockSize();
		}
		flatfilehandler.loadBuildings();
		this.getLogger().info("TownyPlots Enabled!"); 
	}
	
	
	@Override
	public void onDisable() {
		try {
			flatfilehandler.saveBuildings();
			plotdata.save(flatfile);
		} catch (IOException e) {
			getLogger().severe("Could not save Data!");
			e.printStackTrace();
		}
		this.getLogger().info("TownyPlots Disabled!");
	}

	private void loadConfig(){
		configHandler.refreshConfig();
		try {
			config.save(configuration);
		} catch (IOException e) {
			getLogger().severe("The config could not be loaded!");
			e.printStackTrace();
			getLogger().severe("Disabling TownyPlots");
			this.getPluginLoader().disablePlugin(this);
		}
	}
	
	private void registerCommands() {
		this.getCommand("plottype").setExecutor(new CE_plottype(this));
		this.getCommand("building").setExecutor(new CE_building(this));
	}
	
	private void registerListeners() {
		 getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
		 getServer().getPluginManager().registerEvents(new ClickListener(this), this);
		 getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
		 getServer().getPluginManager().registerEvents(new SignChangeListener(this), this);
	}
	
	
	public Plugin getPlugin() {
		return this;
	}
	public String lang(String entry) {
		//Method for formatting messages
		return (ChatColor.AQUA + "[" + ChatColor.WHITE+"Town Plots" + ChatColor.AQUA + "] "+ ChatColor.WHITE + config.getString("lang." + entry));
	}
    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
 
        return (economy != null);
    }
    public Building getBuilding(int x, int z) throws NullPointerException {
    	for(Building b : buildings) {
    		if(b.getX() == x && b.getZ() == z) return b;
    	}
    	return null;
    }
}
