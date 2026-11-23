package es.upm.etsiinf.gib.proyecto;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginThread implements Runnable {

    private static final String TAG = "LOGIN_THREAD";

    private Context ctx;
    private String username, password;

    public LoginThread(Context ctx, String username, String password) {
        this.ctx = ctx;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {

        HttpURLConnection conn = null;

        try {
            URL url = new URL("https://fakestoreapi.com/auth/login");
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty(
                    "Content-Type",
                    "application/json; charset=UTF-8"
            );
            conn.setRequestProperty("Accept", "application/json");

            String body = "{"
                    + "\"username\":\"" + username + "\","
                    + "\"password\":\"" + password + "\""
                    + "}";

            // Escritura del body
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, StandardCharsets.UTF_8)
            );
            writer.write(body);
            writer.flush();
            writer.close();
            os.close();

            conn.connect();

            int code = conn.getResponseCode();
            Log.i(TAG, "HTTP CODE: " + code);

            if (code == HttpURLConnection.HTTP_OK ||
                    code == HttpURLConnection.HTTP_CREATED) {

                LoginResponse response = new Gson().fromJson(
                        new InputStreamReader(conn.getInputStream()),
                        LoginResponse.class
                );

                ((Activity) ctx).runOnUiThread(() ->
                        ((LoginActivity) ctx)
                                .onLoginSuccess(response.getToken())
                );

            } else {
                ((Activity) ctx).runOnUiThread(() ->
                        ((LoginActivity) ctx).onLoginError()
                );
            }

        } catch (Exception e) {
            Log.e(TAG, "EXCEPTION", e);
            ((Activity) ctx).runOnUiThread(() ->
                    ((LoginActivity) ctx).onLoginError()
            );
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
