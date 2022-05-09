package com.example.chatapp;

import java.util.Calendar;

public class DateDisplay {
    int year, monthWithDay;
    String time, monthWithDayString;
    public DateDisplay(long recordedTime){
        Calendar timeInPast = Calendar.getInstance();
        timeInPast.setTimeInMillis(recordedTime);
        year = timeInPast.get(Calendar.YEAR);
        monthWithDay = timeInPast.get(Calendar.DAY_OF_YEAR);
        monthWithDayString = timeInPast.get(Calendar.MONTH) + "/" +timeInPast.get(Calendar.DAY_OF_MONTH);
        if (timeInPast.get(Calendar.HOUR) < 10)
            time = "0"+timeInPast.get(Calendar.HOUR)+":";
        else
            time = timeInPast.get(Calendar.HOUR)+":";
        if (timeInPast.get(Calendar.MINUTE) < 10)
            time += "0"+timeInPast.get(Calendar.MINUTE);
        else
            time += timeInPast.get(Calendar.MINUTE);
    }
    public String returnCurrentTime(){
        Calendar timeNow = Calendar.getInstance();
        if (year != timeNow.get(Calendar.YEAR)){
            return year + " " +  monthWithDayString +  time;
        }
        else if (monthWithDay != timeNow.get(Calendar.DAY_OF_YEAR)){
            return monthWithDayString + " " + time;
        }
        else
            return time;
    }
}
