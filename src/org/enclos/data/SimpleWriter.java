package org.enclos.data;

import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.enclos.component.Bridge;
import org.enclos.component.Sheep;
import org.enclos.ui.Board;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SimpleWriter {
	private String jsonFilePath;

	public static void SaveGame(Board board, String fileName) {
		String jsonFilePath = "resources/save/" + fileName + ".json";

		JSONObject jsonObject = new JSONObject();
		JSONArray players = new JSONArray();
		for (Player player : board.getPlayers()) {
			JSONArray sheepsByPLayer = new JSONArray();

			File imgPath = null;
			for (Sheep sheep : player.getSheeps()) {
				imgPath = sheep.getImgPath();
				sheepsByPLayer.add(sheep.getVirtualIndexHexagon().x + "," + sheep.getVirtualIndexHexagon().y);
			}

			JSONArray playerInfos = new JSONArray();
			JSONObject playerRoot = new JSONObject();
			playerInfos.add(playerRoot);

			playerRoot.put("firstname", player.getFirstName());
			playerRoot.put("lastname", player.getLastName());
			playerRoot.put("sheeps", sheepsByPLayer);
			playerRoot.put("imgPath", imgPath.toString());

			players.add(playerInfos);

			JSONArray barriers = new JSONArray();
			for (Bridge currentBridge : board.getBarriers()) {
				JSONArray barrier = new JSONArray();
				for (Point currentPoint : currentBridge.getVirtualIndex()) {

					barrier.add(currentPoint.x + "," + currentPoint.y);
				}
				barriers.add(barrier);

			}
			
			if(board.getDifficulty() != null){
				jsonObject.put("difficulty", board.getDifficulty().toString());
			}

			jsonObject.put("Players", players);
			jsonObject.put("nbSheepPerPlayer", board.getNbSheepPerPlayer());
			jsonObject.put("Boardsize", board.getBoardSize());
			jsonObject.put("Barriers", barriers);
			jsonObject.put("currentPLayerFirstName", ((Human) board.getCurrentPlayer()).getFirstName());
			jsonObject.put("currentPLayerLastName", ((Human) board.getCurrentPlayer()).getLastName());

			try {
				FileWriter jsonFileWriter = new FileWriter(jsonFilePath);
				jsonFileWriter.write(jsonObject.toJSONString());
				jsonFileWriter.flush();
				jsonFileWriter.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void SavePlayer(List<Human> listPlayers, String fileName) {
		String jsonFilePath = "resources/players/" + fileName + ".json";

		JSONObject jsonObject = new JSONObject();

		JSONArray players = new JSONArray();
		for (Human currentPlayer : listPlayers) {
			JSONArray player = new JSONArray();
			player.add(currentPlayer.getLastName() + "," + currentPlayer.getFirstName() + "," + currentPlayer.getAge() + "," + currentPlayer.getNumberOfGamesWon() + "," + currentPlayer.getNumberOfGamesLost() + "," + currentPlayer.getProfilePicturePath());
			players.add(player);
		}
		jsonObject.put("Players", players);

		try {
			FileWriter jsonFileWriter = new FileWriter(jsonFilePath);
			jsonFileWriter.write(jsonObject.toJSONString());
			jsonFileWriter.flush();
			jsonFileWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
