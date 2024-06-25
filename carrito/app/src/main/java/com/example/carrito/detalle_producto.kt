package com.example.carrito

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.carrito.modelos.Producto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class detalle_producto : AppCompatActivity() {
    private lateinit var nombre:EditText
    private lateinit var precio:EditText
    private lateinit var detalle:EditText
    private lateinit var oferta:EditText
    private lateinit var switch:Switch
    private lateinit var btn_update:Button
    private lateinit var btn_delete:Button
    private lateinit var db:FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_producto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db= Firebase.firestore
        nombre=findViewById(R.id.modnombre)
        precio=findViewById(R.id.modprecio)
        detalle=findViewById(R.id.moddetalle)
        oferta=findViewById(R.id.modoferta)
        switch=findViewById(R.id.switch2)
        btn_update=findViewById(R.id.btn_update)
        btn_delete=findViewById(R.id.btn_delete)
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                nombre.isEnabled=true
                precio.isEnabled=true
                detalle.isEnabled=true
                oferta.isEnabled=true
                btn_update.isEnabled=true
            } else {
                nombre.isEnabled=false
                precio.isEnabled=false
                detalle.isEnabled=false
                oferta.isEnabled=false
                btn_update.isEnabled=false
            }
        }
        val idProducto:String = intent.getStringExtra("id") as String
        getProducto(idProducto)
        btn_update.setOnClickListener {
            updateProducto(idProducto)
        }
        btn_delete.setOnClickListener {
            deleteProducto(idProducto)
        }
    }

    private fun getProducto(id:String){
        val ref=db.collection("productos").document(id)
        ref.get()
            .addOnSuccessListener { doc ->
                if (doc != null){
                    Log.w("Tag","Este es el item ${doc.data}")

                    nombre.setText(doc.get("nombre") as String)
                   precio.setText((doc.get("precio") as Long).toString())
                    detalle.setText(doc.get("detalle") as String)
                   oferta.setText((doc.get("oferta") as Long).toString())
                }else{
                    Log.w("tag","Ocurrio un problema ")
                }
             }
    }

    private fun updateProducto(id:String){
        val nombre = nombre.text.toString();
        val precio = precio.text.toString()
        val detalle = detalle.text.toString();
        val oferta = oferta.text.toString()
        if (!nombre.equals("") && !precio.equals("") && !detalle.equals("") && !oferta.equals("")){
            val prodmod= hashMapOf<String,Any>(
                "nombre" to nombre,
                "precio" to precio.toLong(),
                "detalle" to detalle,
                "oferta" to oferta.toLong(),
            )
            val ref = db.collection("productos").document(id)
            ref.update(prodmod)
                .addOnSuccessListener {
                    Toast.makeText(this, "Se actualizo correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Log.w("tag","ocurrio un error inesperado")
                }
        }else{
            Toast.makeText(this, "Algunos campos estan vacios", Toast.LENGTH_SHORT).show()
        }

    }

    private fun deleteProducto(id:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar")
        builder.setMessage("¿ Esta seguro de eliminar este producto ?.")
        builder.setPositiveButton("Aceptar") { dialog, which ->
            // Acción al presionar el botón "Aceptar"
            val ref = db.collection("productos").document(id)
            ref.delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Se elimino correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Ocurrio un error inesperado ", Toast.LENGTH_SHORT).show()
                }
            dialog.dismiss() // Cerrar la alerta
        }
        builder.setNegativeButton("Cancelar") { dialog, which ->
            // Acción al presionar el botón "Cancelar"
            dialog.dismiss() // Cerrar la alerta
        }

        // Crear y mostrar la alerta
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}