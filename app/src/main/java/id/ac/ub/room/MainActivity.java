package id.ac.ub.room;

import androidx.appcompat.app.AppCompatActivity;

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
    TextView tv1;
    private AppDatabase appDb;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appDb = AppDatabase.getInstance(getApplicationContext());
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        tv1 = findViewById(R.id.tv1);
        et1 = findViewById(R.id.et1);
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
                        String s = "";
                        for (Item item : list) {
                            s = s + item.getJudul() + '\n';
                        }
                        tv1.setText(s);
                    }
                });
            }
        });
    }
}