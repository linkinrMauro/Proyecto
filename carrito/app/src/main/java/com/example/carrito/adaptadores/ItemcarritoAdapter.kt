package com.example.carrito.adaptadores

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.example.carrito.R
import com.example.carrito.interfaces.DeleteItemCarrito
import com.example.carrito.interfaces.Productocarrito
import com.example.carrito.modelos.Itemcarrito
import com.example.carrito.modelos.Producto

class ItemcarritoAdapter(private val context: Context, private val dataSource: ArrayList<Itemcarrito>,private val listener: DeleteItemCarrito) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = convertView ?: inflater.inflate(R.layout.item_carrito, parent, false)

        val nombreTextView = rowView.findViewById<TextView>(R.id.nombre_prod_carrito)
        val precioTextView = rowView.findViewById<TextView>(R.id.precio_prod_carrito)
        val cantidadTextView = rowView.findViewById<TextView>(R.id.cantidad_prod_carrito)
        val totalTextView = rowView.findViewById<TextView>(R.id.total)
        val btn_delete =rowView.findViewById<Button>(R.id.btn_delete_item_carrito)
        val btn_mas =rowView.findViewById<Button>(R.id.btn_mas)
        val btn_menos =rowView.findViewById<Button>(R.id.btn_menos)
        val item_carrito = getItem(position) as Itemcarrito

        nombreTextView.text =item_carrito.nombre
        precioTextView.text = item_carrito.precio.toString()+" Bs"
        cantidadTextView.text = item_carrito.cantidad.toString()+" Unidades"
        totalTextView.text = (item_carrito.cantidad*item_carrito.precio).toString()+" Bs"
        btn_delete.setOnClickListener {
            // Toast.makeText(context, "Esta funcionando", Toast.LENGTH_SHORT).show()
            listener.DeleteItem(item_carrito.id)
        }

        btn_mas.setOnClickListener{
            listener.Mas_uno(item_carrito.id,item_carrito.cantidad)
        }

        btn_menos.setOnClickListener{
            listener.Menos_uno(item_carrito.id,item_carrito.cantidad)
        }


        return rowView
    }
}