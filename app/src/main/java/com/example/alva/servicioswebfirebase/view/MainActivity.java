package com.example.alva.servicioswebfirebase.view;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alva.servicioswebfirebase.R;
import com.example.alva.servicioswebfirebase.Utils.ResultListener;
import com.example.alva.servicioswebfirebase.controller.ControllerObra;
import com.example.alva.servicioswebfirebase.model.Obra;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdaptadorObras.ObraclickListener {
    public static final Integer CAMERA_REQUEST_CODE = 1;

    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        frameLayout = findViewById(R.id.frameLayoutProgress);
        ProgressBar progressBar = findViewById(R.id.progressbar);

        progressBar.setProgress(0);
        progressBar.setMax(100);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(user.getDisplayName());


        final RecyclerView recyclerView = findViewById(R.id.rvObras);
        GridLayoutManager gridLayout = new GridLayoutManager(MainActivity.this, 1);
        gridLayout.setOrientation(GridLayout.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayout);

        final AdaptadorObras adaptadorObras = new AdaptadorObras(new ArrayList<Obra>(), this);

        ControllerObra controllerObra = new ControllerObra();
        controllerObra.getObras(new ResultListener<List<Obra>>() {
            @Override
            public void finish(List<Obra> resultado) {
                adaptadorObras.setObraList(resultado);
            }
        }, getApplicationContext());

        recyclerView.setAdapter(adaptadorObras);

        CoordinatorLayout linearLayout = findViewById(R.id.coordinator);

        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();

        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(2500);

        animationDrawable.start();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_obras, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chat:
                Intent intentChat = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intentChat);
                break;

            case R.id.camara:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
                break;

            case R.id.cerrar_sesion:
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                Intent intentLogOut = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentLogOut);
                break;
            default:
                break;
        }

        return true;
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            mAuth.signOut();
            LoginManager.getInstance().logOut();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Presione nuevamente cerrar Sesion", Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] dataBAOS = baos.toByteArray();

            StorageReference filepath = FirebaseStorage.getInstance().getReference();
            StorageReference imagesRef = filepath.child("filename" + new Date().getTime());


            UploadTask uploadTask = imagesRef.putBytes(dataBAOS);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), "Error subiendo imagen", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(MainActivity.this, "Imagen guardada", Toast.LENGTH_SHORT).show();
                    frameLayout.setVisibility(View.INVISIBLE);

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MainActivity.this, "Subiendo imagen...", Toast.LENGTH_SHORT).show();
                    frameLayout.setVisibility(View.VISIBLE);
                }
            });
        }

    }

    @Override
    public void onClick(final Obra obra) {
        final Intent intent = new Intent(MainActivity.this, DetalleActivity.class);
        final Bundle bundle = new Bundle();

        StorageReference child = storageReference.child(obra.getImagen());
        child.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                bundle.putString(DetalleActivity.KEY_CUADRO, String.valueOf(uri));
                bundle.putString(DetalleActivity.KEY_ID,obra.getId().toString());
                intent.putExtras(bundle);
                startActivity(intent);
                //overridePendingTransition(R.transition.main_reenter, R.transition.main_exit);

            }
        });

    }

}

