package com.example.carrito

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        //Declarando variable que almacenara una referencia al boton
        val login_btn: Button = findViewById(R.id.login);
        val login_invitado: Button = findViewById(R.id.btn_invitado);
        val usuarioEdt: EditText = findViewById(R.id.edtusuario);
        val passEdt: EditText = findViewById(R.id.edtpass);

        //una ves que se precione en el boton nos redirige a la pantalla de home
        login_invitado.setOnClickListener {
            val intent = Intent(this, home::class.java)
            startActivity(intent)
        }

        login_btn.setOnClickListener {

            if (usuarioEdt.text.toString().equals("admin") && passEdt.text.toString().equals("admin111")){
                val intent = Intent(this, home_admin::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Usuario o Password incorrectos..", Toast.LENGTH_SHORT).show()
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}