class HomeMadeMapObservable<T, R>(
    private val source: HomeMadeObservableOnSubscribe<T>,
    private val func: ((T) -> R)
) : HomeMadeObservableOnSubscribe<R> {

    override fun subscribe(downStream: HomeMadeObserver<R>) {
        //此时的downStream就是真正的下游
        val map = HomeMadeMapObserver(downStream, func)//创建自己的观察者对象
        source.subscribe(map)
    }

    class HomeMadeMapObserver<T, R>(
        private val downStream: HomeMadeObserver<R>,
        private val func: ((T) -> R)
    ) : HomeMadeObserver<T> {
        override fun onComplete() {
            downStream.onComplete()
        }

        override fun onError(e: Throwable) {
            downStream.onError(e)
        }

        override fun onNext(item: T) {
            val r = func.invoke(item)
            downStream.onNext(r)
        }

        override fun onSubscribe() {
            downStream.onSubscribe()
        }
    }
}
