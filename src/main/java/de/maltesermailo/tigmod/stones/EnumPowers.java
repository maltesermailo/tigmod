package de.maltesermailo.tigmod.stones;

public class EnumPowers {
	
	public enum PowerStone {
		ELECTROCUTE(false, "Electrocute (Blast)"),
		SHIELD(true, "Shield");
		
		private boolean gauntledNeeded;
		private String caption;
		
		PowerStone(boolean gauntletNeeded, String caption) {
			this.gauntledNeeded = gauntletNeeded;
			this.caption = caption;
		}
		
		public boolean isGauntledNeeded() {
			return gauntledNeeded;
		}
		
		public String getCaption() {
			return caption;
		}
	}
	
	public enum SpaceStone {
		//PORTAL(false, true, "Teleport"),
		TELEPORT(false, false, "Teleport (Enderlike)"),
		JUMP_BOOST(true, true, "Jump Boost (e/d)"),
		SPEED_BOST(true, true, "Speed Boost (e/d)");
		
		private boolean gauntledNeeded;
		private boolean powerStoneNeeded;
		private String caption;
		
		SpaceStone(boolean gauntletNeeded, boolean powerStoneNeeded, String caption) {
			this.gauntledNeeded = gauntletNeeded;
			this.powerStoneNeeded = powerStoneNeeded;
			this.caption = caption;
		}
		
		public boolean isGauntledNeeded() {
			return gauntledNeeded;
		}
		
		public boolean isPowerStoneNeeded() {
			return powerStoneNeeded;
		}
		
		public String getCaption() {
			return caption;
		}
	}
	
	public enum TimeStone {
		TRAP(false, false, "Trap (e/d)");
		
		private boolean gauntledNeeded;
		private boolean powerStoneNeeded;
		private String caption;
		
		TimeStone(boolean gauntletNeeded, boolean powerStoneNeeded, String caption) {
			this.gauntledNeeded = gauntletNeeded;
			this.powerStoneNeeded = powerStoneNeeded;
			this.caption = caption;
		}
		
		public boolean isGauntledNeeded() {
			return gauntledNeeded;
		}
		
		public boolean isPowerStoneNeeded() {
			return powerStoneNeeded;
		}
		
		public String getCaption() {
			return caption;
		}
	}
	
	public enum RealityStone {
		INVISIBILITY(false, false, "Invisibility (e/d)"),
		SET_BLOCK(false, true, "Change Material"),
		CREATE_ITEM(false, true, "Create Item");
		
		private boolean gauntledNeeded;
		private boolean powerStoneNeeded;
		private String caption;
		
		RealityStone(boolean gauntletNeeded, boolean powerStoneNeeded, String caption) {
			this.gauntledNeeded = gauntletNeeded;
			this.powerStoneNeeded = powerStoneNeeded;
			this.caption = caption;
		}
		
		public boolean isGauntledNeeded() {
			return gauntledNeeded;
		}
		
		public boolean isPowerStoneNeeded() {
			return powerStoneNeeded;
		}
		
		public String getCaption() {
			return caption;
		}
	}
	
	public enum SoulStone {
		KILL(false, false, "Kill Player"),
		KILL_ALL(false, true, "Kill All (10 block radius)"),
		HEAL(true, true, "Heal Player");
		
		private boolean gauntledNeeded;
		private boolean powerStoneNeeded;
		private String caption;
		
		SoulStone(boolean gauntletNeeded, boolean powerStoneNeeded, String caption) {
			this.gauntledNeeded = gauntletNeeded;
			this.powerStoneNeeded = powerStoneNeeded;
			this.caption = caption;
		}
		
		public boolean isGauntledNeeded() {
			return gauntledNeeded;
		}
		
		public boolean isPowerStoneNeeded() {
			return powerStoneNeeded;
		}
		
		public String getCaption() {
			return caption;
		}
	}
	
	public enum MindStone {
		FLY(false, false, "Fly (e/d)");
		
		private boolean gauntledNeeded;
		private boolean powerStoneNeeded;
		private String caption;
		
		MindStone(boolean gauntletNeeded, boolean powerStoneNeeded, String caption) {
			this.gauntledNeeded = gauntletNeeded;
			this.powerStoneNeeded = powerStoneNeeded;
			this.caption = caption;
		}
		
		public boolean isGauntledNeeded() {
			return gauntledNeeded;
		}
		
		public boolean isPowerStoneNeeded() {
			return powerStoneNeeded;
		}
		
		public String getCaption() {
			return caption;
		}
	}

}
