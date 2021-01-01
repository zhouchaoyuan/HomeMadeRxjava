class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            HomeMadeObservable.create(object :HomeMadeObservableOnSubscribe<Int> {
                override fun subscribe(observer: HomeMadeObserver<Int>) {
                    println("上游发送数据:10")
                    println("上游线程:${Thread.currentThread().name}")
                    observer.onNext(10)
                }
            }).map { item->
                "这是map操作符转换后的数据:${item*10}"
            }
                .subscribeOn(Schedulers.io())
                .observerOn(Schedulers.compute())
                .setObserver(object : HomeMadeObserver<String> {
                override fun onComplete() {
                }

                override fun onError(e: Throwable) {
                }

                override fun onSubscribe() {
                    println("onSubscribe")
                }

                override fun onNext(item: String) {
                    println("下游接收到数据:$item")
                    println("下游线程:${Thread.currentThread().name}")
                }
            })
        }
    }
}
