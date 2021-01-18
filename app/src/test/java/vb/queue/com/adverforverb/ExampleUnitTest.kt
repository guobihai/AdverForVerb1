package vb.queue.com.adverforverb

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun text() {
//        val str = "cmd1,zhangsan,804"
//        val array = str.split(",")
//        System.out.println(array)
        val ran = (Math.random() * 9000).toInt() + 1000
        System.out.println("=====ran===="+ran);
    }

    @Test
    fun repal() {
        var content = "衰老是每個人都會經歷的階段有說法指在我們出現皺紋和白髮前某類器官已經開始衰老，例如大腦和肺部踏入衰老的關卡是20歲，骨骼和耳朵則是30歲，腎臟、牙齒、眼睛是40歲，腸道則是55歲。"
        val showText = content.substring(0, 30)
        val showText1 = content.substring(5, 9)
        println(showText.toString())
        println(showText1.toString())
    }
}
