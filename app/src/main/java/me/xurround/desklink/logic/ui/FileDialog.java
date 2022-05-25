package me.xurround.desklink.logic.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FileDialog
{
    private static final String PARENT_DIR = "..";
    private String[] fileList;
    private File currentPath;

    public interface FileSelectedListener
    {
        void fileSelected(File file);
    }

    private FileSelectedListener fileListener;

    private final Activity activity;
    private final String fileEndsWith;
    private final File initialPath;

    public FileDialog(Activity activity, File initialPath, String fileEndsWith)
    {
        this.activity = activity;
        this.fileEndsWith = fileEndsWith != null ? fileEndsWith.toLowerCase() : null;
        if (!initialPath.exists())
            initialPath = Environment.getExternalStorageDirectory();
        this.initialPath = initialPath;
        loadFileList(initialPath);
    }

    public Dialog createFileDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(currentPath.getPath());
        builder.setItems(fileList, (dialog1, which) ->
        {
            String fileChosen = fileList[which];
            File chosenFile = getChosenFile(fileChosen);
            if (chosenFile.isDirectory())
            {
                loadFileList(chosenFile);
                dialog1.cancel();
                dialog1.dismiss();
                showDialog();
            }
            else
                fileListener.fileSelected(chosenFile);
        });

        return builder.show();
    }


    public void setFileListener(FileSelectedListener listener)
    {
        fileListener = listener;
    }

    public void showDialog()
    {
        createFileDialog().show();
    }

    private void loadFileList(File path)
    {
        this.currentPath = path;
        List<String> r = new ArrayList<>();
        if (path.exists())
        {
            if (path.getParentFile() != null && path != initialPath)
                r.add(PARENT_DIR);
            FilenameFilter filter = (dir, filename) ->
            {
                File sel = new File(dir, filename);
                if (!sel.canRead())
                    return false;
                return fileEndsWith == null || filename.toLowerCase().endsWith(fileEndsWith) || sel.isDirectory();
            };
            Collections.addAll(r, Objects.requireNonNull(path.list(filter)));
        }
        fileList = r.toArray(new String[]{});
    }

    private File getChosenFile(String fileChosen)
    {
        if (fileChosen.equals(PARENT_DIR))
            return currentPath.getParentFile();
        else
            return new File(currentPath, fileChosen);
    }
}
