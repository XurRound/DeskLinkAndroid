package me.xurround.desklink.models;

import android.graphics.Bitmap;

import java.util.List;

public class Slide
{
    private final int id;
    private final String header;
    private final Bitmap image;
    private final List<SlideComment> comments;

    public Slide(int id, String header, Bitmap image, List<SlideComment> comments)
    {
        this.id = id;
        this.header = header;
        this.image = image;
        this.comments = comments;
    }

    public int getId()
    {
        return id;
    }

    public String getHeader()
    {
        return header;
    }

    public Bitmap getImage()
    {
        return image;
    }

    public List<SlideComment> getComments()
    {
        return comments;
    }
}
