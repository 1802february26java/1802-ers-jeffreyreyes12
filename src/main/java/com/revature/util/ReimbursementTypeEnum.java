package com.revature.util;

public enum ReimbursementTypeEnum {
	OTHER (1), 
	COURSE (2), 
	CERTIFICATION (3), 
	TRAVELING (4) 
	;
	
	private int type;
	
	private ReimbursementTypeEnum(int type) {
		this.type = type;
	}
	
	public String toString() {
		switch (type) {
		case 1:
			return "OTHER";
		case 2:
			return "COURSE";
		case 3:
			return "CERTIFICATION";
		case 4:
			return "TRAVELING";
		}
		
		return null;
	}
};
