package com.chilik1020.steppercontrol.base

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

interface DataFlowObserver<T> : Observer<T> {

    override fun onSubscribe(disposable: Disposable) {
        println("${this.javaClass.typeName} onSubscribe")
    }

    override fun onComplete() {
        println("${this.javaClass.typeName} onComplete")
    }

    override fun onError(th: Throwable) {
        println("${this.javaClass.typeName} onError")
        println("${th.message}")
    }
}