package com.example.androidtrainer.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidtrainer.util.ThreadManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Question>> data;

    public LiveData<List<Question>> getText(InputStream inputStream) {
        parse(inputStream);
        data = new MutableLiveData<>();
        return data;
    }

    private void parse(final InputStream inputStream) {
        ThreadManager.enqueue(new Runnable() {
            @Override
            public void run() {
                List<Question> list = new ArrayList<>();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    String header = null;
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("#### ")) {
                            if (header != null) {
                                list.add(new Question(header, sb.toString()));
                                sb = new StringBuilder();
                            }
                            int index = line.indexOf(" ");
                            header = index >= 0 ? line.substring(index + 1) : line;
                        } else if (header != null) {
                            sb.append(line);
                            sb.append('\n');
                        }
                    }
                    if (header != null) {
                        list.add(new Question(header, sb.toString()));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                data.postValue(list);
            }
        });
    }

    static class Question {
        final String q;
        final String a;

        public Question(String q, String a) {
            this.q = q;
            this.a = a;
        }
    }
}