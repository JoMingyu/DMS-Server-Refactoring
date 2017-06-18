package com.dms.support.parser;

import org.hyunjun.school.SchoolMenu;

public interface Parser {
	public static final String SCHOOL_CODE = "G100000170";
	
	public static final String DEFAULT_URL = "http://dsmhs.djsch.kr/main.do";
	public static final String MEAL = "http://stu.dje.go.kr/sts_sci_md00_001.do?schulCode=G100000170&schulCrseScCode=4&schulKndScCode=04&schYm=?";
	public static final String PLAN = "http://stu.dje.go.kr/sts_sci_sf01_001.do?schulCode=G100000170&schulCrseScCode=4&schulKndScCode=04&ay=?&mm=?";
	public static final String BROAD = "http://dsmhs.djsch.kr/boardCnts/list.do?boardID=54793&m=0201&s=dsmhs&page=?";
	public static final String FAMILER = "http://dsmhs.djsch.kr/boardCnts/list.do?boardID=54794&m=0202&s=dsmhs&page=?";
	public static final String CHALLENGE = "http://dsmhs.djsch.kr/boardCnts/list.do?boardID=54795&m=0204&s=dsmhs&page=?";
	
	public abstract SchoolMenu parse(int year, int month, int date);
	public abstract void insertMealToDB(SchoolMenu dayMenu, int year, int month, int date);
}
