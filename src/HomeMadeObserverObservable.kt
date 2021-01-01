class HomeMadeObserverObservable<T>(
    private val source: HomeMadeObservableOnSubscribe<T>,
    private val thread: Int
) : HomeMadeObservableOnSubscribe<T> {

    override fun subscribe(downStream: HomeMadeObserver<T>) {
        val observer = HomeMadeObserverObserver(downStream, thread)
        source.subscribe(observer)
    }

    class HomeMadeObserverObserver<T>(
        private val downStream: HomeMadeObserver<T>,
        private val thread: Int
    ) : HomeMadeObserver<T> {

        override fun onComplete() {
            Schedulers.INSTANCE.submitObserverWork({
                downStream.onComplete()
            }, thread)
        }

        override fun onError(e: Throwable) {
            Schedulers.INSTANCE.submitObserverWork({
                downStream.onError(e)
            }, thread)
        }

        override fun onNext(item: T) {
            Schedulers.INSTANCE.submitObserverWork({
                downStream.onNext(item)
            }, thread)
        }

        override fun onSubscribe() {
            Schedulers.INSTANCE.submitObserverWork({
                downStream.onSubscribe()
            }, thread)
        }
    }

}