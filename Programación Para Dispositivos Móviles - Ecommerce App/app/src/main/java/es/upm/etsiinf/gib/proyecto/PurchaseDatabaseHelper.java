package es.upm.etsiinf.gib.proyecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class PurchaseDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "purchases.db";
    private static final int DB_VERSION = 1;

    public PurchaseDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE purchases (" +
                        "title TEXT PRIMARY KEY," +
                        "price REAL," +
                        "description TEXT," +
                        "category TEXT," +
                        "image TEXT," +
                        "quantity INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS purchases");
        onCreate(db);
    }

    // Añadir o incrementar compra
    public void addPurchase(Product product) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery(
                "SELECT quantity FROM purchases WHERE title = ?",
                new String[]{product.getTitle()}
        );

        if (c.moveToFirst()) {
            int quantity = c.getInt(0) + 1;
            ContentValues values = new ContentValues();
            values.put("quantity", quantity);
            db.update("purchases", values, "title = ?", new String[]{product.getTitle()});
        } else {
            ContentValues values = new ContentValues();
            values.put("title", product.getTitle());
            values.put("price", product.getPrice());
            values.put("description", product.getDescription());
            values.put("category", product.getCategory());
            values.put("image", product.getImage());
            values.put("quantity", 1);
            db.insert("purchases", null, values);
        }

        c.close();
        db.close();
    }

    // Obtener registro de compras
    public List<PurchaseItem> getAllPurchases() {
        List<PurchaseItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM purchases", null);
        while (c.moveToNext()) {
            list.add(new PurchaseItem(
                    c.getString(0),
                    c.getDouble(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getInt(5)
            ));
        }

        c.close();
        db.close();
        return list;
    }
}

