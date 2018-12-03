package com.cyztc.app.bean;

import java.util.Map;

/**
 * Created by ywl on 2017/2/18.
 */

public class InvestgateBean extends BaseBean{

    private Map<String, String> answer;
    private String id;
    private String title;
    private String targetId;
    private int isSubmitedAnswer;
    private String name;

    public Map<String, String> getAnswer() {
        return answer;
    }

    public void setAnswer(Map<String, String> answer) {
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public int getIsSubmitedAnswer() {
        return isSubmitedAnswer;
    }

    public void setIsSubmitedAnswer(int isSubmitedAnswer) {
        this.isSubmitedAnswer = isSubmitedAnswer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
