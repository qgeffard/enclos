package com.enclos.data;
import java.awt.Point;
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
	public SimpleWriter(Board board, String fileName) {
		this.jsonFilePath = "resources/save/"+fileName+".json";

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("SheepNumber", board.getNbSheep() );

		jsonObject.put("BoardSize", board.getBoardSize());
		
		JSONArray sheepsPos = new JSONArray();
		for(Sheep currentSheep : board.getSheeps()){
			sheepsPos.add(currentSheep.getOwner()+","+currentSheep.getVirtualIndexHexagon().x+","+currentSheep.getVirtualIndexHexagon().y);
		}
		jsonObject.put("SheepsPositions", sheepsPos);


		JSONArray barriers = new JSONArray();
		for(Bridge currentBridge : board.getBarriers()){
			JSONArray barrier = new JSONArray();
			for(Point currentPoint : currentBridge.getVirtualIndex()){
				
				barrier.add(currentPoint.x+","+currentPoint.y);
			}
			barriers.add(barrier);
				
		}
		
		jsonObject.put("Barriers", barriers);
		

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
