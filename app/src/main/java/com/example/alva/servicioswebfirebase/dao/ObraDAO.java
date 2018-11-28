package com.example.alva.servicioswebfirebase.dao;

import android.util.Log;

import com.example.alva.servicioswebfirebase.DaoHelper;
import com.example.alva.servicioswebfirebase.Utils.ResultListener;
import com.example.alva.servicioswebfirebase.model.ContenedorObras;
import com.example.alva.servicioswebfirebase.model.Obra;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObraDAO extends DaoHelper {
    private ServiceObra serviceObra;


    public ObraDAO(){
        super("https://api.myjson.com/");
        serviceObra = retrofit.create(ServiceObra.class);
    }

    public void getObras(final ResultListener<List<Obra>> listenerDelController){
        Call<ContenedorObras> call = serviceObra.getObras();
        call.enqueue(new Callback<ContenedorObras>() {
            @Override
            public void onResponse(Call<ContenedorObras> call, Response<ContenedorObras> response) {
                ContenedorObras contenedorObras = response.body();
                List<Obra> obraList = contenedorObras.getObraList();
                listenerDelController.finish(obraList);
            }

            @Override
            public void onFailure(Call<ContenedorObras> call, Throwable t) {
                Log.e("ERROR",t.toString());
            }
        });
    }
}
