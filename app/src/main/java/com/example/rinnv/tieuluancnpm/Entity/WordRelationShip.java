package com.example.rinnv.tieuluancnpm.Entity;

/**
 * Created by rinnv on 5/3/2017.
 */

public class WordRelationShip {

    private int Root;
    private String Word_Title;
    private String Word_Title_VN;
    private String WordType;


    public WordRelationShip(
            int Root,
            String Word_Title,
            String Word_Title_VN,
            String WordType
    ) {

        this.Word_Title = Word_Title;
        this.Word_Title_VN = Word_Title_VN;
        this.Root = Root;
        this.WordType =WordType;
    }


    public String getWord_Title() {
        return Word_Title;
    }

    public String getWord_Title_VN() {
        return Word_Title_VN;
    }


    public int getWord_Root() {

        return Root;
    }

    public String getWordType() {
        if (WordType != null)
            return "("+WordType+")";
        else return "";
    }
}
