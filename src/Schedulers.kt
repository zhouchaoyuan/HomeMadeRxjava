import java.util.concurrent.Executors

class Schedulers {
    private var IOThreadPool = Executors.newCachedThreadPool()//IO线程池
    private var ComputePool = Executors.newScheduledThreadPool(4)//compute线程池
//    private var handler = Handler(Looper.getMainLooper()) { message ->
//        //这里就是主线程了
//        message.callback.run()
//        return@Handler true
//    }

    companion object {
        //定义一个线程安全的单例模式
        val INSTANCE: Schedulers by
        lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Schedulers()
        }
        private const val IO = 0
        private const val MAIN = 1
        private const val COMPUTE = 2

        fun io(): Int {
            return IO
        }

        fun mainThread(): Int {
            return MAIN
        }

        fun compute(): Int {
            return COMPUTE
        }
    }

    fun <T> submitSubscribeWork(
        source: HomeMadeObservableOnSubscribe<T>,
        downStream: HomeMadeObserver<T>,
        thread: Int//指定的线程
    ) {
        when (thread) {
            IO -> {
                IOThreadPool.submit {
                    //从线程池抽取一个线程执行上游和下游的连接操作
                    source.subscribe(downStream)
                }
            }
            MAIN -> {
//                val message = Message.obtain(it) {
//                    //上下游的连接
//                    source.subscribe(downStream)
//                }
//                it.sendMessage(message)
            }
            COMPUTE -> {
                ComputePool.submit {
                    source.subscribe(downStream)
                }
            }
        }
    }

    fun submitObserverWork(function: () -> Unit, thread: Int) {
        when (thread) {
            IO -> {
                IOThreadPool?.submit {
                    function.invoke() //调用高阶函数
                }
            }
            MAIN -> {
                function.invoke()
//                handler?.let {
//                    val m=Message.obtain(it){
//                        function.invoke()//调用高阶函数
//                    }
//                    it.sendMessage(m)
//                }
            }
            COMPUTE -> {
                ComputePool?.submit(function) {
                    function.invoke()
                }
            }
        }
    }
}