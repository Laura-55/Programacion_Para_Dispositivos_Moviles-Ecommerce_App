package es.upm.etsiinf.gib.proyecto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PurchaseAdapter extends ArrayAdapter<PurchaseItem> {

    public PurchaseAdapter(Context context, List<PurchaseItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.row_purchase, parent, false);
        }

        PurchaseItem item = getItem(position);

        ((TextView) convertView.findViewById(R.id.tvTitle))
                .setText(item.getTitle());
        ((TextView) convertView.findViewById(R.id.tvPrice))
                .setText("€ " + item.getPrice());
        ((TextView) convertView.findViewById(R.id.tvQuantity))
                .setText("Cantidad: " + item.getQuantity());

        ImageView img = convertView.findViewById(R.id.imgProduct);
        Glide.with(getContext()).load(item.getImage()).into(img);

        return convertView;
    }
}
