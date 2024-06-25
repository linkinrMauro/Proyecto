package com.example.carrito.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.carrito.modelos.Itemcarrito

import kotlin.collections.ArrayList

class Sqlmanager(context:Context):SQLiteOpenHelper(context,"carrito.db",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {

        db!!.execSQL("CREATE TABLE items(id TEXT PRIMARY KEY,nombre TEXT,precio INTEGER,cantidad INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun addItem(context:Context,item:Itemcarrito):Boolean{
        var res=true
        var values=ContentValues()
        values.put("id",item.id)
        values.put("nombre",item.nombre)
        values.put("precio",item.precio.toInt())
        values.put("cantidad",item.cantidad.toInt())
        var dbManager=Sqlmanager(context)
        var manager=dbManager.writableDatabase
        try {
            manager.insert("items",null,values)
        }catch (e:Exception){
            print(e.message)
            res=false
        }
        finally {
            dbManager.close()
        }
        return res
    }

    fun CheckItem(context:Context,id:String):Boolean{
        var res:Boolean;
        val sqlQuery:String="select id from items where id like '${id}'"
        var dbManager=Sqlmanager(context)
        var manager=dbManager.readableDatabase
        var data = manager.rawQuery(sqlQuery,null)
        res=data.moveToFirst()
        return res
    }

    fun getCantidadItem(context: Context,id:String):Int{
        var res:Int=0;
        val sqlQuery:String="select cantidad from items where id like '${id}'"
        var dbManager=Sqlmanager(context)
        var manager=dbManager.readableDatabase
        var data = manager.rawQuery(sqlQuery,null)
        if (data.moveToFirst()){
            res=data.getInt(0)
        }
        return res
    }

    fun listarCarrito(context: Context):ArrayList<Itemcarrito>{
        var arreglo=ArrayList<Itemcarrito>()
        val sqlQuery="select * from items order by nombre"
        var dbManager=Sqlmanager(context)
        var manager=dbManager.readableDatabase
        var data = manager.rawQuery(sqlQuery,null)
        while (data.moveToNext()){
            arreglo.add(Itemcarrito(data.getString(0),data.getString(1),data.getInt(2),data.getInt(3)))
        }
        return arreglo
    }

    fun DeleteItem(context: Context,id:String):Boolean{
        var res=true;
        var dbManager=Sqlmanager(context)
        var m=dbManager.writableDatabase
        var arr= arrayOf(id)
        try {
            m.delete("items","id like ?",arr)
        }catch (e:Exception){
            e.printStackTrace()
            res=false
        }
        finally {
            dbManager.close()
        }

        return res
    }

    fun UpdateItem(context: Context,id:String,cantidad:Int):Boolean{
        var res=true;
        var dbManager=Sqlmanager(context)
        var m=dbManager.writableDatabase
        var arr= arrayOf(id)
        var values=ContentValues()
        values.put("cantidad",cantidad)
        try {
            m.update("items",values,"id like ?",arr)
        }catch (e:Exception){
            e.printStackTrace()
            res=false
        }
        finally {
            dbManager.close()
        }

        return res
    }

    fun deleteAllData() {
        writableDatabase.execSQL("DELETE FROM items")
    }

    fun AumentaCantidad(context: Context,id:String,cantidad: Int):Boolean{
        var res=true;
        var dbManager=Sqlmanager(context)
        var mgr=dbManager.writableDatabase
        var arr= arrayOf(id)
        var values=ContentValues()
        values.put("cantidad",cantidad+1)
        try {
            mgr.update("items",values,"id like ?",arr)
        }catch (e:Exception){
            e.printStackTrace()
            res=false
        }
        finally {
            dbManager.close()
        }

        return res
    }

    fun DisminuyeCantidad(context: Context,id:String,cantidad: Int):Boolean{
        var res:Boolean=true;
        if ((cantidad-1)<=0){
          res=false
        }else {
            var dbManager=Sqlmanager(context)
            var mgr=dbManager.writableDatabase
            var arr= arrayOf(id)
            var values=ContentValues()
            values.put("cantidad",cantidad-1)
            try {
                mgr.update("items",values,"id like ?",arr)
            }catch (e:Exception){
                e.printStackTrace()
                res=false
            }
            finally {
                dbManager.close()
            }

        }
        return res
    }

    fun getSizeCarrito(context: Context):Int{
        val sqlQuery = "SELECT COUNT(*) FROM items"
        val dbManager = Sqlmanager(context)
        val manager = dbManager.readableDatabase
        val data = manager.rawQuery(sqlQuery, null)

        var count = 0
        //Haciendo que el cursor se mueva a la primera posicion para extraer el dato
        if (data.moveToFirst()) {
            count = data.getInt(0)
        }
        data.close()
        manager.close()
        return count
    }

    fun UpdateItemFirebase(context: Context,id:String,nombre:String,precio:Int):Boolean{
        var res=true;
        var dbManager=Sqlmanager(context)
        var m=dbManager.writableDatabase
        var arr= arrayOf(id)
        var values=ContentValues()
        values.put("nombre",nombre)
        values.put("precio",precio)
        try {
            m.update("items",values,"id like ?",arr)
        }catch (e:Exception){
            e.printStackTrace()
            res=false
        }
        finally {
            dbManager.close()
        }

        return res
    }

}