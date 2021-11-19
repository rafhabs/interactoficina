package com.rafhabs.interactintegra.singleton

import com.rafhabs.interactintegra.model.ContatosVO

object ContatoSingleton {
    var lista: MutableList<ContatosVO> = mutableListOf()
}