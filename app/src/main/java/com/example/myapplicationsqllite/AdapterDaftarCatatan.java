package com.example.myapplicationsqllite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class AdapterDaftarCatatan extends BaseAdapter {

    private List<SetterGetterData> sgd;
    private Context context;
    private int id;
    private AlertDialog daftarCatatan;

    public AdapterDaftarCatatan(List<SetterGetterData> sgd, Context context,
                                AlertDialog daftarCatatan) {
        this.sgd = sgd;
        this.context = context;
        this.daftarCatatan = daftarCatatan;
    }

    @Override
    public int getCount() {
        return sgd.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.item_catatan, null);

        TextView itemJudul = view.findViewById(R.id.itemJudul);
        ImageView editCatatan = view.findViewById(R.id.editCatatan);
        ImageView hapusCatatan = view.findViewById(R.id.hapusCatatan);

        final SetterGetterData setterGetterData = sgd.get(position);
        itemJudul.setText(setterGetterData.getJudul());

        //Metode Klik Untuk Melihat Catatan
        itemJudul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MainActivity.class);
                i.putExtra("judul", setterGetterData.getJudul());
                i.putExtra("catatan", setterGetterData.getCatatan());
                i.putExtra("lihat", true);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                daftarCatatan.dismiss();
            }
        });

        //Metode Klik Untuk Menyunting Catatan, Dengan Mengirimkan Intent Ke Aktivitas Yang Sama
        //Setelah Aktivitas Di Refresh, Mode Edit Catatan Aktif.
        editCatatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = setterGetterData.getId();
                Intent i = new Intent(context, MainActivity.class);
                i.putExtra("id", id);
                i.putExtra("judul", setterGetterData.getJudul());
                i.putExtra("catatan", setterGetterData.getCatatan());
                i.putExtra("edit", true);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                daftarCatatan.dismiss();
            }
        });

        //Menghapus Catatan Satu-Persatu
        hapusCatatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = setterGetterData.getId();
                DBHelper dbHelper = new DBHelper(context);
                dbHelper.hapusCatatan(id);
                notifyDataSetChanged();
                dbHelper.close();
                Toast.makeText(context, "Catatan dihapus",
                        Toast.LENGTH_SHORT).show();
                daftarCatatan.dismiss();
            }
        });

        return view;
    }
}
