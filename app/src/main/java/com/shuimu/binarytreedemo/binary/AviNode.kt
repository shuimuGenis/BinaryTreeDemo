package com.shuimu.binarytreedemo.binary

/**
 * @author shuimu{lwp}
 * @time 2019/8/14  11:59
 * @desc
 */
class AviNode<T>(
    var data: T,
    var left: AviNode<T>? = null,
    var right: AviNode<T>? = null,
    var height: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        return other is AviNode<*> && data == other.data
    }
}