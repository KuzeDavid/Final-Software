package Boton_de_panico;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.CallbackManager;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.botndepnico.R;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class ConexionFacebook extends AppCompatActivity {

    CallbackManager cbm;
    LoginButton loginButton;
    TextView tvperfil, m1, m2, m3, m4;
    ProfilePictureView foto;
    FirebaseFirestore bd;
    ShareButton comp;
    Button btnVolver;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location location = null;
    Double lat;
    Double lon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_facebook);

        mostrarDatos();

        Bundle bundle = this.getIntent().getExtras();
        String ID = bundle.getString("ID");

        volver(ID);

        loginButton = findViewById(R.id.login_button);
        tvperfil = findViewById(R.id.tvperfil);
        foto  = findViewById(R.id.friendProfilePicture);

        cbm = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("user_gender, user_friends"));

        loginButton.registerCallback(cbm,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Inicio", "Exitoso");
                    }

                    @Override
                    public void onCancel() {
                        Log.d("Inicio", "No procesado");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("Inicio", "Errado");
                    }
                });

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();


    }

    private void volver(String ID) {

        btnVolver = findViewById(R.id.Volver);

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagPrincipal(ID);
            }
        });

    }

    private void mostrarDatos() {

       Bundle bundle = this.getIntent().getExtras();
       String ID = bundle.getString("ID");

       bd = FirebaseFirestore.getInstance();

        bd.collection("usuarios").document(ID)
                .collection("facebook").document("1OoeZagYTRirfyElmjnv")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()){
                    String Nombre = documentSnapshot.getString("Nombre");
                    String IDFace = documentSnapshot.getString("ID_Facebook");

                    tvperfil.setText(Nombre);

                }
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        cbm.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = this.getIntent().getExtras();
        String ID = bundle.getString("ID");

        bd = FirebaseFirestore.getInstance();

        GraphRequest gp = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("Datos", object.toString());
                        try {
                            String name = object.getString("name");
                            String id = object.getString("id");
                            tvperfil.setText(name);
                            foto.setProfileId(id);
                            RegistroBD(name,id,foto);

                        } catch (JSONException e) {

                        }
                    }
                });

        Bundle b= new Bundle();
        b.putString("fields", "gender , name, id, first_name,last_name");

        gp.setParameters(b);
        gp.executeAsync();

        String IDbtn1 = bd.collection("usuarios")
                .document(ID.toString()).collection("botones")
                .document("jRHaBP85jYEEPZjHhlEP").getId();

        envíoredsocial(ID,IDbtn1);

    }

    AccessTokenTracker att = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken == null){
                LoginManager.getInstance().logOut();
            }
        }
    };


    void RegistroBD(String nombre, String IDFace, ProfilePictureView foto){

        Bundle bundle = this.getIntent().getExtras();
        String ID = bundle.getString("ID");

        bd = FirebaseFirestore.getInstance();

        String IDFacebook =  bd.collection("usuarios").document(ID)
                .collection("facebook").document("1OoeZagYTRirfyElmjnv")
                .getId();

        Map<String, String> map = new HashMap<>();

        map.put("Nombre",  nombre);
        map.put("ID_Facebook", IDFace);

        bd.collection("usuarios").document(ID)
                .collection("facebook").document(IDFacebook)
                .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {

                Toast toastCont =
                        Toast.makeText(getApplicationContext(),
                                "Red social Añadida", LENGTH_SHORT);

                toastCont.show();

            }
        });
    }

    void PagPrincipal(String ID){

        Intent i = new Intent(this, Pag_Principal.class);
        i.putExtra("ID", ID);
        startActivity(i);
    }

    void envíoredsocial(String ID, String IDbtn){

        bd.collection("usuarios").document(ID)
                .collection("botones").document(IDbtn)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()){

                    String msj = documentSnapshot.getString("Mensaje");

                    Toast toastCont =
                            Toast.makeText(getApplicationContext(),
                                    msj, LENGTH_SHORT);

                    toastCont.show();

                    ShareLinkContent slc = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("https://www.google.com/maps/search/@4.6447329,-76.2123141,7z"))
                            .setShareHashtag(new ShareHashtag.Builder()
                                    .setHashtag("#"+ msj).build())
                            .build();

                    comp = findViewById(R.id.comp);
                    comp.setShareContent(slc);

                }
            }
        });


    }



}