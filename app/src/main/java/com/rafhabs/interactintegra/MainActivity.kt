package com.rafhabs.interactintegra

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager



import com.rafhabs.interactintegra.R
import com.rafhabs.interactintegra.base.BaseActivity
import com.rafhabs.interactintegra.contato.ContatoActivity
import com.rafhabs.interactintegra.adapter.ContatoAdapter
import com.rafhabs.interactintegra.application.ContatoApplication
import com.rafhabs.interactintegra.model.ContatosVO
import com.rafhabs.interactintegra.singleton.ContatoSingleton

import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : BaseActivity() {

    private var adapter:ContatoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolBar(toolBar, "Lista de contatos",false)
        setupListView()
        setupOnClicks()

    }

    private fun setupOnClicks(){
        fab.setOnClickListener { onClickAdd() }
        ivBuscar.setOnClickListener { onClickBuscar() }
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



    private fun setupListView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun onClickItemRecyclerView(index: Int){
        val intent = Intent(this,ContatoActivity::class.java)
        intent.putExtra("index", index)
        startActivity(intent)
    }

}