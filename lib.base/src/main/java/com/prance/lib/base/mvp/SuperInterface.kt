package com.prance.lib.base.mvp

/**
 * Description :
 * @author  XQ Yang
 * @date 2018/7/5  11:45
 */
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface ITopView : LifecycleOwner {
    fun getContext(): Context?

    fun inited() {}

    fun onNetworkError(throwable: Throwable): Boolean = false
}

interface ITopPresenter : LifecycleObserver {
    fun attachView(view: ITopView)
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun detachView()
}


interface ITopModel {
    fun onDetach()
}


interface IView<P : ITopPresenter> : ITopView {
    var mPresenter: P

    override fun inited() {
        mPresenter.attachView(this)
    }
}

interface IPresenter<V : ITopView, out M : IModel> : ITopPresenter {
    var mView: V?
    val mModel: M
    fun getContext() = mView?.getContext()

    @Suppress("UNCHECKED_CAST")
    override fun attachView(view: ITopView) {
        mView = view as V
        mView?.lifecycle?.addObserver(this)
    }

    override fun detachView() {
        mModel.onDetach()
        mView = null
    }
}

interface IModel : ITopModel {
    val mDisposablePool: CompositeDisposable

    fun addDisposable(disposable: Disposable) {
        mDisposablePool.add(disposable)
    }

    override fun onDetach() {
        mDisposablePool.clear()
    }
}
