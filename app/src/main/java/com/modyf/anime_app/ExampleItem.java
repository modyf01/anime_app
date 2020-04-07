package com.modyf.anime_app;

public class ExampleItem {
    private int mImageResource;
    private String mText1;
    private String mText2, mText3;
    private boolean czy;

    public ExampleItem(int imageResource, String text1, String text2, String text3, Boolean cz) {
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
        mText3 = text3;
        czy=cz;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getText1() {
        return mText1;
    }
    public Boolean czyj(){return czy;}
    public String getText2() {
        return mText2;
    }
    public String getText3() {
        return mText3;
    }
}