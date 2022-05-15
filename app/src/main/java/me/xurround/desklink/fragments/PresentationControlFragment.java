package me.xurround.desklink.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.xurround.desklink.R;
import me.xurround.desklink.adapters.SlideListAdapter;
import me.xurround.desklink.models.Slide;
import me.xurround.desklink.models.SlideComment;

public class PresentationControlFragment extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_presentation_control, container, false);

        List<Slide> slides = new ArrayList<>();

        List<SlideComment> s1comments = new ArrayList<>();
        s1comments.add(new SlideComment("lol", "privet"));
        s1comments.add(new SlideComment("lol1", "privet2"));
        s1comments.add(new SlideComment("lol5", "azazaz"));
        slides.add(new Slide(1, "LOL", Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888), s1comments));

        List<SlideComment> s2comments = new ArrayList<>();
        s2comments.add(new SlideComment("me", "Works"));
        s2comments.add(new SlideComment("me", "Data test"));
        s2comments.add(new SlideComment("me", "Slider is awesome!"));
        s2comments.add(new SlideComment("me", "Works"));
        s2comments.add(new SlideComment("me", "Data test"));
        s2comments.add(new SlideComment("me", "Slider is awesome!"));
        s2comments.add(new SlideComment("me", "Works"));
        s2comments.add(new SlideComment("me", "Data test"));
        s2comments.add(new SlideComment("me", "Slider is awesome!"));
        s2comments.add(new SlideComment("me", "Works"));
        s2comments.add(new SlideComment("me", "Data test"));
        s2comments.add(new SlideComment("me", "Slider is awesome!"));
        s2comments.add(new SlideComment("me", "Works"));
        s2comments.add(new SlideComment("me", "Data test"));
        s2comments.add(new SlideComment("me", "Slider is awesome!"));
        s2comments.add(new SlideComment("me", "Works"));
        s2comments.add(new SlideComment("me", "Data test"));
        s2comments.add(new SlideComment("me", "Slider is awesome!"));
        s2comments.add(new SlideComment("me", "Works"));
        s2comments.add(new SlideComment("me", "Data test"));
        s2comments.add(new SlideComment("me", "Slider is awesome!"));
        s2comments.add(new SlideComment("me", "Works"));
        s2comments.add(new SlideComment("me", "Data test"));
        s2comments.add(new SlideComment("me", "Slider is awesome!"));
        slides.add(new Slide(2, "Important data", Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888), s2comments));

        ViewPager2 vp = view.findViewById(R.id.presentation_slider);
        vp.setAdapter(new SlideListAdapter(slides));

        return view;
    }
}