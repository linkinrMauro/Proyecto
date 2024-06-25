package com.example.carrito

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.carrito.adaptadores.ProductoAdapter
import com.example.carrito.adaptadores.ProductoAdminAdapter
import com.example.carrito.db.Sqlmanager
import com.example.carrito.modelos.Producto
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class home_admin : AppCompatActivity() {
    private lateinit var lv1:ListView;
    private lateinit var btn_add:ImageButton
    private  var productosF:MutableList<Producto> = mutableListOf()
    //firestore
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_admin)
        btn_add=findViewById(R.id.btn_prod_add)
        lv1=findViewById(R.id.lv1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db= Firebase.firestore
        btn_add.setOnClickListener {
            val intent = Intent(this, adicionar_producto::class.java)
            startActivity(intent)
        }
        loadProductos()
        lv1.setOnItemClickListener { parent, view, position, id ->
            // Obtener el elemento clicado
            val item = parent.getItemAtPosition(position) as Producto
            val id=item.id
            val intent = Intent(this, detalle_producto::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }

    private fun loadProductos(){
        db.collection("productos")
            .addSnapshotListener { snapshots, e ->
                if (e!=null){
                    Log.w("Tag","Escucha fallida");
                    return@addSnapshotListener
                }
                for (docs in snapshots!!.documentChanges){
                    when(docs.type){
                        DocumentChange.Type.ADDED->{
                            val nombre:String=docs.document.data["nombre"] as String;
                            val precio:Long=docs.document.data["precio"] as Long;
                            val detalle:String=docs.document.data["detalle"] as String;
                            val oferta:Long=docs.document.data["oferta"] as Long;
                            productosF.add(Producto(docs.document.id,nombre,precio.toInt(),detalle,oferta.toInt()))
                        }
                        DocumentChange.Type.MODIFIED-> {

                            val id = docs.document.id
                            val data = docs.document.data
                            val nombre = data["nombre"] as String
                            val precio = (data["precio"] as Long)
                            val detalle = data["detalle"] as String
                            val oferta = (data["oferta"] as Long)
                            UpdateItem(id,nombre,precio.toInt(),oferta.toInt())
                            val producto = Producto(id, nombre, precio.toInt(), detalle, oferta.toInt())
                            val index = productosF.indexOfFirst { it.id == id }
                            if (index != -1) {
                                productosF[index] =producto
                            }
                        }
                        DocumentChange.Type.REMOVED-> {
                            Log.w("Tag", "eliminado ${docs.document.id} ${docs.document.data}")
                            DeleteItem(docs.document.id)
                            val id = docs.document.id
                            val index = productosF.indexOfFirst { it.id == id }
                            if (index != -1) {
                                productosF.removeAt(index)
                            }
                        }
                    }

                   val ProductosAdp = ProductoAdminAdapter(this, productosF)
                  // val ProductosAdp= ArrayAdapter(this, android.R.layout.simple_list_item_1, productosF)
                    lv1.adapter = ProductosAdp
                }
            }
    }

//Elimina item del carrito tambien
    private fun DeleteItem(id:String){
        var sqlmanager= Sqlmanager(this)
        var deleteItemCarrito=sqlmanager.DeleteItem(this,id)
        if(deleteItemCarrito){
            Log.w("tag","Se elimino tambien del carrito")
        }else{
            Log.w("tag","No se encontro, o hubo un error para que no se complete la accion")
        }
    }
//al actulizar el item en firebase tambien se actualiza en la base de datos de carrito
    private fun UpdateItem(id:String,nombre:String,precio:Int,oferta:Int){
        var sqlmanager= Sqlmanager(this)
        val precioFinal=(precio - (precio*(oferta/100.0))).toInt()
        var updateItemCarrito=sqlmanager.UpdateItemFirebase(this,id,nombre,precioFinal)
        if(updateItemCarrito){
            Log.w("tag","Se elimino tambien del carrito")
        }else{
            Log.w("tag","No se encontro, o hubo un error para que no se complete la accion")
        }
    }

}