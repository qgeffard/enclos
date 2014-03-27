package com.enclos.data;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SimpleReader {

	private String jsonFilePath;
	private Object file;
	
	public SimpleReader(String fileName) {
		JSONParser parser = new JSONParser();
		try {
			this.file = parser.parse(new FileReader("resources/save/"+ fileName + ".json"));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public Map<String,Object> read(){
		Map<String,Object> values = new HashMap<String,Object>();
		try {
			JSONObject reader = (JSONObject) file;

			for(Params param : Params.values()){
				Object value = reader.get(param.toString());
				values.put(param.toString(),value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return values;
	}

	private enum Params {
		SHEEPNUMBER,BOARDSIZE,SHEEPSPOSITIONS,BARRIERS; // ; is required here.

		@Override
		public String toString() {
			// only capitalize the first letter
			String s = super.toString();
			return s.substring(0, 1) + s.substring(1).toLowerCase();
		}
	}
}