package com.example.amia.schoolrent.Task;

import android.content.Context;
import android.os.Handler;


import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.PublicKey;

public interface KeyTask {
    PublicKey getKey(Context context) throws IOException, JSONException;
    void isChangeKey(Context context, Handler handler) throws Exception;
}
