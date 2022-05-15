package me.xurround.desklink.models;

public class SlideComment
{
    private final String author;
    private final String message;

    public SlideComment(String author, String message)
    {
        this.author = author;
        this.message = message;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getMessage()
    {
        return message;
    }
}
