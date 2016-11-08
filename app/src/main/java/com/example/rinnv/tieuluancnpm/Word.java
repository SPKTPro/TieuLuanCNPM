package com.example.rinnv.tieuluancnpm;

/**
 * Created by rinnv on 25/10/2016.
 */

public class Word {
    private String Topic_Id;
    private int Word_Id;
    private String Word_Title;
    private String Word_Title_VN;
    private int Word_check;
    private int Word_Remind;
    private String Example;
    private String Example_VN;

    public Word(
             String Topic_Id,
             int Word_Id,
             String Word_Title,
             String Word_Title_VN,
             int Word_check,
             String Example,
             String Example_VN,
             int Word_Remind
    )
    {
        this.Topic_Id =Topic_Id;
        this.Word_Id=Word_Id;
        this.Word_Title=Word_Title;
        this.Word_Title_VN=Word_Title_VN;
        this.Word_check=Word_check;
        this.Example=Example;
        this.Example_VN=Example_VN;
        this.Word_Remind=Word_Remind;
    }


    public String getTopic_Id() {
        return Topic_Id;
    }

    public boolean getWord_check() {

        return (Word_check==1) ? true: false;
    }

    public int getWord_Id() {
        return Word_Id;
    }

    public String getExample() {
        return Example;
    }

    public String getExample_VN() {
        return Example_VN;
    }

    public String getWord_Title() {
        return Word_Title;
    }

    public String getWord_Title_VN() {
        return Word_Title_VN;
    }


    public boolean getWord_Remind() {

        return (Word_Remind==1) ? true: false;
    }
}
