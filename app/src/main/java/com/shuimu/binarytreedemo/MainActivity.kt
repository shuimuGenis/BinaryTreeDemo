package com.shuimu.binarytreedemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shuimu.binarytreedemo.binary.AviTree
import com.shuimu.binarytreedemo.binary.BinaryNode
import com.shuimu.binarytreedemo.binary.BinarySortTree
import com.shuimu.binarytreedemo.binary.BinaryTree

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

fun main() {
   /* testTree01()

    println(
        "20在数组中的索引：${search(
            intArrayOf(1, 5, 9, 10, 13, 15, 20, 22, 24, 26, 33, 35, 39, 40),
            40
        )}"
    )
    testSort001()*/
    testAviTree()
}

fun testAviTree(){
    val aviTree:AviTree<Int> = AviTree()
    aviTree.inser(20)
    aviTree.inser(22)
    aviTree.inser(23)
    aviTree.inser(18)
    aviTree.inser(19)
    aviTree.inser(16)
    aviTree.preOrder()
}

fun testSort001(){
    val sortTree : BinarySortTree<Int> =BinarySortTree()
    sortTree.inser(20)
    sortTree.inser(30)
    sortTree.inser(22)
    sortTree.inser(25)
    sortTree.inser(18)
    sortTree.inser(28)
    sortTree.inser(16)
    sortTree.inser(19)
    sortTree.inser(12)
    sortTree.inser(13)
    sortTree.preOrder()
    println("节点25的后续节点：${sortTree.flowNode(25,sortTree.root)?.data.toString()}")
    println("删除了节点${sortTree.deleteNode(16)?.data}")
    sortTree.preOrder()
}

fun testTree01() {
    val nodeA: BinaryNode<String> = BinaryNode("A")
    val nodeB: BinaryNode<String> = BinaryNode("B")
    val nodeC: BinaryNode<String> = BinaryNode("C")
    val nodeD: BinaryNode<String> = BinaryNode("D")
    val nodeE: BinaryNode<String> = BinaryNode("E")
    val nodeF: BinaryNode<String> = BinaryNode("F")
    val nodeG: BinaryNode<String> = BinaryNode("G")
    val tree: BinaryTree<String> = BinaryTree()
    tree.root = nodeA
    nodeA.left = nodeB
    nodeA.right = nodeC
    nodeB.left = nodeD
    nodeB.right = nodeE
    nodeC.right = nodeF


    println("树是否是空树：${tree.isEmpty}")
    println("树有多少个节点：${tree.count}")
    tree.preOrder()
    tree.inOrder()
    tree.postOrder()
    println("D节点的父亲节点是：${tree.getParent(nodeD)?.data.toString()}")
    println("B节点的父亲节点是：${tree.getParent(nodeB)?.data.toString()}")
    println("A节点的父亲节点是：${tree.getParent(nodeA)?.data.toString()}")
    println("G节点的父亲节点是：${tree.getParent(nodeG)?.data.toString()}")
    tree.inserNode(nodeA, "H", true)
    println("B节点的父亲节点是：${tree.getParent(nodeB)?.data.toString()}")
    println("D节点的父亲节点是：${tree.getParent(nodeD)?.data.toString()}")
    tree.remodeNode(nodeB, true)
    tree.preOrder()
}

fun testTree02() {
    val nodeA: BinaryNode<String> = BinaryNode("A")
    val nodeB: BinaryNode<String> = BinaryNode("B")
    val nodeC: BinaryNode<String> = BinaryNode("C")
    val nodeD: BinaryNode<String> = BinaryNode("D")
    val nodeE: BinaryNode<String> = BinaryNode("E")
    val nodeF: BinaryNode<String> = BinaryNode("F")
    val nodeG: BinaryNode<String> = BinaryNode("G")
    /* "ABC^^^DE^^F^^" */
    val tree: BinaryTree<String> =
        BinaryTree(arrayOf("A", "B", "C", "E", "^", "^", "G", "^", "L", "^", "F", "^", "^"))

    println("树是否是空树：${tree.isEmpty}")
    println("树有多少个节点：${tree.count}")
    tree.preOrder()
    tree.inOrder()
    println("F节点的父亲节点是：${tree.getParent(nodeF)?.data.toString()}")
}

/**
 * 二分法查找
 */
fun search(arr: IntArray, finded: Int): Int {
    //开始的范围
    var star = 0
    //结束的范围
    var end: Int = arr.size - 1
    //中间的索引
    var middle = 0
    //如果要查找的数据比最小值小，或者比最大值大，说明数据肯定不在该数组中
    if (finded < arr[star] || finded > arr[end]) {
        return -1
    }
    //当开始区间小于等于 结束区间时，继续循环
    while (star <= end) {
        //根据开始的索引和结束的索引 计算中间的索引
        middle = (star + end) shr 1
        //如果查找的树比中间的数大 说明目标数在大的那边
        if (finded > arr[middle]) {
            star = middle + 1
        } else if (finded < arr[middle]) {
            //如果查找的数比中间数小 说明目标数在小的那边
            end = middle - 1
        } else {
            return middle
        }
    }
    return middle
}
