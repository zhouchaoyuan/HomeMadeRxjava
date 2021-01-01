interface HomeMadeObserver<T> {
    fun onSubscribe()
    fun onNext(item: T)
    fun onError(e: Throwable)
    fun onComplete()
}