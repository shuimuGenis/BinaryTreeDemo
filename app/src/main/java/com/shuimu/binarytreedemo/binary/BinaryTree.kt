package com.shuimu.binarytreedemo.binary

import java.util.*

/**
 * @author shuimu{lwp}
 * @time 2019/8/14  11:59
 * @desc
 */
class BinaryTree<T>() : IBinaryTree<T> {
    /**
     * 一棵树肯定会有一个根节点。默认为null。表示当前的树是一个空树
     */
    var root: BinaryNode<T>? = null

    /**
     * 判断一个树是否是空树，根据二叉树定律：树是N个节点的有限集合，当N=0是代表一个空树。
     * 代码上判断是否是空树，只有根节点是不是null就行了
     */
    override val isEmpty: Boolean = root == null

    /**
     * 二叉树的所有方法都是与递归有关。我们通常 都是用递归的方式获取二叉树的节点数量。当然也可以通过非递归的方式
     */
    override val count: Int
        get() = count02(root)

    /**
     * 二叉树计算节点的数量，是通过递归来进行计算的。以最简单的 高为2层的树来写递归循环即可
     * 思路就是：当前的节点为 1 + 当前节点的左子树数量+ 当前节点的右子树数量
     */
    private fun count(root: BinaryNode<T>?): Int =
        if (root == null) 0 else 1 + count(root.left) + count(root.right)

    private fun count02(root: BinaryNode<T>?): Int {
        //根为null直接返回0
        if (root == null) return 0
        val result = Stack<BinaryNode<T>>()
        //temp表示：正在被循环的元素
        var temp: BinaryNode<T>? = root
        //根不为null，则节点数直接先等于1
        var index = 1
        //判断当前要循环的节点不为null，以及linkeList中没有等待循环的元素时，循环结束，
        while (temp != null) {
            //当前被循环的节点的右节点是否为null
            if (temp.right != null) {
                //它的右节点不等于null，直接index+1把这右节点计算进去
                index += 1
                //然后把右节点放入“带循环集合中”，等待后面循环计算这个右节点的左右两边的节点数。
                //因为节点是由左右两边子节点的，我们这个循环是先计算左边节点的数量，右边节点就先缓存起来，后面再计算
                result.push(temp.right)
            }
            //当前被循环的节点的左节点是否为null
            if (temp.left != null) {
                //如果它的左节点不为null，直接index+1把这左节点计算进去
                index += 1
                //然后把它的左节点放入下一轮要循环的节点中。下一轮就会开始计算这个左节点
                temp = temp.left
            } else {
                //进入这里就代表，当前被循环的节点temp的左节点是null，而且前面已经判断过当前节点temp的右节点不为null的话
                //会先被存放入“待循环集合中”，那么现在从集合中取出的就顶部元素有两种情况:
                // 1.取出的是本次循环中刚刚存放进去的当前节点的右节点。2取出之前其他节点中的被缓存的右节点
                if (result.size > 0) {
                    temp = result.pop()
                } else {
                    temp = null
                }
            }
        }
        return index
    }

    constructor(arr: Array<T>?) : this() {
        root = createTree02(arr)
    }

    /**
     * 通过递归的反射创建，难度上 肯定是非递归更加难，但是非递归的好处就是效率高，递归有很大的方法栈调用开销
     */
    var index = 0
    private fun createTree02(arr: Array<T>?): BinaryNode<T>? {
        if (arr == null || arr.isEmpty()) {
            return null
        }
        var node: BinaryNode<T>? = null
        if (index < arr.size) {
            val temp = arr[index]
            index += 1
            if (temp != "^") {
                node = BinaryNode(temp)
                node.left = createTree02(arr)
                node.right = createTree02(arr)
            }
        }
        return node
    }

    /**
     * 前序遍历的方式创建树.非递归方式.非递归的好处就是效率高，递归方式有很大的方法栈调用开销
     */
    private fun createTree(arr: Array<T>?): BinaryNode<T>? {
        if (arr == null || arr.isEmpty()) {
            return null
        }
        val list: LinkedList<BinaryNode<T>> = LinkedList()
        var root: BinaryNode<T>? = null
        var temp: BinaryNode<T>?
        var isLeft: Boolean = true
        for ((index, value) in arr.withIndex()) {
            if (index == 0) {
                root = BinaryNode(value)
                list.addFirst(root)
            } else if (value != "^") {
                temp = BinaryNode(value)
                if (isLeft) {
                    list.first?.left = temp
                } else {
                    list.first?.right = temp
                    isLeft = true
                }
                list.addFirst(temp)
            } else if (value == "^") {
                if (isLeft) {
                    list.first.left = null
                    isLeft = !isLeft
                } else {
                    list.first.right = null
                    list.removeFirst()
                }
            }
        }
        return root
    }

    /**
     * 前根遍历
     */
    override fun preOrder() {
        println("前根遍历开始")
        preOrder(root)
        println("前根遍历结束")
    }

    private fun preOrder(root: BinaryNode<T>?) {
        root?.also {
            println("${it.data}")
            preOrder(it.left)
            preOrder(it.right)
        }
    }

    override fun inOrder() {
        println("中根遍历开始")
        inOrder(root)
        println("中根遍历结束")
    }

    private fun inOrder(root: BinaryNode<T>?) {
        root?.also {
            inOrder(root.left)
            println(root.data.toString())
            inOrder(root.right)
        }
    }

    override fun postOrder() {
        println("后根遍历开始")
        postOrder(root)
        println("后根遍历结束")
    }

    private fun postOrder(root: BinaryNode<T>?) {
        if (root != null) {
            postOrder(root.left)
            postOrder(root.right)
            println(root.data.toString())
        }
    }

    override fun getParent(node: BinaryNode<T>?): BinaryNode<T>? {
        if (node != null) {
            return getParent02(find = node, node = root) as? BinaryNode<T>
        }
        return null
    }

    /**
     * 另一个种方式是 所有情况都在父节点处理，父节点主动去判断字节的的情况，这样少了一次方法调用，或者说少一层递归
     */
    private fun getParent(find: BinaryNode<T>?, node: BinaryNode<T>?): BinaryNode<T>? {
        /*find，node都为null,自然啥也不做，然后null
        * 如果find ==node 说明，当前node是根元素,find也是根元素，查找根元素的父亲自然就是返回null。
        * 当然真正的理解是：当前node节点下所有的数据中是不会存在当前node的父节点,因为这些数据都是以当前node节点为根,挂载在当前node节点下面的。
        * 只有包含当前node节点的树例如nodeA,只有从nodeA开始查找才能查找到当前node节点的父节点nodeA,直接从node节点开始查找肯定查不到nodeA。
        * */
        if (find == null || node == null || find == node) {
            return null
        }
        if (find == node.left || find == node.right) {
            return node
        }
        var temp = getParent(find, node.left)
        if (temp == null) {
            temp = getParent(find, node.right)
        }
        return temp
    }

    /**
     * 非递归方式实现 查找父节点
     */
    private fun getParent02(find: BinaryNode<T>?, node: BinaryNode<T>?): BinaryNode<T>? {
        if (find == null || node == null || find == node) {
            return null
        }
        val result = Stack<BinaryNode<T>>()
        var temp: BinaryNode<T>? = node
        while (temp != null) {
            if (temp.left == find || temp.right == find) {
                break
            }
            if (temp.right != null) {
                result.push(temp.right)
            }
            if (temp.left != null) {
                temp = temp.left
            } else {
                temp = if (result.size > 0) result.pop() else null
            }
        }
        return temp
    }

    /**
     * 该方法表示，在参数node节点下插入一个新的节点，isLeft表示新插入的节点是参数node节点的左子树还是右子树
     */
    override fun inserNode(node: BinaryNode<T>, data: T, isLeft: Boolean): BinaryNode<T>? {
        if (node == null) return null
        val temp: BinaryNode<T>?
        if (isLeft) {
            temp = node.left
            node.left = BinaryNode(data, temp, null)
        } else {
            temp = node.right
            node.right = BinaryNode(data, null, temp)
        }
        return temp
    }

    /**
     * 该方法表示：参数node节点的左右子树
     */
    override fun remodeNode(node: BinaryNode<T>?, isLeft: Boolean): BinaryNode<T>? {
        if (isLeft) node?.left = null else node?.right = null
        return node
    }

    override fun removeAll() {
        root = null
    }
}