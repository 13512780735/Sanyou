package com.likeits.sanyou.entity;

/**
 * Created by Administrator on 2017/11/28.
 */

public class WeatherInfoEntity {

    /**
     * date : 2015-09-17
     * dayTime : 阴天
     * night : 局部多云
     * temperature : 26°C/15°C
     * week : 星期四
     * wind : 东南风2级
     */

    private String date;
    private String dayTime;
    private String night;
    private String temperature;
    private String week;
    private String wind;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public String getNight() {
        return night;
    }

    public void setNight(String night) {
        this.night = night;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
