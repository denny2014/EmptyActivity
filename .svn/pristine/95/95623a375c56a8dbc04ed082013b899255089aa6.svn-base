package com.zet.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RSSHandler extends DefaultHandler {
	private RSSFeed RssFeed;
	private DETAIL mDetail;
	private ATTACH mAttach;
	private static final int ADMDIV_CODE = 1;
	private static final int SET_YEAR = 2;
	private static final int DOC_YEAR = 3;
	private static final int DIV_CODE = 4;
	private static final int PRJ_CODE = 5;
	private static final int FILE_ID = 6;
	private static final int TITLE = 7;
	private static final int REPORT_DATE = 8;
	private static final int DEP_CODE = 9;
	private static final int DEP_NAME = 10;
	private static final int LEADER = 11;
	private static final int REPORT_USER = 12;
	private static final int FILE_NO = 13;
	private static final int LEADER_NAME = 14;
	private static final int FILE_TYPE = 15;

	private static final int DETAIL_SET_YEAR = 16;
	private static final int DETAIL_FILE_ID = 17;
	private static final int DETAIL_ELEMENT_CODE = 18;
	private static final int DETAIL_ELEMENT_NAME = 19;
	private static final int DETAIL_PROC_CONTENT = 20;

	private static final int ATTACH_SET_YEAR = 21;
	private static final int ATTACH_FILE_ID = 22;
	private static final int ATTACH_FILE_TYPE = 23;
	private static final int ATTACH_ATTACH_ID = 24;
	private static final int ATTACH_FILE_BIN = 25;
	private static final int ATTACH_FILE_NAME = 26;

	private int currentstate = 0;
	private String currentParent;

	public RSSFeed getFeed() {
		return RssFeed;
	}


	public RSSHandler() {
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		RssFeed = new RSSFeed();
		mDetail = new DETAIL();
		mAttach = new ATTACH();

	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		if (localName.equals("WORK")) {
			currentstate = 0;
			currentParent = "WORK";
		} else if (localName.equals("ADMDIV_CODE")
				&& currentParent.equals("WORK")) {
			currentstate = ADMDIV_CODE;
		} else if (localName.equals("SET_YEAR") && currentParent.equals("WORK")) {
			currentstate = SET_YEAR;
		} else if (localName.equals("DOC_YEAR") && currentParent.equals("WORK")) {
			currentstate = DOC_YEAR;
		} else if (localName.equals("DIV_CODE") && currentParent.equals("WORK")) {
			currentstate = DIV_CODE;
		} else if (localName.equals("PRJ_CODE") && currentParent.equals("WORK")) {
			currentstate = PRJ_CODE;
		} else if (localName.equals("FILE_ID") && currentParent.equals("WORK")) {
			currentstate = FILE_ID;
		} else if (localName.equals("TITLE") && currentParent.equals("WORK")) {
			currentstate = TITLE;
		} else if (localName.equals("REPORT_DATE")
				&& currentParent.equals("WORK")) {
			currentstate = REPORT_DATE;
		} else if (localName.equals("DEP_CODE") && currentParent.equals("WORK")) {
			currentstate = DEP_CODE;
		} else if (localName.equals("DEP_NAME") && currentParent.equals("WORK")) {
			currentstate = DEP_NAME;
		} else if (localName.equals("LEADER") && currentParent.equals("WORK")) {
			currentstate = LEADER;
		} else if (localName.equals("REPORT_USER") && currentParent.equals("WORK")) {
			currentstate = REPORT_USER;
		} else if (localName.equals("FILE_NO") && currentParent.equals("WORK")) {
			currentstate = FILE_NO;
		} else if (localName.equals("LEADER_NAME") && currentParent.equals("WORK")) {
			currentstate = LEADER_NAME;
		} else if (localName.equals("FILE_TYPE") && currentParent.equals("WORK")) {
			currentstate = FILE_TYPE;
		} else if (localName.equals("DETAIL")) {
			currentstate = 0;
			currentParent = "DETAIL";
			mDetail = new DETAIL();
		} else if (localName.equals("SET_YEAR") && currentParent.equals("DETAIL")) {
			currentstate = DETAIL_SET_YEAR;
		}else if (localName.equals("FILE_ID") && currentParent.equals("DETAIL")) {
			currentstate = DETAIL_FILE_ID;
		}else if (localName.equals("ELEMENT_CODE") && currentParent.equals("DETAIL")) {
			currentstate = DETAIL_ELEMENT_CODE;
		}else if (localName.equals("ELEMENT_NAME") && currentParent.equals("DETAIL")) {
			currentstate = DETAIL_ELEMENT_NAME;
		}else if (localName.equals("PROC_CONTENT") && currentParent.equals("DETAIL")) {
			currentstate = DETAIL_PROC_CONTENT;
		}else if (localName.equals("ATTACH")) {
			currentstate = 0;
			currentParent = "ATTACH";
			mAttach=new ATTACH();
		}else if (localName.equals("SET_YEAR") && currentParent.equals("ATTACH")) {
			currentstate = ATTACH_SET_YEAR;
		}else if (localName.equals("FILE_ID") && currentParent.equals("ATTACH")) {
			currentstate = ATTACH_FILE_ID;
		}else if (localName.equals("FILE_TYPE") && currentParent.equals("ATTACH")) {
			currentstate = ATTACH_FILE_TYPE;
		}else if (localName.equals("ATTACH_ID") && currentParent.equals("ATTACH")) {
			currentstate = ATTACH_ATTACH_ID;
		}else if (localName.equals("FILE_BIN") && currentParent.equals("ATTACH")) {
			currentstate = ATTACH_FILE_BIN;
		}else if (localName.equals("FILE_NAME") && currentParent.equals("ATTACH")) {
			currentstate = ATTACH_FILE_NAME;
		}else{
			currentstate = 0;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if(localName.equals("DETAIL")){
			RssFeed.addDETAILItem(mDetail);
		}else if(localName.equals("ATTACH")){
			RssFeed.addATTACHItem(mAttach);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		String theString = new String(ch, start, length);
		switch (currentstate) {
		case ADMDIV_CODE:
			RssFeed.setADMDIV_CODE(theString);
			currentstate = 0;
			break;
		case SET_YEAR:
			RssFeed.setSET_YEAR(theString);
			currentstate = 0;
			break;
		case DOC_YEAR:
			RssFeed.setDOC_YEAR(theString);
			currentstate = 0;
			break;
		case DIV_CODE:
			RssFeed.setDIV_CODE(theString);
			currentstate = 0;
			break;
		case PRJ_CODE:
			RssFeed.setPRJ_CODE(theString);
			currentstate = 0;
			break;
		case FILE_ID:
			RssFeed.setFILE_ID(theString);
			currentstate = 0;
			break;
		case TITLE:
			RssFeed.setTITLE(theString);
			currentstate = 0;
			break;
		case REPORT_DATE:
			RssFeed.setREPORT_DAT(theString);
			currentstate = 0;
			break;
		case DEP_CODE:
			RssFeed.setDEP_CODE(theString);
			currentstate = 0;
			break;
		case DEP_NAME:
			RssFeed.setDEP_NAME(theString);
			currentstate = 0;
			break;
		case LEADER:
			RssFeed.setLEADER(theString);
			currentstate = 0;
			break;
		case REPORT_USER:
			RssFeed.setREPORT_USER(theString);
			currentstate = 0;
			break;
		case FILE_NO:
			RssFeed.setFILE_NO(theString);
			currentstate = 0;
			break;
		case LEADER_NAME:
			RssFeed.setLEADER_NAME(theString);
			currentstate = 0;
			break;
		case FILE_TYPE:
			RssFeed.setFILE_TYPE(theString);
			currentstate = 0;
			break;
		case DETAIL_SET_YEAR:
			mDetail.setSET_YEAR(theString);
			currentstate = 0;
			break;
		case DETAIL_FILE_ID:
			mDetail.setFILE_ID(theString);
			currentstate = 0;
			break;
		case DETAIL_ELEMENT_CODE:
			mDetail.setELEMENT_CODE(theString);
			currentstate = 0;
			break;
		case DETAIL_ELEMENT_NAME:
			mDetail.setELEMENT_NAME(theString);
			currentstate = 0;
			break;
		case DETAIL_PROC_CONTENT:
			mDetail.setPROC_CONTENT(theString);
			currentstate = 0;
			break;
		case ATTACH_SET_YEAR:
			mAttach.setSET_YEAR(theString);
			currentstate = 0;
			break;
		case ATTACH_FILE_ID:
			mAttach.setFILE_ID(theString);
			currentstate = 0;
			break;
		case ATTACH_FILE_TYPE:
			mAttach.setFILE_TYPE(theString);
			currentstate = 0;
			break;
		case ATTACH_ATTACH_ID:
			mAttach.setATTACH_ID(theString);
			currentstate = 0;
			break;
		case ATTACH_FILE_BIN:
			mAttach.setFILE_BIN(theString);
			currentstate = 0;
			break;
		case ATTACH_FILE_NAME:
			mAttach.setFILE_NAME(theString);
			currentstate = 0;
			break;
		}
	}
}