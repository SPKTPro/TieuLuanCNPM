package com.example.rinnv.tieuluancnpm;

/**
 * Created by rinnv on 25/10/2016.
 */

public class Topic {
    private int MainTopic_Id;
    private String Topic_Id;
    private String Topic_Title;
    private String Topic_Title_VN;
    private int Topic_Process;

    public Topic(int MainTopic_Id,
                 String Topic_Id,
                 String Topic_Title,
                 String Topic_Title_VN,
                 int Topic_Process
    ) {
        this.MainTopic_Id = MainTopic_Id;
        this.Topic_Id = Topic_Id;
        this.Topic_Title = Topic_Title;
        this.Topic_Title_VN = Topic_Title_VN;
        this.Topic_Process = Topic_Process;
    }

    public int getMainTopic_Id() {
        return MainTopic_Id;
    }

    public int getTopic_Process() {
        return Topic_Process;
    }

    public String getTopic_Id() {
        return Topic_Id;
    }

    public String getTopic_Title() {
        return Topic_Title;
    }

    public String getTopic_Title_VN() {
        return Topic_Title_VN;
    }

}
