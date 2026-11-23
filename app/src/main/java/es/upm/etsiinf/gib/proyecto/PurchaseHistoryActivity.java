package es.upm.etsiinf.gib.proyecto;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class PurchaseHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

        ListView listView = findViewById(R.id.listPurchases);

        PurchaseDatabaseHelper db = new PurchaseDatabaseHelper(this);
        List<PurchaseItem> purchases = db.getAllPurchases();

        PurchaseAdapter adapter = new PurchaseAdapter(this, purchases);
        listView.setAdapter(adapter);
    }
}

