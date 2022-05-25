package me.xurround.desklink.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        s1comments.add(new SlideComment("Mikhail", "Представиться"));
        s1comments.add(new SlideComment("Mikhail", "Микро-введение: \"...\""));
        s1comments.add(new SlideComment("Mikhail", "Название проекта"));
        slides.add(new Slide(1, "Your Best Presentation", BitmapFactory.decodeResource(getResources(), R.drawable.sample_presentation1), s1comments));

        List<SlideComment> s2comments = new ArrayList<>();
        s2comments.add(new SlideComment("Mikhail", "Важность удобства управления презентацией для выступающего очень велика..."));
        s2comments.add(new SlideComment("Mikhail", "Важно иметь наводящие подсказки под рукой..."));
        s2comments.add(new SlideComment("Mikhail", "С данными возможностями выступающему будет гораздо легче..."));
        s2comments.add(new SlideComment("Mikhail", "Формулировка задачи исходя из проблемы: \"\""));
        slides.add(new Slide(2, "Convenient way to present", BitmapFactory.decodeResource(getResources(), R.drawable.sample_presentation2), s2comments));

        List<SlideComment> s3comments = new ArrayList<>();
        s3comments.add(new SlideComment("Mikhail", "Система дистанционного управления ПК - это..."));
        s3comments.add(new SlideComment("Mikhail", "Используемые инструменты и технологии: \"...\""));
        s3comments.add(new SlideComment("Mikhail", "Обзор решения: \"...\""));
        s3comments.add(new SlideComment("Mikhail", "Обратить внимание на \"...\""));
        s3comments.add(new SlideComment("Mikhail", "..."));
        slides.add(new Slide(3, "Stay Informative", BitmapFactory.decodeResource(getResources(), R.drawable.sample_presentation3), s3comments));

        List<SlideComment> s4comments = new ArrayList<>();
        s4comments.add(new SlideComment("Mikhail", "Спасибо за внимание"));
        slides.add(new Slide(3, "Thank you", BitmapFactory.decodeResource(getResources(), R.drawable.sample_presentation4), s4comments));

        ViewPager2 vp = view.findViewById(R.id.presentation_slider);
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(new SlideListAdapter(slides));

        return view;
    }
}