package es.upm.etsiinf.gib.proyecto;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;

public class ProductDetailActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "purchase_channel";
    private static final int NOTIFICATION_PERMISSION_CODE = 1001;

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Obtener producto
        product = (Product) getIntent().getSerializableExtra("product");

        // Referencias UI
        ImageView img = findViewById(R.id.imgDetail);
        TextView title = findViewById(R.id.tvTitleDetail);
        TextView price = findViewById(R.id.tvPriceDetail);
        TextView category = findViewById(R.id.tvCategoryDetail);
        TextView rating = findViewById(R.id.tvRatingDetail);
        TextView description = findViewById(R.id.tvDescriptionDetail);
        Button btnBuy = findViewById(R.id.btnBuy);

        Button btnShare = findViewById(R.id.btnShare);

        btnShare.setOnClickListener(v ->
                shareProductWithImage(product)
        );

        // Rellenar datos
        title.setText(product.getTitle());
        price.setText("€ " + product.getPrice());
        category.setText("Categoría: " + product.getCategory());

        if (product.getRating() != null) {
            rating.setText("⭐ " + product.getRating().getRate()
                    + " (" + product.getRating().getCount() + " valoraciones)");
        }

        description.setText(product.getDescription());

        Glide.with(this)
                .load(product.getImage())
                .into(img);

        // Pedir permiso de notificaciones
        requestNotificationPermissionIfNeeded();

        // Botón Comprar
        btnBuy.setOnClickListener(v -> {
            PurchaseDatabaseHelper db = new PurchaseDatabaseHelper(this);
            db.addPurchase(product);
            if (canShowNotifications()) {
                showPurchaseNotification(product.getTitle());
            } else {
                Toast.makeText(this,
                        "Permiso de notificaciones no concedido",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Crear y mostrar notificación
    private void showPurchaseNotification(String productTitle) {

        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Canal obligatorio Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Compras",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notificaciones de compra");
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Compra realizada")
                        .setContentText("Has comprado \"" + productTitle + "\" correctamente")
                        .setAutoCancel(true);

        manager.notify(1, builder.build());
    }

    // Comprobar si se pueden mostrar notificaciones
    private boolean canShowNotifications() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED;
    }

    // Pedir permiso si es necesario
    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!canShowNotifications()) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE
                );
            }
        }
    }

    // Respuesta del usuario al permiso
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this,
                        "Permiso de notificaciones concedido",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "Las notificaciones están desactivadas",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void shareProductWithImage(Product product) {

        String shareText =
                "🛍️ Mira este producto:\n\n" +
                        product.getTitle() + "\n" +
                        "Precio: €" + product.getPrice() + "\n" +
                        "Categoría: " + product.getCategory() + "\n\n" +
                        product.getDescription();

        Glide.with(this)
                .asBitmap()
                .load(product.getImage())
                .into(new com.bumptech.glide.request.target.CustomTarget<android.graphics.Bitmap>() {

                    @Override
                    public void onResourceReady(android.graphics.Bitmap resource,
                                                com.bumptech.glide.request.transition.Transition<? super android.graphics.Bitmap> transition) {

                        try {
                            // Crear archivo temporal
                            File cachePath = new File(getCacheDir(), "images");
                            cachePath.mkdirs();

                            File file = new File(cachePath, "product.png");
                            FileOutputStream stream = new FileOutputStream(file);
                            resource.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream);
                            stream.close();

                            Uri imageUri = FileProvider.getUriForFile(
                                    ProductDetailActivity.this,
                                    getPackageName() + ".provider",
                                    file
                            );

                            // Intent para compartir
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("image/*");
                            intent.putExtra(Intent.EXTRA_TEXT, shareText);
                            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                            startActivity(Intent.createChooser(intent, "Compartir producto"));

                        } catch (Exception e) {
                            Toast.makeText(ProductDetailActivity.this,
                                    "Error al compartir imagen",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onLoadCleared(android.graphics.drawable.Drawable placeholder) {}
                });
    }

}
