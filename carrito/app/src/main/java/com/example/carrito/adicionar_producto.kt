package com.example.carrito

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class adicionar_producto : AppCompatActivity() {
    private lateinit var edtnombre: EditText;
    private lateinit var edtprecio: EditText;
    private lateinit var edtdetalle: EditText;
    private lateinit var edtoferta: EditText;
    private lateinit var btn_agregar:Button;
    //firestore
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_adicionar_producto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db= Firebase.firestore
        edtnombre=findViewById(R.id.edtnombre);
        edtprecio=findViewById(R.id.edtprecio);
        edtdetalle=findViewById(R.id.edtdetalle);
        edtoferta=findViewById(R.id.edtoferta);
        btn_agregar=findViewById(R.id.btn_agregar);
        btn_agregar.setOnClickListener {
            createProducto();

        }

    }

    private fun createProducto(){
        val nombre = edtnombre.text.toString();
        val precio = edtprecio.text.toString()
        val detalle = edtdetalle.text.toString();
        val oferta = edtoferta.text.toString()

        if (!nombre.equals("") && !precio.equals("") && !detalle.equals("") && !oferta.equals("")){
            val prod= hashMapOf(
                "nombre" to nombre,
                "precio" to precio.toLong(),
                "detalle" to detalle,
                "oferta" to oferta.toLong(),
            )
            db.collection("productos")
                .document()
                .set(prod)
                .addOnSuccessListener { documentReferent ->
                    Toast.makeText(this, "Se creo un nuevo producto", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                    finish();
                }
                .addOnFailureListener {
                    Log.w("tag","Error al crear el documento")
                }


        }else{
            Toast.makeText(this,"Algunos campos estan vacios", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos(){
        edtnombre.setText("")
        edtprecio.setText("")
        edtdetalle.setText("")
        edtoferta.setText("")
    }
}