package de.maltesermailo.tigmod.gauntlet;

public enum EnumStones {
	NONE(""), SPACE_STONE("Space Stone"), POWER_STONE("Power Stone"), REALITY_STONE("Reality Stone"), TIME_STONE("Time Stone"), MIND_STONE("Mind Stone"), SOUL_STONE("Soul Stone"), SNAP("Snap");
	
	private String caption;
	
	EnumStones(String caption) {
		this.caption = caption;
	}
	
	public String getCaption() {
		return caption;
	}
}
