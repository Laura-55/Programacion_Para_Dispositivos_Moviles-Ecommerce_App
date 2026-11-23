package es.upm.etsiinf.gib.proyecto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.row_product, parent, false);
        }

        Product product = getItem(position);

        ImageView img = convertView.findViewById(R.id.imgProduct);
        TextView title = convertView.findViewById(R.id.tvTitle);
        TextView price = convertView.findViewById(R.id.tvPrice);
        TextView rating = convertView.findViewById(R.id.tvRating);

        title.setText(product.getTitle());
        price.setText("€ " + product.getPrice());

        if (product.getRating() != null) {
            rating.setText("⭐ " + product.getRating().getRate()
                    + " (" + product.getRating().getCount() + ")");
        }

        Glide.with(getContext())
                .load(product.getImage())
                .into(img);

        return convertView;
    }
}

