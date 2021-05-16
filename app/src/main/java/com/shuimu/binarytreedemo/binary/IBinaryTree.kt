package com.shuimu.binarytreedemo.binary

/**
 * @author shuimu{lwp}
 * @time 2019/8/14  11:59
 * @desc 二叉树基本的操作方法
 */
interface IBinaryTree<T> {
    /**
     * 判断是否是一个空树。按照二叉树的定义，树的节点个数N=0则表示为空树；树的节点个数 N>0时，有且仅有一个根节点和零个或一个左右子树
     */
    val isEmpty: Boolean

    /**
     * 二叉树的节点数量
     */
    val count: Int

    /**
     * 先根遍历，也叫前序遍历
     */
    fun preOrder()

    /**
     * 中根遍历，也叫中序遍历
     */
    fun inOrder()

    /**
     * 后根遍历，也叫后序遍历
     */
    fun postOrder()

    /**
     * 查找某个节点的父节点
     */
    fun getParent(node: BinaryNode<T>?): BinaryNode<T>?

    /**
     * 该方法表示，在node节点下插入一个新的节点，isLeft表示新插入的节点是node节点的左子树还是右子树
     */
    fun inserNode(node: BinaryNode<T>, data: T, isLeft: Boolean): BinaryNode<T>?

    /**
     * 该方法表示：参数node节点的左右子树
     */
    fun remodeNode(node:BinaryNode<T>?,isLeft:Boolean):BinaryNode<T>?

    /**
     * 移除树中所有的节点
     */
    fun removeAll()
}