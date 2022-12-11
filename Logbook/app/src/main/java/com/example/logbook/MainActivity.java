package com.example.logbook;

import android.os.Bundle;
import android.util.Patterns;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

  String currentUrl;
  int count;
  int currentNum;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    EditText inputUrl = findViewById(R.id.inputUrl);
    Button btnAdd = findViewById(R.id.btnAdd);
    Button btnPrev = findViewById(R.id.btnPrev);
    Button btnNext = findViewById(R.id.btnNext);
    TextView totalPicture = findViewById(R.id.indexView);
    ImageView imgView = findViewById(R.id.imageView);
    DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
    if (databaseHelper.getSize() > 0 && currentUrl == null) {
      currentUrl = databaseHelper.getImageById("1");
      count = databaseHelper.getSize();
      currentNum = 1;
      totalPicture.setText(currentNum + " / " + count);
      Picasso.with(this).load("" + currentUrl).into(imgView);
    }

    btnAdd.setOnClickListener(view -> {
      String url = inputUrl.getText().toString().trim();
      if (!IsValidUrl(url) ||url.length()==0) {
        inputUrl.requestFocus();
        inputUrl.setError("Incorrect URL link");
      } else {
        currentUrl = url;
        Picasso.with(this).load(currentUrl).into(imgView);
        databaseHelper.insertImage(this.currentUrl);
        Toast
          .makeText(
            getApplicationContext(),
            "Picture added!",
            Toast.LENGTH_SHORT
          )
          .show();
        count += 1;
        currentNum = count;
        totalPicture.setText(currentNum + " / " + count);
        inputUrl.setText("");
      }
    });

    btnPrev.setOnClickListener(view -> {
      if (count == 0) {
        Toast
          .makeText(
            getApplicationContext(),
            "Nothing to show!",
            Toast.LENGTH_SHORT
          )
          .show();
        return;
      }
      int id = databaseHelper.getIdImage(currentUrl);
      currentNum = id;

      if (id == 1) {
        currentNum = id;

        Picasso.with(this).load(currentUrl).into(imgView);
        Toast
          .makeText(getApplicationContext(), "Min", Toast.LENGTH_SHORT)
          .show();
        totalPicture.setText(currentNum + " / " + count);
      } else {
        currentNum = id - 1;
        currentUrl = databaseHelper.getImageById(String.valueOf(id - 1));
        Picasso.with(this).load(currentUrl).into(imgView);
        totalPicture.setText(currentNum + " / " + count);
      }
    });

    btnNext.setOnClickListener(view -> {
      if (count == 0) {
        Toast
          .makeText(
            getApplicationContext(),
            "Nothing to show!",
            Toast.LENGTH_SHORT
          )
          .show();
        return;
      }
      int id = databaseHelper.getIdImage(this.currentUrl);
      if (id == count) {
        currentNum = id;
        Picasso.with(this).load(currentUrl).into(imgView);
        Toast
          .makeText(getApplicationContext(), "Max", Toast.LENGTH_SHORT)
          .show();
        totalPicture.setText(currentNum + " / " + count);
      } else {
        currentNum = id + 1;
        currentUrl = databaseHelper.getImageById(String.valueOf(id + 1));
        Picasso.with(this).load(currentUrl).into(imgView);
        totalPicture.setText(currentNum + " / " + count);
      }
    });
  }

  public static boolean IsValidUrl(String urlString) {
    try {
      URL url = new URL(urlString);
      return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches();
    } catch (MalformedURLException ignored) {
    }
    return false;
  }
}
