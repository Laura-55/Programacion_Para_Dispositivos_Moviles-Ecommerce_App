package es.upm.etsiinf.gib.proyecto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);

        // Botón logout
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> logout());

        // Botón historial de compras
        Button btnPurchases = findViewById(R.id.btnPurchases);
        btnPurchases.setOnClickListener(v ->
                startActivity(new Intent(this, PurchaseHistoryActivity.class))
        );

        // ListView productos
        listView = findViewById(R.id.listProducts);

        // Cargar productos
        loadProducts();
    }


     // Lanza el hilo en segundo plano para descargar productos
    private void loadProducts() {
        new Thread(new FetchProductsThread(this)).start();
    }

    //Llamado desde FetchProductsThread (UI thread)
    public void startDownload() {
        Toast.makeText(this,
                "Descargando productos...",
                Toast.LENGTH_SHORT).show();
    }

    //Llamado desde FetchProductsThread cuando la descarga finaliza correctamente Lee products.json y actualiza la lista
    public void finishDownloadFromFile() {
        loadProductsFromFile();
    }

    // Llamado desde FetchProductsThread si ocurre un error
    public void finishDownloadError() {
        Toast.makeText(this,
                "Error descargando productos",
                Toast.LENGTH_SHORT).show();
    }

    // Lee el JSON almacenado localmente y muestra la lista
    private void loadProductsFromFile() {

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            openFileInput(FetchProductsThread.FILE_NAME)
                    )
            );

            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();

            String json = jsonBuilder.toString();

            Type type = new TypeToken<List<Product>>() {}.getType();
            List<Product> products =
                    new Gson().fromJson(json, type);

            ProductAdapter adapter =
                    new ProductAdapter(this, products);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener((parent, view, position, id) -> {
                Product selected = products.get(position);

                Intent intent = new Intent(
                        MainActivity.this,
                        ProductDetailActivity.class
                );
                intent.putExtra("product", selected);
                startActivity(intent);
            });

        } catch (Exception e) {
            Toast.makeText(this,
                    "Error leyendo productos",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Cierra sesión y vuelve al LoginActivity
    private void logout() {
        prefs.edit().clear().apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );
        startActivity(intent);
        finish();
    }
}
