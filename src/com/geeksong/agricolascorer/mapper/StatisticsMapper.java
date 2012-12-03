package com.geeksong.agricolascorer.mapper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.geeksong.agricolascorer.model.StatisticFilter;

public class StatisticsMapper {
	private Database db;
	
    public StatisticsMapper() {
    	this.db = Database.getInstance();
    }
    
    public void getStatisticsForFilter(StatisticFilter filter) {
    	SQLiteDatabase sqlDb = db.getReadableDatabase();
    	
    	String query = String.format("SELECT game.%s, score.%s, player.%s, playerCount.playerCount " +
    			"FROM %s as game " +
    			"JOIN %s as score on score.%s=game.%s " +
    			"JOIN %s as player on score.%s=player.%s " +
    			"JOIN (SELECT game.%s as gameId, COUNT(%s) as playerCount " +
    			"	FROM %s as game " +
    			"	JOIN %s as score on game.%s=score.%s " +
    			"	GROUP BY score.%s " +
    			"	ORDER BY playerCount) as playerCount on playerCount.%s=game.%s ",
    			Database.KEY_DATE, Database.KEY_FINALSCORE, Database.KEY_NAME, 
    			Database.TABLE_GAMES,
    			Database.TABLE_SCORES, Database.KEY_GAMEID, Database.KEY_ID,
    			Database.TABLE_RECENTPLAYERS, Database.KEY_PLAYERID, Database.KEY_ID,
    			Database.KEY_ID, Database.KEY_PLAYERID,
    			Database.TABLE_GAMES,
    			Database.TABLE_SCORES, Database.KEY_ID, Database.KEY_GAMEID,
    			Database.KEY_GAMEID,
    			Database.KEY_GAMEID, Database.KEY_ID);
    	
    	boolean hasWhere = false;
    	if(filter.hasPlayerCount()) {
    		query += String.format("WHERE playerCount=%d ", filter.getPlayerCount());
    		hasWhere = true;
    	}
    	
    	if(filter.hasSelectedPlayers()) {
    		if(!hasWhere)
    			query += "WHERE ";
			else
				query += "AND ";
    			
    		query += String.format("player.%s in (", Database.KEY_NAME);
    		String prefix = "";
    		for(String playerName : filter.getSelectedPlayers()) {
    			query += (prefix + "'" + playerName + "'");
    			prefix = ", ";    					
    		}
    		query += ")";
    	}
    	
    	Cursor scoreCursor = sqlDb.rawQuery(query, null);

    	// todo...return something!
    }
}
