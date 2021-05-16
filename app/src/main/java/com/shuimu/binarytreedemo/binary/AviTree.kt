package com.shuimu.binarytreedemo.binary

import java.util.*
import kotlin.math.abs
import kotlin.math.max

/**
 * @author shuimu{lwp}
 * @time 2019/8/14  11:59
 * @desc
 */
class AviTree<T : Comparable<T>> {
    /**
     * 只要是二叉树肯定会有一个根节点。
     */

    var root: AviNode<T>? = null

    fun inser(data: T) {
        //root=null时，说明当前是一棵空树，那么直接创建根节点即可
        if (root != null) {
            //表示"当前被循环处理的节点"
            var tempNode: AviNode<T>? = root
            //用于缓存在查找"数据data的存放位置时，所经过的节点。"
            var cache: Stack<AviNode<T>> = Stack()
            //用于缓存一下比较结果
            var compareTo: Int
            //"当前被循环处理的节点"不等于null时才会继续循环
            while (tempNode != null) {
                //比较的结果能够确定“数据data应该是存放在‘当前被循环处理的节点’左节点上 还是右节点上”
                compareTo = data.compareTo(tempNode.data)
                if (compareTo == 1) {
                    //说明 数据应该在"当前被循环处理的节点"的右子树里面
                    //储存一下插入数据时所经过的路径
                    cache.push(tempNode)
                    //如果"当前被循环处理的节点"的右节点为null，那么直接把数据放这里即可
                    if (tempNode.right == null) {
                        tempNode.right = AviNode(data, height = 0)
                        /*虽然数据被放在"当前被循环处理的节点"的右节点上,但是如果"当前被循环处理的节点"的左节点不为null，
                        那么新增加的右节点肯定不会改变树的高度,那么后面的循环处理节点高度就不需要执行。
                        这里通过清空Stack对象的方式让处理节点高度的循环不执行*/
                        if (tempNode.left != null) {
                            cache = Stack()//之所以选择创建对象来清空数据因为Stack的clear方法是循环移除,重新创建能避免循环。
                        }
                        break
                    } else {
                        //"当前被循环处理的节点"的右节点不为null，拿出右节点继续判断"数据data"的存放位置
                        tempNode = tempNode.right
                    }
                } else if (compareTo == -1) {
                    //说明 数据应该在"当前被循环处理的节点"的左子树里面
                    //储存一下插入数据时所经过的路径
                    cache.push(tempNode)
                    //如果"当前被循环处理的节点"的左节点为null，那么直接把数据放这里即可
                    if (tempNode.left == null) {
                        tempNode.left = AviNode(data, height = 0)
                        /*虽然数据被放在"当前被循环处理的节点"的左节点上,但是如果"当前被循环处理的节点"的右节点不为null，
                       那么新增加的左节点肯定不会改变树的高度,那么后面的循环处理节点高度就不需要执行。
                       这里通过清空Stack对象的方式让处理节点高度的循环不执行*/
                        if (tempNode.right != null) {
                            cache = Stack()//之所以选择创建对象来清空数据因为Stack的clear方法是循环移除,重新创建能避免循环。
                        }
                        break
                    } else {
                        //"当前被循环处理的节点"的左节点不为null，拿出左节点继续判断"数据data"的存放位置
                        tempNode = tempNode.left
                    }
                } else {
                    //如果插入的数据已经存在,那么直接替换退出循环。
                    tempNode.data = data
                    //而且因为是替换数据，树的结构没发生改变，那边后面的循环设置树的高度就没必要执行了。
                    cache = Stack()//之所以选择创建对象来清空数据因为Stack的clear方法是循环移除,重新创建能避免循环。
                    break
                }
            }
            //上面完成对插入的“数据data”进行了处理，而新增节点后,新增节点所“经过的节点”的高度可能会发生变化
            //那么接下来就是循环处理节点的高度了。
            var rightIsNotEmpty: Boolean
            var leftIsNotEmpty: Boolean
            //记录平衡因子
            var balanceRia: Int = 0
            //当缓存里面存在插入数据的访问路径节点时，就说明这些节点的高度变了。需要重新计算高度
            /*
             * 计算树中每个节点的高度，方法是：从树底的叶子节点开始计算，树底的叶子节点高度肯定是0，然后计算叶子节点的父节点。
             * 叶子节点的父节点的父节点....依次类推，一直向上追溯，这样才能计算出来
             * 因此就必须要求cache中的元素是存放时是从上(根节点)到下(叶子节点)的顺序存放节点的，取节点时是按照从下(叶子节点)到上(根节点)的顺序取出的
             * cache是栈数据结构，本身就支持先进后出，后进先出的特点。
             */
            while (cache.size > 0) {
                /*
                 * 因为cache里面储存的"查找数据data的存放位置时，所经过的节点",
                 * 因此它是按照访问顺序存放的，即按照上(根节点)到下(叶子节点)顺序存放的。
                 * 因此cache.pop()取出的顶部元素的顺序,就符合我们要求，从下(叶子节点)到上(根节点)的顺序取出
                 */
                //这里复用tempNode变量
                tempNode = cache.pop()
                //先判断节点的左节点和右节点是否为null，记录一下结果，供后面使用。
                rightIsNotEmpty = tempNode.right != null
                leftIsNotEmpty = tempNode.left != null
                /*其实这句判断当左右节点都为null时,跳过进行下一个节点；这个判断是不需要的，可以删除的。
                * 因为cache里面储存的都是"访问数据data所代表的节点所需要经过的节点"
                * 这些节点的左节点或者右节点肯定有一个不为null的
                * 但是为了学习和笔记，把这个条件写上，方便日后复习时逻辑能通顺。
                * */
                if (!rightIsNotEmpty && !leftIsNotEmpty) continue
                /*
                * 这里的判断是：因为假设子节点的高度为h那么它的父节点的高度一定是：h+1
                * 父节点的父节点高度肯定是：父节点的高度+1
                * 依次类推...循环处理
                * */
                //当节点的左右两个节点都不为null时,就比较左子树高还是右子树高，取最大值进行+1就是本节点的高度
                if (rightIsNotEmpty && leftIsNotEmpty) {
                    tempNode.height = 1 + max(tempNode.right!!.height, tempNode.left!!.height)
                    //当节点存在左右子树时，计算左右子树的高度差。高度差>1就是不平衡了
                    //原本平衡因子的计算代码是这样的：balanceRia = Math.abs((1+tempNode.right!!.height) - (1+tempNode.left!!.height)) 其中+1-1相互抵消
                    balanceRia = abs(tempNode.right!!.height - tempNode.left!!.height)
                } else if (rightIsNotEmpty) {
                    //当节点只有右节点不为null时，当前节点的高度就是：右节点的高度+1
                    tempNode.height = 1 + tempNode.right!!.height
                    //只有一个子树时，那么当前节点的高度超过1就是不平衡了
                    balanceRia = tempNode.height
                } else if (leftIsNotEmpty) {
                    //当节点只有左节点不为null时，拿左节点的高度+1
                    tempNode.height = 1 + tempNode.left!!.height
                    //只有一个子树时，那么当前节点的高度超过1就是不平衡了
                    balanceRia = tempNode.height
                }
                //平衡因子>1 代表最小不平衡子树
                if (balanceRia > 1) {
                    if (rightIsNotEmpty) {
                        if (data.compareTo(tempNode.right!!.data) == 1) {
                            //条件满足，表示数据比最小不平衡子树的右孩子节点大，数据被插入在"最小不平衡子树的右孩子的右子树上"
                            //根据公式：插入的元素在最小不平衡子树的右孩子的右子树上时，只需要对最小不平衡子树进行一次左旋即可恢复平衡。
                            println("对最小不平衡子树${tempNode.data}进行一次左旋")
                            leftRotate(tempNode)
                        } else {
                            //说明数据比最小不平衡子树的右孩子节点小，数据被插入在"最小不平衡子树的右孩子的左子树上"
                            //根据公式：插入的元素在最小不平衡子树的右孩子的左子树上时，需要先对"最小不平衡子树的右孩子"进行右旋,然后对"最小不平衡子树"进行一次左旋即可恢复平衡。
                            println("对最小不平衡子树的右孩子右旋，然后对最小不平衡子树进行左旋")
                            rightRotate(tempNode.right!!)
                            leftRotate(tempNode)
                        }
                    } else {
                        if (data.compareTo(tempNode.left!!.data) == 1) {
                            //条件满足，说明“数据所代表的节点”比"最小不平衡子树的左节点大",“数据所代表的节点”被插入在"最小不平衡子树的左节点的右子树上"。
                            //根据公式：插入的元素在最小不平衡子树的左孩子的右子树上时，需要先对“最小不平衡子树的左孩子”进行左旋，然后对“最小不平衡子树”进行右旋。
                            println("对最小不平衡子树的左孩子左旋，然后对最小不平衡子树进行右旋")
                            leftRotate(tempNode.left!!)
                            rightRotate(tempNode)
                        } else {
                            //说明“数据所代表的节点”比"最小不平衡子树的左节点小",“数据所代表的节点”被插入在"最小不平衡子树的左节点的左子树上",
                            //根据公式：插入的元素在最小不平衡子树的左孩子的左子树上时，只需要对最小不平衡子树进行一次右旋即可恢复平衡。
                            println("对最小不平衡子树${tempNode.data}进行一次右旋")
                            rightRotate(tempNode)
                        }
                    }
                }
            }
        } else {
            //如果root==null，表示当前是一棵空树，那么直接创建根节点即可。
            root = AviNode(data, height = 0)
        }
    }

    /**
     * 左旋
     * */
    fun leftRotate(node: AviNode<T>) {
        //因为是进行左旋，那么右节点肯定是不能为null的。
        if (node.right != null) {
            /*
             * 左旋的步骤是：
             * (1)右节点带着它的右孩子上浮为根节点
             * (2)根节点要带着它的左节点一起下沉为右节点的左节点，
             * (3)如果右节点的存在左节点的话，那么右节点的左节点就成为根节点的右节点。
             */
            //取出根节点的右节点，保存记录一下
            val oldNode: AviNode<T> = node.right!!
            /*
            * 根据左旋的规则:
            * 根节点下沉时，根节点要带着它的左节点一起下沉为右节点的左节点
            * 同时：右节点如果有左节点,那么右节点的左节点要作为根节点的右节点存在
            * 因此。这里创建的新根节点nowNode，新根节点nowNode的左节点关系与原根节点node的左节点一致；
            * 新根节点nowNode的右节点为oldNode右节点的左节点。
            * */
            val newNode: AviNode<T> = AviNode(node.data, node.left, oldNode.left, node.height)
            /*
            * 根据左旋规则:
            * 右节点带着它的右孩子上浮为根节点
            * 因此把右节点oldNode的data值赋值给根节点node，这样根节点node就成为的右节点，但是根节点node还没有继承右节点oldNode的右孩子关系
            * 所以把右节点oldNode的右孩子赋值给根节点node的右节点
            */
            node.data = oldNode.data
            node.right = oldNode.right
            /*
            * 根据左旋规则：最后根节点作为右节点的左节点存在。
            * 因此把新根节点newNode挂在右节点node的左节点下即可
            * */
            node.left = newNode
            //最后原来的右节点清空一下关系。不清空也行,因为其父节点已经断开了对它的持有，jvm会回收它的，只是为了学习和笔记方便写一下
            oldNode.left = null
            oldNode.right = null
            //旋转完成之后就要处理一下节点高度。
            //节点的高度本质上就是它有多少层子节点。假设一个节点有N层子节点，那么它高度就是N。
            //假如节点A一个叶子节点,那节点A的高度就是1，把节点A移动到其他节点下，作为其他节点的孩子节点，那其实节点A还是高度为1.
            //同理左旋的情况下，原先根节点下沉为其右节点的左子节点，原先挂在根节点下的左节点一层也没少。只是根节点自己的右孩子少了两层子节点而已
            //所以只需要对下沉的根节点的高度-2即可
            //这里还有一个小问题就是，为什么根节点是少了两层子节点，
            // 其实是这样理解的：根节点下沉到与其右节点同一层高度时算一层，然后下沉到与其右节点的子节点的高度时又算一层，因此是下沉了两层
            newNode.height -= 2
        }
    }

    /**
     * 右旋
     * */
    fun rightRotate(node: AviNode<T>) {
        //因为是进行右旋，那么左节点肯定是不能为null的。
        if (node.left != null) {
            /*
             * 右旋的步骤是：
             * (1)左节点带着它的左孩子上浮为根节点
             * (2)根节点要带着它的右孩子一起下沉为左节点的右节点，
             * (3)如果左节点的存在右孩子节点的话，那么左节点的右孩子节点就成为根节点的左节点。
             */
            //取出根节点的左节点，保存记录一下
            val oldNode: AviNode<T> = node.left!!
            /*
             * 新建一个根节点。因为根据右旋的规则:
             * 根节点是带着它的右孩子一起下沉为左节点的右节点的，因此新创建的根节点对象要保留右孩子的关系
             * 同时，左节点如果存在右孩子节点的话,是作为根节点的左节点存在
             * 因此新建的根节点newNode,newNode.left是左节点的右孩子,newNode.right是原先根节点的右孩子。
             */
            val newNode: AviNode<T> = AviNode(node.data, oldNode.right, node.right, node.height)
            /*
             * 根据右旋规则：
             * 左节点带着它的左孩子上浮为根节点。
             * 因此我们把左孩子的值赋值给根节点node，这样根节点node就成为了左节点，但这时根节点node还没继承左节点oldNode的左孩子关系
             * 所以要把左节点oldNode的左孩子赋值给根节点node的左节点，这样根节点node就变成了“左节点,而且还是带着左节点的左孩子”
             * 这样： 左节点带着它的左孩子上浮为根节点。就完成了。
             */
            node.data = oldNode.data
            node.left = oldNode.left
            /*
             * 最后根据右旋规则：根节点作为左节点的右节点存在。
             * 因此把新根节点newNode挂在左节点node的右节点下即可
             * */
            node.right = newNode
            //右旋操作完成之后，就要重新设置节点的高度。
            //只有根节点的左节点少了2层子节点而已，其他节点的高度不变。
            newNode.height -= 2
        }
    }
}