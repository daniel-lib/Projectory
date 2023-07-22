package com.app.projectory.service;

import java.util.HashMap;
import java.util.Map;

public class FormValidation {
	private HashMap<String, Integer> charLimitRules= new HashMap<>();
	private String[] listOfFields = {"projectTitle", "projectDescription"};
	private int[] fieldCharacterLimitList= {25, 200};
	
	public FormValidation() {
		for(int i = 0; i < listOfFields.length; i++) {
			charLimitRules.put(listOfFields[i], fieldCharacterLimitList[i]);
		}
	}
	
	public int validateUserInput(int  inputLength, String fieldName) {
		if(fieldName.length() <= charLimitRules.get(fieldName))
			return 1;
		return -1;
	}

}
