package com.dms.support.parser;

import org.hyunjun.school.School;

public class Parser {
	private static final String SCHOOL_CODE = "G100000170";
	static final School api = new School(School.Type.HIGH, School.Region.DAEJEON, Parser.SCHOOL_CODE);
}
