package com.example.bm_admin.timekeeper.adapter;

import android.graphics.Bitmap;
import android.net.Uri;

public class NavDrawerItem {
	private String fromName, message, time, response, identifier, groupName,
			year, percentage, id, monthName, no_Of_days, present, absent,
			filePath, groupID, userID, userName, messageCount, date,
			leaveCount, user_type, count, sendDt, msgSource, leaveID,
			approvedBy, status, msg_type,halfDay, toDate, fromDate;
	private Bitmap user_Image;

	private int leavePercentage, icon, position,titleTamil;
	private String title, color;
	private Uri uri;
	private boolean isSelf;

	public NavDrawerItem() {
	}

	public NavDrawerItem(String year) {
		super();
		this.year = year;
	}

	public NavDrawerItem(String id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	public NavDrawerItem(int icon) {
		super();
		this.icon = icon;
	}

	public NavDrawerItem(int icon, String title, String count) {
		super();
		this.icon = icon;
		this.title = title;
		this.count = count;
	}

	public NavDrawerItem(int icon, int titleTamil, String count) {
		super();
		this.icon = icon;
		this.titleTamil = titleTamil;
		this.count = count;
	}

	public NavDrawerItem(String userName, String status, String date,
						 String time) {
		super();
		this.userName = userName;
		this.status = status;
		this.date = date;
		this.time = time;
	}

	public NavDrawerItem(int position, String message, String date,
						 String leaveCount, String user_type) {
		super();
		this.message = message;
		this.date = date;
		this.setPosition(position);
		this.leaveCount = leaveCount;
		this.user_type = user_type;
	}

	public NavDrawerItem(int position, String message, String date,
						 String fromDate, String toDate, String halfDay, String leaveCount,
						 String leaveID, String approvedBy, String status, String groupName,
						 String userName, String userType) {
		super();
		this.message = message;
		this.date = date;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.halfDay = halfDay;
		this.position = position;
		this.leaveID = leaveID;
		this.leaveCount = leaveCount;
		this.approvedBy = approvedBy;
		this.status = status;
		this.groupName = groupName;
		this.userName = userName;
		this.user_type = userType;
	}

	public NavDrawerItem(String monthName, String no_Of_days, String present,
						 String absent, int leavePercentage) {
		super();
		this.monthName = monthName;
		this.no_Of_days = no_Of_days;
		this.present = present;
		this.absent = absent;
		this.leavePercentage = leavePercentage;
	}

	public NavDrawerItem(String message, String time, String groupName,
						 String groupId, String userID, String msgCount, String user_type,
						 String response) {
		super();
		this.message = message;
		this.time = time;
		this.groupName = groupName;
		this.userID = userID;
		this.messageCount = msgCount;
		this.groupID = groupId;
		this.user_type = user_type;
		this.response = response;
	}

	public NavDrawerItem(String message, String msgSource, String time, String groupName,
						 String userName, String groupId, String userID, String msgCount,
						 String user_type, String response, Bitmap user_Image) {
		super();
		this.message = message;
		this.time = time;
		this.groupName = groupName;
		this.userID = userID;
		this.userName = userName;
		this.messageCount = msgCount;
		this.groupID = groupId;
		this.user_type = user_type;
		this.msgSource=msgSource;
		this.response = response;
		this.setUser_Image(user_Image);
	}

	public NavDrawerItem(String fromName, String message, String time,
						 String response, boolean isSelf, String identifier,
						 String messageID, String sendDt, String msgSource, String msg_type) {
		super();
		this.fromName = fromName;
		this.message = message;
		this.time = time;
		this.sendDt = sendDt;
		this.response = response;
		this.isSelf = isSelf;
		this.identifier = identifier;
		this.id = messageID;
		this.msgSource = msgSource;
		this.msg_type = msg_type;
	}

	public NavDrawerItem(String fromName, Uri message, String time,
						 String response, boolean isSelf, String identifier,
						 String messageID, String sendDt, String msgSource, String status,
						 String msg_type) {
		super();
		this.fromName = fromName;
		this.uri = message;
		this.time = time;
		this.sendDt = sendDt;
		this.response = response;
		this.isSelf = isSelf;
		this.identifier = identifier;
		this.id = messageID;
		this.msgSource = msgSource;
		this.status = status;
		this.msg_type = msg_type;
	}

	public NavDrawerItem(String fromName, String time, String response,
						 Uri uri, boolean isSelf, String identifier) {
		super();
		this.fromName = fromName;
		this.time = time;
		this.response = response;
		this.setUri(uri);
		this.isSelf = isSelf;
		this.identifier = identifier;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSelf() {
		return isSelf;
	}

	public void setSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Uri getUri() {
		return uri;
	}

	public void setUri(Uri uri) {
		this.uri = uri;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMonthName() {
		return monthName;
	}

	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	public String getNo_Of_days() {
		return no_Of_days;
	}

	public void setNo_Of_days(String no_Of_days) {
		this.no_Of_days = no_Of_days;
	}

	public String getPresent() {
		return present;
	}

	public void setPresent(String present) {
		this.present = present;
	}

	public String getAbsent() {
		return absent;
	}

	public void setAbsent(String absent) {
		this.absent = absent;
	}

	public int getLeavePercentage() {
		return leavePercentage;
	}

	public void setLeavePercentage(int leavePercentage) {
		this.leavePercentage = leavePercentage;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(String messageCount) {
		this.messageCount = messageCount;
	}

	public String getLeaveCount() {
		return leaveCount;
	}

	public void setLeaveCount(String leaveCount) {
		this.leaveCount = leaveCount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public int getTitleTamil() {
		return titleTamil;
	}

	public void setTitleTamil(int titleTamil) {
		this.titleTamil = titleTamil;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getSendDt() {
		return sendDt;
	}

	public void setSendDt(String sendDt) {
		this.sendDt = sendDt;
	}

	public String getMsgSource() {
		return msgSource;
	}

	public void setMsgSource(String msgSource) {
		this.msgSource = msgSource;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getLeaveID() {
		return leaveID;
	}

	public void setLeaveID(String leaveID) {
		this.leaveID = leaveID;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Bitmap getUser_Image() {
		return user_Image;
	}

	public void setUser_Image(Bitmap user_Image) {
		this.user_Image = user_Image;
	}

	public String getMsg_type() {
		return msg_type;
	}

	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}

	public String getHalfDay() {
		return halfDay;
	}

	public void setHalfDay(String halfDay) {
		this.halfDay = halfDay;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

}
