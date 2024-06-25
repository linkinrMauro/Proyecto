package com.example.carrito


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.carrito.adaptadores.ProductoAdapter
import com.example.carrito.db.Sqlmanager
import com.example.carrito.interfaces.Productocarrito
import com.example.carrito.modelos.Itemcarrito
import com.example.carrito.modelos.Producto
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class home : AppCompatActivity(),Productocarrito {
    private  var productosF:MutableList<Producto> = mutableListOf()
    private lateinit var lv1:ListView
    private lateinit var btn_modal_carrito:ImageButton

    //firestore
    private lateinit var db:FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        lv1= findViewById(R.id.lv1)
        btn_modal_carrito=findViewById(R.id.btn_modal_carrito)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db= Firebase.firestore
        loadProductos()

        btn_modal_carrito.setOnClickListener {
            //funcion para ir a la pantalla del carrito
            var sqlmanager=Sqlmanager(this)
            var size_carrito=sqlmanager.getSizeCarrito(this);
            if (size_carrito<=0){
                Toast.makeText(this, "El carrito esta vacio", Toast.LENGTH_SHORT).show()
            }else {
                val intent = Intent(this, checkout::class.java)
                startActivity(intent)
            }
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
                            val prod=Producto(docs.document.id,nombre,precio.toInt(),detalle,oferta.toInt())
                            productosF.add(prod)
                        }
                        DocumentChange.Type.MODIFIED-> {
                            val id = docs.document.id
                            val data = docs.document.data
                            val nombre = data["nombre"] as String
                            val precio = (data["precio"] as Long)
                            val detalle = data["detalle"] as String
                            val oferta = (data["oferta"] as Long)
                            val producto = Producto(id, nombre, precio.toInt(), detalle, oferta.toInt())
                            val index = productosF.indexOfFirst { it.id == id }
                            if (index != -1) {
                                productosF[index] =producto

                            }
                        }
                        DocumentChange.Type.REMOVED-> {
                            Log.w("Tag", "eliminado ${docs.document.id} ${docs.document.data}")
                            val id = docs.document.id
                            val index = productosF.indexOfFirst { it.id == id }
                            if (index != -1) {
                                productosF.removeAt(index)

                            }
                        }
                    }

                    val ProductosAdp = ProductoAdapter(this, productosF,this)
                    lv1.adapter = ProductosAdp
                }
            }
    }

    override fun addCarrito(producto:Producto){
        val item=Itemcarrito(producto.id,producto.nombre,producto.precio,1);
        var sqlmanager=Sqlmanager(this)
        if(sqlmanager.CheckItem(this,producto.id)){

            var cantidad:Int=sqlmanager.getCantidadItem(this,producto.id)
            Log.w("tag","El producto ya existe en la tabla habra que actualizar")
            if (sqlmanager.UpdateItem(this,producto.id,cantidad+1)){
                Toast.makeText(this, "Se adiciono +1 a la cantidad", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Error al momento de actualizar la cantidad", Toast.LENGTH_SHORT).show()
            }
        }else{
            var response=sqlmanager.addItem(this,item)
            if (response){
                Toast.makeText(this, "Se agrego correctamente", Toast.LENGTH_SHORT).show()
               // Log.w("tag","Se agrego correctamente")
            }else{
                Toast.makeText(this, "Ocurrio un error ", Toast.LENGTH_SHORT).show()
                //Log.w("tag","ocurrio un error al agregar")
            }
        }
    }

}
