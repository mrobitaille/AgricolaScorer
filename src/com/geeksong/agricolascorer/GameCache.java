package com.geeksong.agricolascorer;

import java.util.ArrayList;

import com.geeksong.agricolascorer.model.Game;
import com.geeksong.agricolascorer.model.Player;
import com.geeksong.agricolascorer.model.Score;

public class GameCache {
	private ArrayList<Score> scores = new ArrayList<Score>();
	private ArrayList<Player> players = new ArrayList<Player>();
	
	private boolean isFromDatabase = false;
	private Game savedGame;
	private boolean farmersOfTheMoor = false;
	
	private static GameCache instance;
	public static GameCache getInstance() {
		if(instance == null)
			instance = new GameCache();
		
		return instance;
	}
	
	private GameCache() {
	}
	
	public void clearGame() {
		scores.clear();
		players.clear();
		isFromDatabase = false;
		savedGame = null;
	}
	
	public void setGame(Game game) {
		clearGame();
		
		this.savedGame = game;
		this.isFromDatabase = true;
		
		for(int i = 0; i < game.getScoreCount(); i++) {
			Score score = game.getScore(i);
			
			players.add(score.getPlayer());
			scores.add(score);
		}
	}
	
	// This only returns a proper Game object when the game has been loaded from the database
	public Game getGame() {
		return savedGame;
	}
	
	public boolean isFromDatabase() { return this.isFromDatabase; }
	
	public void addPlayer(Player player) {
		players.add(player);
		scores.add(new Score(player));
	}
	
	public void removePlayer(int index) {
		Player playerToRemove = players.get(index);
		
		Score scoreToRemove = null;
		for(Score score : scores) {
			if(score.getPlayer().getId() == playerToRemove.getId()) {
				scoreToRemove = score;
				break;
			}
		}
		if(scoreToRemove != null)
			scores.remove(scoreToRemove);
		
		players.remove(index);
	}
	
	public boolean hasPlayers() {
		return !players.isEmpty();
	}
	
	public boolean isPlayerInGame(String playerName) {
		return getPlayerByName(playerName) != null;
	}
	
	public Player getPlayer(int position) {
		return players.get(position);
	}
	
	public Player getPlayerByName(String playerName) {
		for(Player player : players) {
			if(player.getName().equals(playerName))
				return player;
		}
		return null;
	}
	
	public Score getScoreByPlayerName(String playerName) {
		for(Score score : scores) {
			if(score.getPlayer().getName().equals(playerName))
				return score;
		}
		return null;
	}
	
	public ArrayList<Player> getPlayerList() { return players; }
	public ArrayList<Score> getScoreList() { return scores; }
	
	public void setFarmersOfTheMoor(boolean farmersOfTheMoor) { this.farmersOfTheMoor = farmersOfTheMoor; }
	public boolean isFarmersOfTheMoor() { 
		if(this.isFromDatabase)
			return savedGame.isFarmersOfTheMoor();
		
		return this.farmersOfTheMoor; 
	}
}
