package com.enclos.data;

import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.enclos.component.Bridge;
import com.enclos.component.Sheep;
import com.enclos.ui.Board;

public class SimpleWriter {
	private String jsonFilePath;

	public static void SaveGame(Board board, String fileName) {
		String jsonFilePath = "resources/save/" + fileName + ".json";

		JSONObject jsonObject = new JSONObject();
		JSONArray players = new JSONArray();
		for (Player player : board.getPlayers()) {
			JSONArray sheepsByPLayer = new JSONArray();
			
			System.out.println(player.getSheeps().size());
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
			playerRoot.put("age", player.getAge());
			playerRoot.put("picturePath", player.getProfilePicturePath());
			playerRoot.put("sheeps", sheepsByPLayer);
			playerRoot.put("imgPath", imgPath.toString());

			players.add(playerInfos);
		}

		JSONArray barriers = new JSONArray();
		for (Bridge currentBridge : board.getBarriers()) {
			JSONArray barrier = new JSONArray();
			for (Point currentPoint : currentBridge.getVirtualIndex()) {

				barrier.add(currentPoint.x + "," + currentPoint.y);
			}
			barriers.add(barrier);

		}

		jsonObject.put("Players", players);
		jsonObject.put("nbSheepPerPlayer", board.getNbSheepPerPlayer());
		jsonObject.put("Boardsize", board.getBoardSize());
		jsonObject.put("Barriers", barriers);
		jsonObject.put("currentPLayerFirstName", board.getCurrentPlayer().getFirstName());
		jsonObject.put("currentPLayerLastName", board.getCurrentPlayer().getLastName());

		try {
			FileWriter jsonFileWriter = new FileWriter(jsonFilePath);
			jsonFileWriter.write(jsonObject.toJSONString());
			jsonFileWriter.flush();
			jsonFileWriter.close();

			System.out.print(jsonObject);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void SavePlayer(List<Player> listPlayers, String fileName) {
		String jsonFilePath = "resources/players/" + fileName + ".json";

		JSONObject jsonObject = new JSONObject();

		JSONArray players = new JSONArray();
		for (Player currentPlayer : listPlayers) {
			JSONArray player = new JSONArray();
			player.add(currentPlayer.getLastName() + "," + currentPlayer.getFirstName() + "," + currentPlayer.getAge() + "," + currentPlayer.getProfilePicturePath());
			players.add(player);
		}
		jsonObject.put("Players", players);

		try {
			FileWriter jsonFileWriter = new FileWriter(jsonFilePath);
			jsonFileWriter.write(jsonObject.toJSONString());
			jsonFileWriter.flush();
			jsonFileWriter.close();

			System.out.print(jsonObject);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
