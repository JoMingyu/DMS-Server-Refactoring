package com.dms.support.parser;

import org.hyunjun.school.School;

public abstract class Parser {
	public static final String SCHOOL_CODE = "G100000170";
	School api = new School(School.Type.HIGH, School.Region.DAEJEON, Parser.SCHOOL_CODE);
}
