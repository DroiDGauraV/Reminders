package com.example.reminders;

public class RemindersItems {

    String id;
    String head;
    String msg;
    String date;
    String time;

    public RemindersItems(String id, String head, String msg, String date, String time) {
        this.id = id;
        this.head = head;
        this.msg = msg;
        this.date = date;
        this.time = time;
    }


    public String getId() {
        return id;
    }

    public String getHead() {
        return head;
    }

    public String getMsg() {
        return msg;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

}
