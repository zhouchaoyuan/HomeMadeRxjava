class HomeMadeSubscribeObservable<T>(
    private val source: HomeMadeObservableOnSubscribe<T>,
    private val thread: Int
) : HomeMadeObservableOnSubscribe<T> {

    override fun subscribe(observer: HomeMadeObserver<T>) {
        val downStream = HomeMadeSubscribeObserver(observer)
        Schedulers.INSTANCE.submitSubscribeWork(source, downStream, thread)
        //source.subscribe(downStream)
    }

    class HomeMadeSubscribeObserver<T>(
        private val downStream: HomeMadeObserver<T>
    ) : HomeMadeObserver<T> {
        override fun onComplete() {
            downStream.onComplete()
        }

        override fun onError(e: Throwable) {
            downStream.onError(e)
        }

        override fun onNext(item: T) {
            downStream.onNext(item)
        }

        override fun onSubscribe() {
            downStream.onSubscribe()
        }
    }
}