package com.example.alva.servicioswebfirebase.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alva.servicioswebfirebase.R;
import com.example.alva.servicioswebfirebase.model.Artista;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class DetalleActivity extends AppCompatActivity {

    public static final String KEY_CUADRO = "cuadro";
    public static final String KEY_ID = "id";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseStorage mStorage;
    private Artista artista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        mStorage = FirebaseStorage.getInstance();

        ImageView imageViewCuadro = findViewById(R.id.ivCuadroDetalle);
        final TextView textViewNombre = findViewById(R.id.tvNombreArtista);
        final TextView textViewNacionalidad = findViewById(R.id.tvNacionalidad);
        final TextView textViewInfluencia = findViewById(R.id.tvInfluenciado);


        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        final String id = bundle.getString(KEY_ID);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        mReference.child("artists").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot chilSnapshot : dataSnapshot.getChildren()){

                    artista = (chilSnapshot.getValue(Artista.class));

                    if (artista.getArtistId().equals(id)){
                        textViewNombre.setText(artista.getName());
                        textViewInfluencia.setText("Influenced_by: "+ artista.getInfluecia());
                        textViewNacionalidad.setText("Nacionalidad: "+artista.getNacionalidad());
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Glide.with(getApplicationContext()).load(bundle.getString(KEY_CUADRO)).into(imageViewCuadro);

    }
}
