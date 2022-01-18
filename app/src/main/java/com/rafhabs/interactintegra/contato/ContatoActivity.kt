package com.rafhabs.interactintegra.contato

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView.CommaTokenizer
import com.rafhabs.interactintegra.R
import com.rafhabs.interactintegra.application.ContatoApplication
import com.rafhabs.interactintegra.base.BaseActivity
import com.rafhabs.interactintegra.model.ContatosVO
import kotlinx.android.synthetic.main.activity_contato.*

class ContatoActivity : BaseActivity()  {

    private var idContato: Int = -1

    private val EVENTOS = arrayOf(
        "Evento 1", "Evento 2", "Evento 3", "Evento 4", "Evento 5"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contato)
        setupToolBar(toolBar, "Ordem de Serviço",true)
        setupContato()
        setupOnMultitext()
        btnSalvarConato.setOnClickListener { onClickSalvarContato() }


    }

    private fun setupOnMultitext(){

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line, EVENTOS
        )
        etcdEvento2.setAdapter(adapter)

    }

    private fun setupContato(){
        
        idContato = intent.getIntExtra("index",-1)
        if (idContato == -1){
            btnExcluirContato.visibility = View.GONE
            return
        }

        progress.visibility = View.VISIBLE

        Thread(Runnable {

            Thread.sleep(500)

            var lista = ContatoApplication.instance.helperDB?.buscarContatos("$idContato",true) ?: return@Runnable
            var contato = lista.getOrNull(0) ?: return@Runnable

            runOnUiThread {
                etNome.setText(contato.nome)
                etcdEvento2.setText(contato.codevento)
                etOS.setText(contato.os)
                //etEvento.setText(contato.evento)
                etDatahorai.setText(contato.datahorai)
                etDatahoraf.setText(contato.datahoraf)
                etobs.setText(contato.observacao)
                progress.visibility = View.GONE
            }

        }).start()

    }

    private fun onClickSalvarContato(){
        val nome = etNome.text.toString()
        val codevento = etcdEvento2.text.toString()
        val os = etOS.text.toString()
        val evento = etcdEvento2.text.toString()
        val datahorai = etDatahorai.text.toString()
        val datahoraf = etDatahoraf.text.toString()
        val observacao = etobs.text.toString()
        val contato = ContatosVO(
            idContato,
            nome,
            codevento,
            os,
            evento,
            datahorai,
            datahoraf,
            observacao,
        )

        progress.visibility = View.VISIBLE
        Thread(Runnable {

            Thread.sleep(500)
            if(idContato == -1) {
                ContatoApplication.instance.helperDB?.salvarContato(contato)
            }else{
                ContatoApplication.instance.helperDB?.updateContato(contato)
            }

            runOnUiThread {
                progress.visibility = View.GONE
                finish()
            }


        }).start()

    }

    fun onClickExcluirContato(view: View) {


        if(idContato > -1){

            progress.visibility = View.VISIBLE
            Thread(Runnable {

                Thread.sleep(500)
                ContatoApplication.instance.helperDB?.deletarContato(idContato)

                runOnUiThread {
                    progress.visibility = View.GONE
                    finish()
                }


            }).start()
        }
    }


}

