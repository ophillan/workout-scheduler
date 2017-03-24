package ru.gymthing.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Mart on 20/03/2017.
 */
public class Workout implements Serializable {
    private String content;
    private String comments;
    private String day;
    private int finished;
    private Date date;

    public Workout(String content, String day, Date date) {
        this.content = content;
        this.comments = "";
        this.day = day;
        this.finished = 0;
        this.date = date;
    }

    public Workout() {
        this.comments = "";
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getFinishedCode() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("Workout on %s, %s: \"%s.\" is %s! \n Comments: \n %s",
                day, date, content, finished == 1 ? "complete" : "not complete", comments);
    }
}
