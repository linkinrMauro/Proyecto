package com.example.carrito.interfaces

interface DeleteItemCarrito {
    fun DeleteItem(id:String)
    fun Mas_uno(id:String,cantidad:Int)
    fun Menos_uno(id: String,cantidad: Int)
}