package com.example.alva.servicioswebfirebase.controller;

import android.content.Context;

import com.example.alva.servicioswebfirebase.Utils.ResultListener;
import com.example.alva.servicioswebfirebase.Utils.Util;
import com.example.alva.servicioswebfirebase.dao.ObraDAO;
import com.example.alva.servicioswebfirebase.model.Obra;

import java.util.List;

public class ControllerObra {
    public void getObras(final ResultListener<List<Obra>> listerView, Context context){
        if (Util.isOnline(context)){
            ObraDAO obraDAO = new ObraDAO();
            obraDAO.getObras(new ResultListener<List<Obra>>() {
                @Override
                public void finish(List<Obra> resultado) {
                    listerView.finish(resultado);
                }
            });
        }
    }

}
