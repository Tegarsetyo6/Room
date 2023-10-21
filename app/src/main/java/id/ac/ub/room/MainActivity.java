package id.ac.ub.room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    Button bt1;
    Button bt2;
    EditText et1;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    private AppDatabase appDb;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appDb = AppDatabase.getInstance(this);
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        et1 = findViewById(R.id.et1);
        recyclerView = findViewById(R.id.recyclerView); // Inisialisasi Recycler View
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Atur jenis layout

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                Item item = new Item();
                item.setId(i);
                item.setJudul(et1.getText().toString());
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        appDb.itemDao().insertAll(item);
                    }
                });
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Item> list = appDb.itemDao().getAll();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (itemAdapter == null) {
                                    itemAdapter = new ItemAdapter(MainActivity.this, list);
                                    recyclerView.setAdapter(itemAdapter);
                                } else {
                                    itemAdapter.notifyDataSetChanged(); // Jika adapter sudah ada, gunakan ini untuk memperbarui data
                                }
                            }
                        });
                    }
                });
            }
        });

    }
}
