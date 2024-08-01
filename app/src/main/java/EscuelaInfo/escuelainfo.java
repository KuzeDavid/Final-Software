package EscuelaInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.proyecto.botndepnico.R;

public class escuelainfo extends AppCompatActivity {

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String TEXT = "text";

    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escuelainfo);

        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        loadData();
        WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setJavaScriptEnabled(true); // Habilitar JavaScript si es necesario
        // Habilitar compresión y caché
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // Cargar la URL en el WebView
        webView.loadUrl("https://siged.sep.gob.mx/SIGED/escuelas.html");
    }
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, editText.getText().toString());
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(TEXT, "");

        editText.setText(text);
    }
}
