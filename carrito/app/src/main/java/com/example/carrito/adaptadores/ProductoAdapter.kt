package com.example.carrito.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.carrito.R
import com.example.carrito.interfaces.Productocarrito
import com.example.carrito.modelos.Producto

class ProductoAdapter(private val context: Context, private val dataSource: List<Producto>,private val listener: Productocarrito) : BaseAdapter() {

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
        val rowView = convertView ?: inflater.inflate(R.layout.item_producto, parent, false)

        val nombreTextView = rowView.findViewById<TextView>(R.id.tvNombre)
        val precioActualTextView = rowView.findViewById<TextView>(R.id.tvprecioactual)
        val ofertaTextView = rowView.findViewById<TextView>(R.id.tvoferta)
        val precioTextView = rowView.findViewById<TextView>(R.id.tvPrecio)
        val descripcionTextView = rowView.findViewById<TextView>(R.id.tvDescripcion)
        val btn_carrito=rowView.findViewById<Button>(R.id.btn_carrito)

        val producto = getItem(position) as Producto

        nombreTextView.text = producto.nombre
        //convirtiendo el dato precio que viene en tipo entero a tipo cadena con el metodo toString()
        precioActualTextView.text = producto.precio.toString()+" Bs"
        if (producto.oferta>0){
            ofertaTextView.text ="Oferta -"+ producto.oferta.toString()+" %"
            ofertaTextView.setBackgroundResource(android.R.color.holo_red_light)
        }else{
            ofertaTextView.text =""
            ofertaTextView.setBackgroundResource(android.R.color.transparent)
        }
       // ofertaTextView.text ="Oferta -"+ producto.oferta.toString()+" %"
        precioTextView.text = (producto.precio - (producto.precio*(producto.oferta/100.0))).toString()+" Bs"
        descripcionTextView.text=producto.detalle

        btn_carrito.setOnClickListener {
           // Toast.makeText(context, "Esta funcionando", Toast.LENGTH_SHORT).show()
            val precioFinal=(producto.precio - (producto.precio*(producto.oferta/100.0)))
            listener.addCarrito(Producto(producto.id,producto.nombre,precioFinal.toInt(),producto.detalle,producto.oferta))

        }

        return rowView
    }
}