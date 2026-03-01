package com.example.ku7_milesconv;

import android.content.Context;
import android.os.Parcelable;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class NoteIO {
    public String password = "0000";
    public String[] titles = {"Empty note"};
    public String[] contents = {"Empty note"};
    private Context context;

    public NoteIO(boolean loadContent, Context c) { // load password
        this.context = c;
        try {
            File file = new File(this.context.getFilesDir(), "data.bin");
            FileInputStream f = new FileInputStream(file);
            byte[] buffer = new byte[4];
            f.read(buffer);
            buffer = new byte[decode(buffer)];
            f.read(buffer);
            this.password = new String(buffer, StandardCharsets.UTF_8);

            if (loadContent) {
                buffer = new byte[4];
                f.read(buffer);
                int num = decode(buffer);
                this.titles = new String[num];
                this.contents = new String[num];

                for (int i = 0; i < num; i++) {
                    buffer = new byte[4];
                    f.read(buffer);
                    buffer = new byte[decode(buffer)];
                    f.read(buffer);
                    this.titles[i] = new String(buffer, StandardCharsets.UTF_8);

                    buffer = new byte[4];
                    f.read(buffer);
                    buffer = new byte[decode(buffer)];
                    f.read(buffer);
                    this.contents[i] = new String(buffer, StandardCharsets.UTF_8);
                }
            }
            f.close();

        } catch (Exception e) {
            this.password = "0000";
            this.titles = new String[]{"New note"};
            this.contents = new String[]{e.toString()};
            saveNote();
        }
    }

    public void saveNote() { // save content
        try {
            File file = new File(this.context.getFilesDir(), "data.bin");
            FileOutputStream f = new FileOutputStream(file);
            byte[] buffer = this.password.getBytes(StandardCharsets.UTF_8);
            f.write(encode(buffer.length));
            f.write(buffer);

            f.write(encode(this.titles.length));
            for (int i = 0; i < this.titles.length; i++) {
                buffer = this.titles[i].getBytes(StandardCharsets.UTF_8);
                f.write(encode(buffer.length));
                f.write(buffer);
                buffer = this.contents[i].getBytes(StandardCharsets.UTF_8);
                f.write(encode(buffer.length));
                f.write(buffer);
            }

            f.close();
            showMsg("Note saved");

        } catch (Exception e) {
            showMsg(e.toString());
        }
    }

    public void sortNote() {
        int n = this.titles.length;
        for (int i = 1; i < n; ++i) {
            String keyTitle = this.titles[i];
            String keyContent = this.contents[i];
            int j = i - 1;
            while (j >= 0 && this.titles[j].compareTo(keyTitle) > 0) {
                this.titles[j + 1] = this.titles[j];
                this.contents[j + 1] = this.contents[j];
                j = j - 1;
            }
            this.titles[j + 1] = keyTitle;
            this.contents[j + 1] = keyContent;
        }
    }

    public void showMsg(String msg) {
        Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();
    }

    private byte[] encode(int num) {
        byte[] result = new byte[4];
        for (int i = 0; i < 4; i++) {
            result[i] = (byte) (num % 256);
            num = num / 256;
        }
        return result;
    }

    private int decode(byte[] data) {
        int result = 0;
        int mul = 1;
        for (int i = 0; i < 4; i++) {
            result = result + mul * (data[i] & 0xFF);
            mul = mul * 256;
        }
        return result;
    }
}