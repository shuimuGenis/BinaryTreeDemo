package com.shuimu.binarytreedemo.binary

/**
 * @author shuimu{lwp}
 * @time 2019/8/14  11:59
 * @desc
 */
class BinaryNode<T>(
    var data: T,
    var left: BinaryNode<T>? = null,
    var right: BinaryNode<T>? = null
) {
    override fun equals(other: Any?): Boolean {
        return other as? BinaryNode<*> != null && data == other.data
    }
}