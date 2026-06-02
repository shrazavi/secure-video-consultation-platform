package com.shrazavi.dadmehr.DataClass;

public class CallLog {
   public String _id;
   public String room;
   public String from;
   public String to;
   public String hours;
   public long starttime;
   public long endtime;
   public long date;
   public String status;
   public String type;
   public String condition;
   public int report;

//   public String imgurl;


   public String get_id() {
      return _id;
   }

   public void set_id(String _id) {
      this._id = _id;
   }

   public String getRoom() {
      return room;
   }

   public void setRoom(String room) {
      this.room = room;
   }

   public String getFrom() {
      return from;
   }

   public void setFrom(String from) {
      this.from = from;
   }

   public String getTo() {
      return to;
   }

   public void setTo(String to) {
      this.to = to;
   }

   public String getHours() {
      return hours;
   }

   public void setHours(String hours) {
      this.hours = hours;
   }

   public long getStarttime() {
      return starttime;
   }

   public void setStarttime(long starttime) {
      this.starttime = starttime;
   }

   public long getEndtime() {
      return endtime;
   }

   public void setEndtime(long endtime) {
      this.endtime = endtime;
   }

   public long getDate() {
      return date;
   }

   public void setDate(long date) {
      this.date = date;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getCondition() {
      return condition;
   }

   public void setCondition(String condition) {
      this.condition = condition;
   }

   public int getReport() {
      return report;
   }

   public void setReport(int report) {
      this.report = report;
   }
}
