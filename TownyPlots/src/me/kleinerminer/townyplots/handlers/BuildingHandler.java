package me.kleinerminer.townyplots.handlers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import com.palmergames.bukkit.towny.object.Town;

import me.kleinerminer.townyplots.TownyPlots;
import me.kleinerminer.townyplots.building.Building;

public class BuildingHandler {
	private TownyPlots plugin;
	public BuildingHandler(TownyPlots townyplots) {
		this.plugin = townyplots;
	}
	
	
	
	public int countBlocks(Location l1, Location l2, Material material) {
	    World w = l1.getWorld();
	    int totalBlocks = 0;
	    for(int x=Math.min(l1.getBlockX(), l2.getBlockX()) ; x<=l2.getBlockX(); x++){
	        for(int y=Math.min(l1.getBlockY(), l2.getBlockY());y<=l2.getBlockY(); y++){
	            for(int z=Math.min(l1.getBlockZ(), l2.getBlockZ());z<=l2.getBlockZ(); z++){
	                if(w.getBlockAt(x,y,z).getType().equals(material)) 
	                        totalBlocks++;
	            }
	        }
	    }
	 
	    return totalBlocks;
	}
	public Building getBuilding(Location loc){
		int size = plugin.plotSize;
		int x = (int)(loc.getX() - (loc.getX() % size));
		int z = (int)(loc.getZ() - (loc.getZ() % size));
		if(loc.getX() < 0) {
			x = ((int)(loc.getX() - (loc.getX() % size)) - size);
		}
		if(loc.getZ() < 0) {
			z = ((int)(loc.getZ() - (loc.getZ() % size)) - size);
		}
		for(Building b : plugin.buildings) {
			if(b == null) return null;
			if(b.getX() == x && b.getZ() == z) {
				return b;
			}
		}
		return null;
	}
	
	public Building[] getTownBuildings(Town town) {
		Building[] townBuildings = new Building[50];
		int i = 0;
		for(Building b : plugin.buildings) {
			if(b == null) return null;
			if(b.getTown().equals(town)) {
				townBuildings[i] = b;
			}
			i++;
		}
		return townBuildings;
	}
	//Returns the level of the road connecting two buildings. 0 = No road, 1 = Gravel road, 2 = Cobblestone road
	public int roadConnectionLevel(Building b1, Building b2) {
		//TODO: Add road system 
		return 0;
	}
}