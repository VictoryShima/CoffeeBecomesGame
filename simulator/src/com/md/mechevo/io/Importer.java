package com.md.mechevo.io;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.md.mechevo.game.*;
import com.md.mechevo.game.action.Action;
import com.md.mechevo.game.action.ActionFactory;
import com.md.mechevo.game.action.IdleAction;
import com.md.mechevo.game.ai.AIAlgorithm;
import com.md.mechevo.game.ai.AIEntry;
import com.md.mechevo.game.condition.Condition;
import com.md.mechevo.game.condition.ConditionFactory;
import com.md.mechevo.game.condition.TrueCondition;
import com.md.mechevo.game.weapon.Weapon;
import com.md.mechevo.game.weapon.WeaponFactory;

/**
 * Imports the data received in JSON and converts to Java Objects
 */
public final class Importer {
	/**
	 * Creates the initial state from the data received in JSON. The data must be in the following
	 * structure: 
	 *   {
	 *     "map" : { "width":double, "height":double },
	 *     "obstacles" : [ { "x":double, "y":double, "radius":double }, ... ],
	 *     "players" : [ {
	 *       "teamId":int, 
	 *       "weapons":[string,string,string],
	 *       "color":string,
	 *       "x":double,
	 *       "y":double,
	 *       "angle":double,
	 *       "algorithm": [ { 
	 *         "conditions": [ { "name":string, "param":[string] }, ... ],
	 *         "actions": [ { "name":string, "param":[string] }, ... ]
	 *       }]
	 *     }]
	 *   }
	 * 
	 * @param json the data in JSON
	 * @return the initial state
	 */
	public static State createInitialState(String json) {
		// Generate JSON tree
		JsonParser parser = new JsonParser();
		JsonObject tree = parser.parse(json).getAsJsonObject();

		// Deal with map
		JsonObject mapJson = tree.get("map").getAsJsonObject();
		double mapWidth = mapJson.get("width").getAsDouble();
		double mapHeight = mapJson.get("height").getAsDouble();

		EventObserver report = new EventObserver();
		State state = new State(new Map(mapWidth, mapHeight));
		state.registerEventObserver(report);

		List<Obstacle> obstacles =
				Importer.createObstacles(tree.get("obstacles").getAsJsonArray(), state, report);
		for (Obstacle o : obstacles) {
			state.addObstacle(o);
		}

		List<Player> players = createPlayers(tree.get("players").getAsJsonArray(), state, report);
		for (Player p : players) {
			state.addPlayer(p);
		}

		return state;
	}

	/**
	 * Parses and creates all the Players.
	 *
	 * @param playersJson the array with all the players
	 * @param state the initial state
	 * @param report the report where all events will be sent
	 */
	private static List<Player> createPlayers(JsonArray playersJson, State state,
			EventObserver report) {
		LinkedList<Player> listPlayers = new LinkedList<>();

		for (int playerId = 0; playerId < playersJson.size(); playerId++) {
			// Enough arguments to create a Player object
			JsonObject playerJson = playersJson.get(playerId).getAsJsonObject();
			int teamId = playerJson.get("teamId").getAsInt();
			double playerX = playerJson.get("x").getAsDouble();
			double playerY = playerJson.get("y").getAsDouble();
			double playerAngle = playerJson.get("angle").getAsDouble();

			Player player =
					new Player(state.getNextId(), teamId, new Position(playerX, playerY),
							playerAngle);
			player.registerEventObserver(report);
			createWeapons(playerJson.get("weapons").getAsJsonArray(), player, report);
			createAIAlgorithm(playerJson.get("algorithm").getAsJsonArray(), player, report);

			player.begin(state);
			listPlayers.add(player);
		}
		return listPlayers;
	}

	/**
	 * Parses and creates the algorithm.
	 *
	 * @param algorithmJson the array with all the AIEntries
	 * @param player the owner
	 */
	private static void createAIAlgorithm(JsonArray algorithmJson, Player player,
			EventObserver report) {
		AIAlgorithm algorithm = new AIAlgorithm(player);
		player.setAlgorithm(algorithm);

		for (int entryId = 0; entryId < algorithmJson.size(); entryId++) {
			AIEntry entry = new AIEntry(algorithm);
			algorithm.addEntry(entry);

			createConditions(algorithmJson.get(entryId).getAsJsonObject().get("conditions")
					.getAsJsonArray(), entry, player);

			createActions(algorithmJson.get(entryId).getAsJsonObject().get("actions")
					.getAsJsonArray(), entry, player, report);
		}

		// Add TrueCondition and IdleAction
		AIEntry entry = new AIEntry(algorithm);
		algorithm.addEntry(entry);
		entry.addCondition(new TrueCondition(player));
		entry.addAction(new IdleAction(player));
	}

	/**
	 * Parses and creates all the AIConditions for this AIEntry.
	 *
	 * @param conditionsJson the array with all the AIConditions.
	 * @param entry the entry these conditions belong to.
	 * @param player the owner.
	 */
	private static void createConditions(JsonArray conditionsJson, AIEntry entry, Player player) {
		for (int conditionId = 0; conditionId < conditionsJson.size(); conditionId++) {
			JsonObject conditionJson = conditionsJson.get(conditionId).getAsJsonObject();

			JsonArray paramsJson = conditionJson.get("param").getAsJsonArray();
			ArrayList<String> params = new ArrayList<>();
			for (int i = 0; i < paramsJson.size(); i++) {
				params.add(paramsJson.get(i).getAsString());
			}

			Condition condition =
					ConditionFactory.createCondition(conditionJson.get("name").getAsString(),
							player, params);
			entry.addCondition(condition);
		}
	}

	/**
	 * Parses and creates all the AIActions for this AIEntry.
	 *
	 * @param actionsJson the array with all the AIActions.
	 * @param entry the entry these conditions belong to.
	 * @param player the owner.
	 */
	private static void createActions(JsonArray actionsJson, AIEntry entry, Player player,
			EventObserver report) {
		for (int actionId = 0; actionId < actionsJson.size(); actionId++) {
			JsonObject actionJson = actionsJson.get(actionId).getAsJsonObject();

			JsonArray paramsJson = actionJson.get("param").getAsJsonArray();
			ArrayList<String> params = new ArrayList<>();
			for (int i = 0; i < paramsJson.size(); i++) {
				params.add(paramsJson.get(i).getAsString());
			}

			Action action =
					ActionFactory
							.createAction(actionJson.get("name").getAsString(), player, params);
			action.registerEventObserver(report);
			entry.addAction(action);
		}
	}

	/**
	 * Parses and creates all weapons from the json.
	 * The order which the weapons are assigned is [LEFT, RIGHT, CENTER].
	 *
	 * @param weaponJson the array of weapons for this player
	 * @param player the owner of these weapons
	 */
	private static void createWeapons(JsonArray weaponJson, Player player, EventObserver report) {
		for (int weaponId = 0; weaponId < weaponJson.size(); weaponId++) {
			Weapon weapon =
					WeaponFactory.createWeapon(weaponJson.get(weaponId).getAsString(), player);
			weapon.registerEventObserver(report);
			player.equipWeapon(weapon, Weapon.WeaponSlot.values()[weaponId]);
		}
	}

	/**
	 * Parses and creates all obstacles from the json.
	 * Creates the event 'begin'.
	 *
	 * @param obstaclesJson the array of obstacles in JSON
	 * @param state the initial state
	 * @param report the report where all events will be sent
	 * @return
	 */
	private static List<Obstacle> createObstacles(JsonArray obstaclesJson, State state,
			EventObserver report) {
		LinkedList<Obstacle> listObstacles = new LinkedList<>();
		for (int obstacleId = 0; obstacleId < obstaclesJson.size(); obstacleId++) {
			JsonObject obstacleJson = obstaclesJson.get(obstacleId).getAsJsonObject();
			double obstacleX = obstacleJson.get("x").getAsDouble();
			double obstacleY = obstacleJson.get("y").getAsDouble();
			double obstacleRadius = obstacleJson.get("radius").getAsDouble();

			Obstacle obstacle =
					new Obstacle(state.getNextId(), new Position(obstacleX, obstacleY),
							obstacleRadius);
			obstacle.registerEventObserver(report);
			obstacle.begin(state);

			listObstacles.add(obstacle);
		}
		return listObstacles;
	}
}
