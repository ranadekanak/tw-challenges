package com.example.springsocial.util;

public class Stage1 {

	public static void main(String[] args) {
		String input = "GOVV GO MKX NY SD WI GKI, YB GO MKX KVV MYWO LKMU SX DSWO PYB DRO XOHD KVSQXWOXD KXN IYE'BO GOVMYWO DY DBI KXN USVV WO DROX, SX YR, CKI, KXYDROB 5,000 IOKBC?";
		int key = 10;
		StringBuilder sb = new StringBuilder();
	    for (char c : input.toCharArray()) {
	    	int ascii = (int)c;
	    	if(ascii >=65 && ascii<=90) {
	    		int decodedAscii = ascii - key;
	    		if(decodedAscii < 65) {
	    			decodedAscii += 26;
	    		}
	    		sb.append((char)decodedAscii);
	    	} else {
	    		sb.append(c);
	    	}
	    }
	    	
	    System.out.println(sb.toString());
	    
	}
}
