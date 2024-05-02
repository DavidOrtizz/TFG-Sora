package com.example.sora;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://192.168.1.69/signup";
    private Map<String,String> params;
    public RegisterRequest(String NombreCuenta, String NombreUsuario, String Correo, String Contrasena, String Descripcion, String Rol, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("NombreCuenta",NombreCuenta);
        params.put("NombreUsuario",NombreUsuario);
        params.put("Correo",Correo);
        params.put("Contrasena",Contrasena);
        params.put("Descripcion",Descripcion);
        params.put("Rol",Rol);
    }

    @Nullable
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
