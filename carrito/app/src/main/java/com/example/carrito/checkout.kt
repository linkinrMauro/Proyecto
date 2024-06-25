package com.example.carrito

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.carrito.adaptadores.ItemcarritoAdapter
import com.example.carrito.db.Sqlmanager
import com.example.carrito.interfaces.DeleteItemCarrito
import com.example.carrito.modelos.Itemcarrito
import com.google.android.material.snackbar.Snackbar


class checkout : AppCompatActivity() , DeleteItemCarrito{
    private lateinit var lv2:ListView
    private var carrito:ArrayList<Itemcarrito> = ArrayList<Itemcarrito>()
    private lateinit var tvtotal:TextView
    private lateinit var btn_whatsapp:Button
    private lateinit var btn_vaciar_carrito:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_checkout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tvtotal=findViewById(R.id.tvtotal)
        lv2=findViewById(R.id.lv2)
        btn_whatsapp=findViewById(R.id.btn_whatsapp)
        loadcarrito()
        btn_whatsapp.setOnClickListener {
            var message = ""
            var total=0
            for(item in carrito){
                total=(item.precio*item.cantidad)+total
                message+="Producto :${item.nombre} \nPrecio: ${item.precio} Bs. \nCantidad: ${item.cantidad} Unidades \nTotal: ${item.precio*item.cantidad } Bs \n ----------------------------------------------------------\n"
            }
            sendMessage("********DETALLE PEDIDO********\n"+message+"Total a pagar : ${total} Bs")
        }

        btn_vaciar_carrito=findViewById(R.id.btn_vaciar_carrito)
        btn_vaciar_carrito.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Eliminar")
            builder.setMessage("¿ Esta seguro de vaciar el carrito ?.")
            builder.setPositiveButton("Aceptar") { dialog, which ->
                // Acción al presionar el botón "Aceptar"
                var sqlmanager= Sqlmanager(this)
                sqlmanager.deleteAllData()
                Toast.makeText(this, "Se vacio el Carrito", Toast.LENGTH_SHORT).show()
                finish()
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

    override fun DeleteItem(id:String){
        Log.w("tag","Se eliminara el item ${id}")
        var sqlmanager= Sqlmanager(this)
        var itemdelete=sqlmanager.DeleteItem(this,id)
        if (itemdelete){
            Log.w("tag","SE elimino el item")
            Toast.makeText(this, "Se elimino correctamente", Toast.LENGTH_SHORT).show()
            this.loadcarrito()
        }else{
            Log.w("tag","Ocurrio un error al intentar eliminar")
        }
    }

    private fun loadcarrito(){
        var sqlmanager= Sqlmanager(this)
        carrito=sqlmanager.listarCarrito(this)
        if (carrito.isEmpty()){
            finish()
        }else{
            var total=0
            for (item in carrito){
                total=(item.precio*item.cantidad)+total
            }
            var adapterCarrito=ItemcarritoAdapter(this,carrito,this);
            lv2.adapter=adapterCarrito;
            tvtotal.setText(total.toString()+" Bs")
        }

    }

    private fun sendMessage(message: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.setPackage("com.whatsapp")
        intent.putExtra(
            Intent.EXTRA_TEXT,
            message
        )

        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            ex.printStackTrace()
            Log.w("tag","El dispositivo no tiene instalado whatsapp")
        }


    }

    override fun Mas_uno(id:String,cantidad:Int){
        Log.w("tag","Este es el id ${id}")
        var sqlmanager= Sqlmanager(this)
        var cantidadUpdate=sqlmanager.AumentaCantidad(this,id,cantidad)
        if (cantidadUpdate){
            Log.w("tag","Se aumento en una cantidad mas ")
            loadcarrito()
        }else{
            Log.w("tag","Ocurrio un error al aumentar la cantidad")
            Toast.makeText(this, "Ocurrio un error inesperado, intentelo nuevamente", Toast.LENGTH_SHORT).show()
        }
    }

    override fun Menos_uno(id:String,cantidad: Int){
        var sqlmanager= Sqlmanager(this)
        var cantidadUpdate=sqlmanager.DisminuyeCantidad(this,id,cantidad)
        if (cantidadUpdate){
            Log.w("tag","Se disminuyo en una cantidad ")
            loadcarrito()
        }else{
            Log.w("tag","Item se elimino del carrito")
            Toast.makeText(this, "La cantidad no puede ser menor que 1", Toast.LENGTH_SHORT).show()
        }
    }

}