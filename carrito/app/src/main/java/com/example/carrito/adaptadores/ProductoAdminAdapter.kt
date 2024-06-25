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
import com.example.carrito.modelos.Producto

class ProductoAdminAdapter(private val context: Context, private val dataSource: List<Producto>) : BaseAdapter() {

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
        val rowView = convertView ?: inflater.inflate(R.layout.item_producto_admin, parent, false)

        val nombreTextView = rowView.findViewById<TextView>(R.id.prodnombre)
        val precioTextView = rowView.findViewById<TextView>(R.id.prodprecio)



        val producto = getItem(position) as Producto


        nombreTextView.text = producto.nombre
        precioTextView.text = producto.precio.toString()
        return rowView
    }
}