package com.example.lilo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {
    EditText edtsodienthoai, edtpassword;
    Button btndangnhap, btndangky, btnxacnhan;
    String sdt, mk;
    ListView listViewfood;
    String mTitlefood[] = {"1", "2", "3"};
    String mSubtitlefood[] = {"a", "b", "c"};
    int imagesfood[] = {R.drawable.login, R.drawable.login, R.drawable.login};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        ControlButton();
        listViewfood = findViewById(R.id.listViewfood);
        MyAdapter adapter = new MyAdapter(this, mTitlefood, mSubtitlefood, imagesfood);
        //listViewfood.setAdapter(adapter);
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rTitlefood[];
        String rSubtitlefood[];
        int rImgsfood[];

        MyAdapter(Context c, String titlefood[], String subtitlefood[], int imgsfood[]) {
            super(c, R.layout.row, R.id.textView1food, titlefood);
            this.context = c;
            this.rTitlefood = titlefood;
            this.rSubtitlefood = subtitlefood;
            this.rImgsfood = imgsfood;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView imagesfood = row.findViewById(R.id.imagefood);
            TextView myTitlefood = row.findViewById(R.id.textView1food);
            TextView mySubtitlefood = row.findViewById(R.id.textView2food);

            imagesfood.setImageResource(rImgsfood[position]);
            myTitlefood.setText(rTitlefood[position]);
            mySubtitlefood.setText(rSubtitlefood[position]);
            return row;

        }
    }

    public static byte[] LongToLittleEndian(long numero) {
        byte[] b = new byte[8];
        b[0] = (byte) (numero & 0xFF);
        b[1] = (byte) ((numero >> 8) & 0xFF);
        b[2] = (byte) ((numero >> 16) & 0xFF);
        b[3] = (byte) ((numero >> 24) & 0xFF);
        b[4] = (byte) ((numero >> 32) & 0xFF);
        b[5] = (byte) ((numero >> 40) & 0xFF);
        b[6] = (byte) ((numero >> 48) & 0xFF);
        b[7] = (byte) ((numero >> 56) & 0xFF);

        return b;
    }

    public static long convertToLong(byte[] array) {
        ByteBuffer buffer = ByteBuffer.wrap(array);
        return buffer.getLong();
    }

    public static byte[] DoubletoByteArray(double d) {
        long l = Double.doubleToRawLongBits(d);
        return new byte[]{
                (byte) ((l >> 0) & 0xff),
                (byte) ((l >> 8) & 0xff),
                (byte) ((l >> 16) & 0xff),
                (byte) ((l >> 24) & 0xff),
                (byte) ((l >> 32) & 0xff),
                (byte) ((l >> 40) & 0xff),
                (byte) ((l >> 48) & 0xff),
                (byte) ((l >> 56) & 0xff),
        };
    }

    private void ControlButton() {
        btndangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setTitle("Đăng ký");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dangky);
                final EditText edtsdt = (EditText) dialog.findViewById(R.id.edtsdt);
                final EditText edtpass = (EditText) dialog.findViewById(R.id.edtpass);
                Button btnxacnhan = (Button) dialog.findViewById(R.id.buttonxacnhan);
                btnxacnhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        sdt = edtsdt.getText().toString().trim();
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
                BackgroundTask b = new BackgroundTask();
                String op = "2";
                b.execute(op, edtsodienthoai.toString(), edtpassword.toString());
            }
        });
    }

    private void Anhxa() {
        edtsodienthoai = (EditText) findViewById(R.id.editsodienthoai);
        edtpassword = (EditText) findViewById(R.id.editpassword);
        btndangnhap = (Button) findViewById(R.id.buttondangnhap);
        btndangky = (Button) findViewById(R.id.buttondangky);


    }

    class BackgroundTask extends AsyncTask<String, Void, String> {
        Socket s;
        DataOutputStream dos;
        DataInputStream dis;

        @Override
        protected String doInBackground(String... params) {
            int op = Integer.parseInt(params[0]);
            try {
                s = new Socket("172.16.255.129", 6666);
                dos = new DataOutputStream(s.getOutputStream());
                dis = new DataInputStream(s.getInputStream());

                byte b = (byte) (op & 0xff);

                byte[] endString = new byte[1];
                endString[0] = 0x00;

                switch (op) {
                    case 1:

                        byte[] username1 = params[1].getBytes();
                        byte[] pass1 = params[2].getBytes();
                        long length1 = username1.length + pass1.length;
                        byte[] bytearlength1 = LongToLittleEndian(length1);

                        dos.write(b);
                        dos.write(bytearlength1);
                        dos.write(username1);
                        dos.write(endString);
                        dos.write(pass1, 0, 64);

                        dos.flush();

                        byte[] result = new byte[1];
                        dis.read(result);
                        int status1 = ByteBuffer.wrap(result).getInt();

                        if (status1 == 1) //nhap dieu kien dang nhap thanh cong
                        {
                            Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, menu.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }

                        dos.close();
                        dis.close();

                        s.close();

                        break;
                    case 2:


                        byte[] username2 = params[1].getBytes();
                        byte[] pass2 = params[2].getBytes();

                        long length2 = username2.length + pass2.length;
                        byte[] bytearlength2 = LongToLittleEndian(length2);

                        dos.write(b);
                        dos.write(bytearlength2);
                        dos.write(username2);
                        dos.write(endString);
                        dos.write(pass2, 0, 64);

                        dos.flush();

                        byte[] result2 = new byte[1];
                        dis.read(result2);


                        dos.close();
                        dis.close();

                        s.close();

                        break;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}

