package com.example.lilo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText edtsodienthoai,edtpassword;
    Button btndangnhap,btndangky, btnxacnhan;
    String sdt, mk ;
    ListView listViewfood;
    String mTitlefood[]={"1","2","3"};
    String mSubtitlefood[]={"a","b","c"};
    int imagesfood[]={R.drawable.login,R.drawable.login,R.drawable.login};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        ControlButton();
        listViewfood=findViewById(R.id.listViewfood);
        MyAdapter adapter = new MyAdapter(this, mTitlefood,mSubtitlefood,imagesfood);
        ///listViewfood.setAdapter(adapter);
    }
    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        String rTitlefood[];
        String rSubtitlefood[];
        int rImgsfood[];

        MyAdapter(Context c, String titlefood[],String subtitlefood[],int imgsfood[]){
            super(c,R.layout.row,R.id.textView1food,titlefood);
            this.context=c;
            this.rTitlefood=titlefood;
            this.rSubtitlefood=subtitlefood;
            this.rImgsfood=imgsfood;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater= (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row,parent,false);
            ImageView imagesfood = row.findViewById(R.id.imagefood);
            TextView myTitlefood = row.findViewById(R.id.textView1food);
            TextView mySubtitlefood = row.findViewById(R.id.textView2food);

            imagesfood.setImageResource(rImgsfood[position]);
            myTitlefood.setText(rTitlefood[position]);
            mySubtitlefood.setText(rSubtitlefood[position]);
            return row;

        }
    }

    private void ControlButton() {
        btndangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setTitle("Đăng ký");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dangky);
                final EditText edtsdt = (EditText)dialog.findViewById(R.id.edtsdt);
                final EditText edtpass = (EditText)dialog.findViewById(R.id.edtpass);
                Button btnxacnhan = (Button)dialog.findViewById(R.id.buttonxacnhan);
                btnxacnhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        sdt =  edtsdt.getText().toString().trim();
                        mk = edtpass.getText().toString().trim();
                        edtsodienthoai.setText(sdt);
                        edtpassword.setText(mk);
                        dialog.cancel();
                    }
                });
                dialog.show();

            }
        });

        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) //nhap dieu kien dang nhap thanh cong
                     {
                         Toast.makeText(MainActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(MainActivity.this,menu.class);
                         startActivity(intent);
                     }
            }
        });
    }

    private void Anhxa()
    {
        edtsodienthoai = (EditText)findViewById(R.id.editsodienthoai);
        edtpassword =(EditText)findViewById(R.id.editpassword);
        btndangnhap = (Button)findViewById(R.id.buttondangnhap);
        btndangky = (Button)findViewById(R.id.buttondangky);


    }
}
