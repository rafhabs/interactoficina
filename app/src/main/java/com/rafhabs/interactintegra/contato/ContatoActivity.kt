package com.rafhabs.interactintegra.contato

import android.os.Bundle
import android.view.View

import com.rafhabs.interactintegra.R
import com.rafhabs.interactintegra.application.ContatoApplication
import com.rafhabs.interactintegra.model.ContatosVO
import com.rafhabs.interactintegra.base.BaseActivity

import kotlinx.android.synthetic.main.activity_contato.*
import kotlinx.android.synthetic.main.activity_contato.toolBar

class ContatoActivity : BaseActivity()  {

    private var idContato: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contato)
        setupToolBar(toolBar, "Apontamento",true)
        setupContato()
        btnSalvarConato.setOnClickListener { onClickSalvarContato() }
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
                etcdEvento.setText(contato.codevento)
                etOS.setText(contato.os)
                etEvento.setText(contato.evento)
                etDatahorai.setText(contato.datahorai)
                etDatahoraf.setText(contato.datahoraf)
                etobs.setText(contato.observacao)
                progress.visibility = View.GONE
            }

        }).start()

    }

    private fun onClickSalvarContato(){
        val nome = etNome.text.toString()
        val codevento = etcdEvento.text.toString()
        val os = etOS.text.toString()
        val evento = etEvento.text.toString()
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