package com.example.myapplicationsqllite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //Variabel Global
    private TextView simpanCatatan;
    private EditText judulCatatan;
    private EditText isiCatatan;
    private Button batal;
    private boolean edit = false;
    private int id = 0;
    private RelativeLayout layoutCatatan;
    private LinearLayout layoutUtama;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View Id Dari Seluruh Komponen Pada Layout
        judulCatatan = findViewById(R.id.judulCatatan);
        isiCatatan = findViewById(R.id.isiCatatan);
        simpanCatatan = findViewById(R.id.simpanCatatan);
        batal = findViewById(R.id.batal);
        layoutCatatan = findViewById(R.id.layoutCatatan);
        layoutUtama = findViewById(R.id.layoutUtama);

        //Local Variabel Dan View Id
        TextView buatCatatanBaru = findViewById(R.id.buatCatatanBaru);
        TextView daftarCatatan = findViewById(R.id.daftarCatatan);

        //Menerima Intent Yang Dikirimkan Oleh Tombol Edit Catatan.
        //Jika Nilai Boolean Edit Adalah True Maka Mode Sunting Catatan Aktif.
        Intent i = getIntent();
        edit = i.getBooleanExtra("edit", false);
        if (edit) {
            layoutUtama.setVisibility(View.GONE);
            layoutCatatan.setVisibility(View.VISIBLE);
        }
        //Mode Untuk Melihat Catatan
        //Aktif Saat Judul Catatan Pada Daftar Catatan Di Klik.
        boolean lihat = i.getBooleanExtra("lihat", false);
        if (lihat) {
            simpanCatatan.setVisibility(View.INVISIBLE);
            batal.setText("Kembali");
            layoutUtama.setVisibility(View.GONE);
            layoutCatatan.setVisibility(View.VISIBLE);
        }
        judulCatatan.setText(i.getStringExtra("judul"));
        isiCatatan.setText(i.getStringExtra("catatan"));
        id = i.getIntExtra("id", 0);

        //Metode Klik Untuk Tombol Membuat Catatan Baru
        buatCatatanBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanCatatan.setVisibility(View.VISIBLE);
                batal.setVisibility(View.VISIBLE);
                batal.setText("Batal");
                layoutUtama.setVisibility(View.GONE);
                layoutCatatan.setVisibility(View.VISIBLE);
            }
        });

        //Metode Klik Untuk Tombol Menyimpan Catatan
        simpanCatatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (judulCatatan.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Judul tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isiCatatan.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Konten catatan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                DBHelper dbHelper = new DBHelper(getApplicationContext());
                SetterGetterData sgd = new SetterGetterData();
                sgd.setJudul(judulCatatan.getText().toString());
                sgd.setCatatan(isiCatatan.getText().toString());

                boolean masukkanCatatan;
                if (edit) {
                    masukkanCatatan = dbHelper.perbaharuiCatatan(sgd, id);
                } else {
                    masukkanCatatan = dbHelper.masukkanCatatan(sgd);
                }
                if (masukkanCatatan) {
                    Toast.makeText(getApplicationContext(), "Catatan berhasil disimpan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Kesalahan terjadi!",
                            Toast.LENGTH_SHORT).show();
                }
                dbHelper.close();
                judulCatatan.getText().clear();
                isiCatatan.getText().clear();
                layoutUtama.setVisibility(View.VISIBLE);
                layoutCatatan.setVisibility(View.GONE);
            }
        });

        //Metode Klik Untuk Tombol Melihat Daftar Catatan
        daftarCatatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDaftarCatatan();
            }
        });

        //Metode Klik Untuk Tombol Membatalkan Pembuatan Atau Penyuntingan Catatan
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judulCatatan.getText().clear();
                isiCatatan.getText().clear();
                layoutUtama.setVisibility(View.VISIBLE);
                layoutCatatan.setVisibility(View.GONE);
                edit = false;
            }
        });
    }

    //Dialog Untuk Menampilkan Daftar Catatan
    @SuppressLint("SetTextI18n")
    private void dialogDaftarCatatan () {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.layout_daftar_catatan, null);
        TextView hapusSemuaCatatan = v.findViewById(R.id.hapusSemuaCatatan);
        TextView judulDaftar = v.findViewById(R.id.judulDaftar);
        b.setView(v);

        ArrayList<SetterGetterData> setterGetterData = new ArrayList<>();
        ListView listDaftar = v.findViewById(R.id.listDaftar);
        final DBHelper dh = new DBHelper(getApplicationContext());
        Cursor cursor = dh.dapatkanSemuaCatatan();
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            while (!cursor.isAfterLast()) {
                SetterGetterData sgd = new SetterGetterData();
                sgd.setId((cursor.getInt(cursor.getColumnIndexOrThrow("id"))));

                sgd.setJudul((cursor.getString(cursor.getColumnIndexOrThrow("judul"))));

                sgd.setCatatan((cursor.getString(cursor.getColumnIndexOrThrow("catatan"))));
                setterGetterData.add(sgd);
                cursor.moveToNext();
            }
            dh.close();
        }
        final AlertDialog daftarCatatan = b.create();if (daftarCatatan.getWindow()
                !=null) {
            daftarCatatan.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        AdapterDaftarCatatan adapterDaftarCatatan = new
                AdapterDaftarCatatan(setterGetterData, getApplicationContext(), daftarCatatan);
        listDaftar.setAdapter(adapterDaftarCatatan);

        if (listDaftar.getAdapter().getCount() < 2) {
            hapusSemuaCatatan.setVisibility(View.INVISIBLE);
        } else {
            hapusSemuaCatatan.setVisibility(View.VISIBLE);
        }

        if (listDaftar.getAdapter().getCount() < 1) {
            judulDaftar.setText("Kosong");
        } else {
            judulDaftar.setText("Daftar Catatan");
        }

        //Menghapus Seluruh Catatan
        hapusSemuaCatatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dh.hapusSemuaCatatan();
                dh.close();
                daftarCatatan.dismiss();
                Toast.makeText(getApplicationContext(), "Seluruh catatan dihapus",
                        Toast.LENGTH_SHORT).show();
            }
        });

        daftarCatatan.show();
    }

    public void onBackPressed () {
        if (layoutCatatan.getVisibility() == View.VISIBLE) {
            layoutCatatan.setVisibility(View.GONE);
            layoutUtama.setVisibility(View.VISIBLE);
        } else {
            finishAffinity();
        }
    }
}
