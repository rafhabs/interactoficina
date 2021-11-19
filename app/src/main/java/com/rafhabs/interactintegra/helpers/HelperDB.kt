package com.rafhabs.interactintegra.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rafhabs.interactintegra.model.ContatosVO

class HelperDB(
    context: Context
) : SQLiteOpenHelper(context, NOME_BANCO, null, VERSAO_ATUAL) {

    companion object {
        private val NOME_BANCO = "contato.db"
        private val VERSAO_ATUAL = 12

    }

    val TABLE_NAME = "contato"
    val COLUMNS_ID = "id"
    val COLUMNS_NOME = "nome"
    val COLUMNS_TELEFONE = "telefone"
    val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
            "$COLUMNS_ID INTEGER NOT NULL," +
            "$COLUMNS_NOME TEXT NOT NULL," +
            "$COLUMNS_TELEFONE TEXT NOT NULL," +
            "" +
            "PRIMARY KEY($COLUMNS_ID AUTOINCREMENT)" +
            ")"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion != newVersion) {
            db?.execSQL(DROP_TABLE)
        }
        onCreate(db)
    }

    fun buscarContatos(busca: String, isBuscaPorID: Boolean = false) : List<ContatosVO> {


        val db = readableDatabase ?: return mutableListOf()
        var lista = mutableListOf<ContatosVO>()

        var where: String? = null
        var args: Array<String> = arrayOf()
        if(isBuscaPorID){
            where = "$COLUMNS_ID = ?"
            args = arrayOf("$busca")
        }else{
            where = "$COLUMNS_NOME LIKE ?"
            args = arrayOf("%$busca%")
        }
        var cursor = db.query(TABLE_NAME,null,where,args,null,null,null)

        //val sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMNS_NOME like ? OR $COLUMNS_TELEFONE like ?"
        //var buscacomPercentual = "%$busca%"
        //var cursor = db.rawQuery(sql, buscacomPercentual) ?: return mutableListOf()


        if (cursor == null) {
            db.close()
            return mutableListOf()
        }
        while (cursor.moveToNext()) {

            var contato = ContatosVO (
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNS_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMNS_NOME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMNS_TELEFONE))
            )
            lista.add(contato)

        }

    return lista
    }

    fun salvarContato(contatos: ContatosVO) {
        val db = writableDatabase ?: return

        var content = ContentValues()
        content.put(COLUMNS_NOME,contatos.nome)
        content.put(COLUMNS_TELEFONE,contatos.telefone)
        db.insert(TABLE_NAME,null,content)

       // val sql = "INSERT INTO $TABLE_NAME ($COLUMNS_NOME, $COLUMNS_TELEFONE) VALUES (?,?)"
        // var array = arrayOf(contatos.nome, contatos.telefone)
        //db.execSQL(sql, array)

        db.close()


    }

    fun deletarContato(id: Int) {

        val db = writableDatabase ?: return

        val sql = "DELETE FROM $TABLE_NAME WHERE $COLUMNS_ID = ?"
        val arg = arrayOf("$id")
        db.execSQL(sql,arg)

        //var where = """id = ?"""
        //var arg = arrayOf("$id")
        //db.delete(TABLE_NAME,where,arg)

        db.close()

    }

    fun updateContato(contato: ContatosVO) {

        val db = writableDatabase ?: return


        val sql = "UPDATE $TABLE_NAME SET $COLUMNS_NOME = ? , $COLUMNS_TELEFONE = ? WHERE $COLUMNS_ID = ? "
        val arg = arrayOf(contato.nome,contato.telefone,contato.id)
        db.execSQL(sql,arg)

        //val contant = ContentValues()
        //contant.put(COLUMNS_NOME,contato.nome)
        //contant.put(COLUMNS_TELEFONE,contato.telefone)
        //val where = "id = ?"
        //val arg = arrayOf("${contato.id}")
        //db.update(TABLE_NAME,contant,where,arg)

        db.close()

    }

}