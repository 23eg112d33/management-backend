package com.lms.dto;

public class GradeRequest {

    private Integer marks;
    private String feedback;

    public GradeRequest() {}

    public Integer getMarks() { return marks; }
    public void setMarks(Integer marks) { this.marks = marks; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
