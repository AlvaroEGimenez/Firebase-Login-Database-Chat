package com.example.alva.servicioswebfirebase.view;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.alva.servicioswebfirebase.R;
import com.example.alva.servicioswebfirebase.model.Obra;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AdaptadorObras extends RecyclerView.Adapter {

    private List<Obra> obraList;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ObraclickListener obraclickListener;

    public AdaptadorObras(List<Obra> obraList, ObraclickListener obraclickListener) {
        this.obraList = obraList;
        this.obraclickListener = obraclickListener;
        this.firebaseStorage = FirebaseStorage.getInstance();
        this.storageReference = firebaseStorage.getReference();
    }


    public void setObraList(List<Obra> obraList) {
        this.obraList = obraList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.modelo_obra,viewGroup,false);
        ObraViewHolder obraViewHolder = new ObraViewHolder(view);
        return obraViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Obra obra = obraList.get(i);
        ObraViewHolder obraViewHolder = (ObraViewHolder) viewHolder;
        obraViewHolder.bind(obra);

    }

    @Override
    public int getItemCount() {
        return obraList.size();
    }

    public class ObraViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNombre;
        private ImageView imageView;

        public ObraViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.tvNombre);
            imageView = itemView.findViewById(R.id.ivCuadro);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    obraclickListener.onClick(obraList.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Obra obra){
            textViewNombre.setText(obra.getNombre());
            StorageReference child = storageReference.child(obra.getImagen());
            child.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.presentation_logo);
                    Glide.with(itemView.getContext()).applyDefaultRequestOptions(requestOptions).load(uri).into(imageView);
                }
            });

        }
    }

    public  interface ObraclickListener{
         void onClick(Obra obra);
    }
}
