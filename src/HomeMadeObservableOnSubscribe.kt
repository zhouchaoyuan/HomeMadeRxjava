interface HomeMadeObservableOnSubscribe<T> {
    fun subscribe(observer: HomeMadeObserver<T>)
}