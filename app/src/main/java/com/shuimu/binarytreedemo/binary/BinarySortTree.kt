package com.shuimu.binarytreedemo.binary

import java.util.*

/**
 * @author shuimu{lwp}
 * @time 2019/8/14  11:59
 * @desc 二叉排序树
 *
 * 二叉排序树的要满足哪些特点之后才能称为二叉排序树呢?
 * (1)因为要给每个节点排序，就要求每个节点都是能够进行比较的。那么节点之间究竟比较什么呢?比较的是哪个节点更大。
 * 两种方式实现节点的大小比较：
 * a.给节点增加字段,例如var sort:Int；通过给每个节点的sort字段赋予数值，比较数值的大小来对节点排序
 * b.不需要给节点增加字段,而是要求节点所储存的业务数据data本身是可比较的，例如业务数据本身实现了java SDK中的Comparable接口
 * 通过Comparable接口的compareTo()方法进行比较然后排序。
 * (2)每个节点能够比较大小之后，要求每个节点(包括根节点)的左节点比它自身小，每个节点的右节点比它自身大
 * 满足上面的条件，就是一个二叉排序树。
 *
 * 本类演示，要求业务数据自身要实现Comparable接口，业务数据自身是能够进行大小比较 的情况下，实现的二叉排序树。
 */
class BinarySortTree<T : Comparable<T>>() {
    /**
     * 不管是满二叉树也好，二叉排序树也好，只要是树，都会必定有一个根节点
     */
    var root: BinaryNode<T>? = null

    /**
     * 插入元素，并且对元素进行排序.
     */
    fun inser(other: T) {
        //判断根元素是否为null，为空说明，二叉排序树当前是一棵空树，则直接创建树根节点即可
        if (root != null) {
            //表示"当前正在被循环处理的节点"
            var tempNode: BinaryNode<T>? = root
            //表示"当前正在被循环处理的节点" 在进行比较之后所得的结果
            var comparaTo: Int = 0
            //记录本轮"当前正在被循环处理的节点"。
            var parent: BinaryNode<T>? = null
            //表示"当前正在被循环处理的节点"不为null就继续循环
            while (tempNode != null) {
                /*
                * 记录当前这轮的tempNode节点，如果下一轮判断不满足条件tempNode!=null，即temp=null，循环就会退出，
                * 那时循环退出了,那么就需要从parent这里来获取刚刚那轮的tempNode值。
                * */
                parent = tempNode
                //插入的元素是应该在"当前正在被循环处理的节点"左子树里面还是右子树里面
                comparaTo = other.compareTo(tempNode.data)
                if (comparaTo == 1) {
                    //说明 应该在右子树里面，获取右子树的节点，进行下一轮
                    tempNode = tempNode.right
                } else if (comparaTo == -1) {
                    //说明应该在左子树里面，获取左子树的节点，进行下一轮
                    tempNode = tempNode.left
                } else {
                    break
                }
                //当获取的左节点或右节点为null，说明找到了运算要存放的位置
            }
            //因为在最后一轮temNode !=null中已经判断了comparaTo,因此这里复用判断结果，当然啦，你也可以为了确保安全，在判断一次comparaTo也行的
            if (comparaTo == 1) {
                parent!!.right = BinaryNode(other)
            } else if (comparaTo == -1) {
                parent!!.left = BinaryNode(other)
            } else {
                parent!!.data = other
            }
        } else {
            //说明二叉排序树当前是一棵空树，直接创建树根节点
            root = BinaryNode(other)
        }
    }

    fun preOrder() {
        println("前序遍历开始啦")
        if (root != null) {
            val cache: Stack<BinaryNode<T>> = Stack()
            var temp: BinaryNode<T>? = root
            while (temp != null) {
                println(temp.data.toString())
                if (temp.right != null) {
                    cache.push(temp.right)
                }
                temp =
                    if (temp.left != null) temp.left else if (cache.size > 0) cache.pop() else null
            }
        }
        println("前序遍历结束啦")
    }

    fun deleteNode(data: T): BinaryNode<T>? {
        if (root == null) {
            return null
        }
        //表示"当前要循环处理的节点"
        var tempNode: BinaryNode<T>? = root
        //记录一下"当前要循环处理的节点"与数据data比较的结果
        var comparaTo: Int = 0
        /*上一轮循环结束之后,下一轮是拿着上一轮节点的左孩子或右孩子进行循环的，
        因此只要在循环的最后记录一下本论的"循环处理的节点",下一轮开始之后，本论的"循环处理的节点"就是下一轮的父节点了*/
        var parent: BinaryNode<T> = root!!
        while (tempNode != null) {
            //首先的目的是，先找到"数据所代表的节点",通过comparaTo来判断是不是"数据所代表的节点"
            //"当前要循环处理的节点"与 数据所代表的节点的比较有三种结果有：相同，比它大，比它小。
            comparaTo = data.compareTo(tempNode.data)
            if (comparaTo == 1) {
                parent = tempNode
                //数据data所代表的节点比"当前要循环处理的节点"大，则说明数据所代表的节点在"当前要循环处理的节点"的右边
                tempNode = tempNode.right
            } else if (comparaTo == -1) {
                //数据data所代表的节点比"当前要循环处理的节点"小，则说明数据所代表的节点在"当前要循环处理的节点"的左边
                parent = tempNode
                tempNode = tempNode.left
            } else if (comparaTo == 0) {
                //说明找到了"数据所代表的节点"。
                //判断节点的左右子树是否为null,缓存一下判断情况
                val rightIsNotEmpty = tempNode.right != null
                val leftIsNotEmpty = tempNode.left != null
                if (rightIsNotEmpty && leftIsNotEmpty) {
                    //情况1：当前要删除的节点同时有左子树和右子树
                    //1:获取后继节点，并先把后继节点从其父节点中删除
                    val flowUpNode = commentLowup02(tempNode)
                    //后继节点肯定不为null。因为能进来这个判断就说明了最起码“当前要删除的节点”的右节点是不为null的。
                    tempNode.data = flowUpNode!!.data
                } else if (!rightIsNotEmpty && !leftIsNotEmpty) {
                    //情况2：当前要删除的节点左右子树都为null
                    if (parent.left == tempNode) parent.left = null else parent.right = null
                } else {
                    val temp = if (rightIsNotEmpty) tempNode.right else tempNode.left
                    if (parent.left == tempNode) {
                        parent.left = temp
                    } else {
                        parent.right = temp

                    }
                }
                break
            }
        }

        return tempNode
    }

    /**
     * 查找某节点的后继节点
     */
    fun flowNode(data: T, node: BinaryNode<T>?): BinaryNode<T>? {
        //node为null，表示要遍历的树都是空树了，这还查找什么？直接返回null。
        if (node == null) {
            return null
        }
        //表示"当前要循环处理的节点"
        var tempNode: BinaryNode<T>? = node
        //记录一下"当前要循环处理的节点"与数据data比较的结果
        var comparaTo: Int = 0
        //创建一个标签，用于内部循环结束时，能够直接退出双重循环
        first@ while (tempNode != null) {
            //首先的目的是，先找到"数据所代表的节点",通过comparaTo来判断是不是"数据所代表的节点"
            //"当前要循环处理的节点"与 数据所代表的节点的比较有三种结果有：相同，比它大，比它小。
            comparaTo = data.compareTo(tempNode.data)
            if (comparaTo == 1) {
                //数据data所代表的节点比"当前要循环处理的节点"大，则说明数据所代表的节点在"当前要循环处理的节点"的右边
                tempNode = tempNode.right
            } else if (comparaTo == -1) {
                //数据data所代表的节点比"当前要循环处理的节点"小，则说明数据所代表的节点在"当前要循环处理的节点"的左边
                tempNode = tempNode.left
            } else if (comparaTo == 0 && tempNode.right != null) {
                //说明找到了"数据所代表的节点"。之后就要查找比“'数据所代表的节点'大但又是比它大的那堆节点中最小的节点”，即查找>="数据所代表的节点"的节点。
                //比"数据所代表的节点"大的节点放在节点的右子树上的，因此从右子树开始查找。
                tempNode = tempNode.right
                while (tempNode != null) {
                    //循环右子树的左节点，当某个节点的左节点为null，说明该节点就是满足"恰好比'数据所代表的节点'大，但又是比它大的那堆节点中最小的那个"
                    if (tempNode.left != null) {
                        tempNode = tempNode.left
                    } else {
                        //找到了之后就退出双重循环
                        break@first
                    }
                }
                //当然啦，这里的方法我们可以抽取处理，形成另一个方法专门进行查找"节点的后继节点"
                /*  tempNode = commentLowup01(tempNode)
                  break@first*/
            } else {
                break
            }
        }
        return tempNode
    }
}

/**
 * 查找某个节点的后继节点
 */
inline fun <T> commentLowup01(node: BinaryNode<T>): BinaryNode<T>? {
    //查找后继节点，即查找>=node的节点的节点。对于二叉排序树来说肯定是从右节点开始查找。
    /*
      因为查找的是node节点的后继节点，因此如果node没有right节点，
      那么node节点的后继节点就是node本身,所以当node.right==null时，返回node自身
      */
    if (node.right != null) {
        //从右子树开始，查找右子树的左子节点，左子节点的左子节点...一直循环，直到某个节点的左子节点为null，说明找到了
        var tempNode: BinaryNode<T>? = node.right
        while (tempNode != null) {
            if (tempNode.left != null) {
                //拿出左节点，进行下一轮判断
                tempNode = tempNode.left
            } else {
                break
            }
        }
        return tempNode
    }
    return node
}

/**
 * 查找后继节点时直接把后继节点从其父节点中移除，然后返回后继节点。
 */
inline fun <T> commentLowup02(node: BinaryNode<T>): BinaryNode<T>? {
    /*
    * 在删除节点的过程中进行的后继节点查找，被找到的后继节点面对的命运只有：拿出后继节点的值，然后删除后继节点。
    * 因此我们可以在查找后继节点的过程中，顺便找到后继节点的父节点，这是很容易的事情，只需要用一个parent字段记录节点的父节点即可。
    * 那么当我们找到后继节点和后继节点的父节点时，我们可以直接把后继节点从父节点中删除，然后把后继节点的值返回去，让他们替换拿后继节点的值替换到删除节点上，
    * 这样删除节点的操作就能够完成了。
    * */
    //查找后继节点，即查找>=node的节点的节点。对于二叉排序树来说肯定是从右节点开始查找。
    if (node.right != null) {
        //node.right!=null时，记录下父节点。
        var parent: BinaryNode<T>? = node
        //从右子树开始，查找右子树的左子节点，左子节点的左子节点...一直循环，直到某个节点的左子节点为null，说明找到了
        var tempNode: BinaryNode<T>? = node.right
        while (tempNode != null) {
            if (tempNode.left != null) {
                //记录父节点
                parent = tempNode
                //拿出左节点，进行下一轮判断
                tempNode = tempNode.left
            } else {
                break
            }
        }
        //直接进行后继节点的删除。
        if (tempNode == parent!!.left) {
            //删除后继节点时，如果后继节点有右节,那么要把右节点接在父节点的左节点上
            parent.left = if (tempNode!!.right != null) tempNode.right else null
        } else {
            //删除后继节点时，如果后继节点有右节,那么要把右节点接在父节点的右节点上
            parent.right = if (tempNode!!.right != null) tempNode.right else null
        }
        return tempNode
    }
    //node.right==null说明当前节点node它自己就是自己的后继节点，或者说当前节点就没有后继节点，你们自己看着处理。
    return null
}