package tk.icudi.durandal.core.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import tk.icudi.durandal.core.logic.player.Player;

public class StartOptions implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String START_OPTIONS_MESSAGE = "START_OPTIONS_MESSAGE";
	
	private List<Player> players = new ArrayList<Player>();

	private boolean releaseCreepAtStart = true;
	
	public void addPlayer(Player player) {
		players.add(player);
	}

	public List<Player> getPlayers() {
		return players;
	}

	public boolean isReleaseCreepAtStart() {
		return releaseCreepAtStart;
	}

	public void setReleaseCreepAtStart(boolean releaseCreepAtStart) {
		this.releaseCreepAtStart = releaseCreepAtStart;
	}

}
