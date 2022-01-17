package com.rafhabs.interactintegra


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.content.FileProvider.getUriForFile
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafhabs.interactintegra.adapter.ContatoAdapter
import com.rafhabs.interactintegra.application.ContatoApplication
import com.rafhabs.interactintegra.base.BaseActivity
import com.rafhabs.interactintegra.contato.ContatoActivity
import com.rafhabs.interactintegra.model.ContatosVO
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : BaseActivity() {

    private var adapter:ContatoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolBar(toolBar, "M&R - Manutenção - OS",false)
        setupListView()
        setupOnClicks()

    }

    private fun setupOnClicks(){
        fab.setOnClickListener { onClickAdd() }
        ivBuscar.setOnClickListener { onClickBuscar() }
        ivExport.setOnClickListener { onClickExportar() }
    }

    override fun onResume() {
        super.onResume()
        onClickBuscar()
    }

    private fun onClickAdd(){
        val intent = Intent(this,ContatoActivity::class.java)
        startActivity(intent)
    }

    private fun onClickBuscar(){
        val busca = etBuscar.text.toString()
        progress.visibility = View.VISIBLE

        //Thread.sleep(5000000)

        Thread(Runnable {

            Thread.sleep(500)

            var listaFiltrada: List<ContatosVO> = mutableListOf()

            try {
                listaFiltrada = ContatoApplication.instance.helperDB?.buscarContatos(busca)?: mutableListOf()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            runOnUiThread {
                adapter = ContatoAdapter(this,listaFiltrada) {onClickItemRecyclerView(it)}
                recyclerView.adapter = adapter
                progress.visibility = View.GONE
                Toast.makeText(this,"Buscando por $busca",Toast.LENGTH_SHORT).show()
            }

        }).start()


    }



    private fun onClickExportar(){

        val busca = etBuscar.text.toString()
        var listaFiltrada: List<ContatosVO> = mutableListOf()

        try {
            listaFiltrada = ContatoApplication.instance.helperDB?.buscarContatos(busca)?: mutableListOf()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        val csv_header = "os,codevento,evento,datahorai,datahoraf,observacao,nome"
        var filename = "export.csv"

        var path = getExternalFilesDir(null)   //get file directory for this package
        //(Android/data/.../files | ... is your app package)

        //create fileOut object
        var fileOut = File(path, filename)
        //delete any file object with path and filename that already exists
        fileOut.delete()
        //create a new file
        fileOut.createNewFile()
        //append the header and a newline
        fileOut.appendText(csv_header)

        for(OS in listaFiltrada){

            fileOut.appendText("\n")
            // trying to append some data into csv file
            fileOut.appendText("${OS.os}")
            fileOut.appendText(",${OS.codevento}")
            fileOut.appendText(",${OS.evento}")
            fileOut.appendText(",${OS.datahorai}")
            fileOut.appendText(",${OS.datahoraf}")
            fileOut.appendText(",${OS.observacao}")
            fileOut.appendText(",${OS.nome}")
            fileOut.appendText("")

        }

        if(fileOut.exists())
            try {

                // here, com.example.myapp.fileprovider should match the file provider in your manifest
                val contentUri = getUriForFile(this.applicationContext, "com.rafhabs.interactintegra.fileprovider", fileOut)

                val intent = Intent(Intent.ACTION_SEND)
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                intent.setDataAndType(
                    contentUri,
                    "text/csv")
                intent.putExtra(Intent.EXTRA_STREAM, contentUri);
                startActivity(intent)

            }catch (e: java.lang.Exception) {
                e.printStackTrace()
                println("debug: failed")
            }

    }

    private fun setupListView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun onClickItemRecyclerView(index: Int){
        val intent = Intent(this,ContatoActivity::class.java)
        intent.putExtra("index", index)
        startActivity(intent)
    }

}