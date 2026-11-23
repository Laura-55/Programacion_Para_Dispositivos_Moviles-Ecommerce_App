package es.upm.etsiinf.gib.proyecto;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchProductsThread implements Runnable {

    private static final String TAG = "FetchProductsThread";
    public static final String FILE_NAME = "products.json";

    private Context ctx;

    public FetchProductsThread(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        // Notificar inicio (UI thread)
        ((Activity) ctx).runOnUiThread(() ->
                ((MainActivity) ctx).startDownload()
        );

        try {
            URL url = new URL("https://fakestoreapi.com/products");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setDoInput(true);

            connection.connect();

            int code = connection.getResponseCode();
            Log.i(TAG, "HTTP CODE: " + code);

            if (code != HttpURLConnection.HTTP_OK) {
                throw new Exception("HTTP error: " + code);
            }

            reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );

            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            // Guardar JSON en archivo interno
            FileOutputStream fos =
                    ctx.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);

            fos.write(jsonBuilder.toString().getBytes());
            fos.close();

            Log.i(TAG, "JSON guardado en archivo interno");

            // Notificar fin OK
            ((Activity) ctx).runOnUiThread(() ->
                    ((MainActivity) ctx).finishDownloadFromFile()
            );

        } catch (Exception e) {
            Log.e(TAG, "Error descargando productos", e);

            ((Activity) ctx).runOnUiThread(() ->
                    ((MainActivity) ctx).finishDownloadError()
            );

        } finally {
            try {
                if (reader != null) reader.close();
            } catch (Exception ignored) {}

            if (connection != null) connection.disconnect();
        }
    }
}
