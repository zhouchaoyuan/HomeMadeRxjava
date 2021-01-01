class HomeMadeObservable<T> constructor() {

    private var source: HomeMadeObservableOnSubscribe<T>? = null

    constructor(source: HomeMadeObservableOnSubscribe<T>) : this() {
        this.source = source
    }

    companion object {
        //静态方法创建一个真正的被观察者, 伴生对象,伴生对象在类中只能存在一个
        fun <T> create(source: HomeMadeObservableOnSubscribe<T>): HomeMadeObservable<T> {
            return HomeMadeObservable(source)
        }
    }

    fun setObserver(downStream: HomeMadeObserver<T>) {
        downStream.onSubscribe()
        source?.subscribe(downStream)
    }

    fun <R> map(func: (T) -> R): HomeMadeObservable<R> {
        //source就是上游真正的被观察者
        val map = HomeMadeMapObservable(this.source!!, func)
        return HomeMadeObservable(map)
    }

    fun subscribeOn(scheduler: Int): HomeMadeObservable<T> {
        val subscribe = HomeMadeSubscribeObservable(source!!, scheduler)
        return HomeMadeObservable(subscribe)
    }

    fun observerOn(scheduler: Int): HomeMadeObservable<T> {
        val subscribe = HomeMadeObserverObservable(this.source!!, scheduler)
        return HomeMadeObservable(subscribe)
    }
}