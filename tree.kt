

import kotlin.collections.listOf
import kotlin.collections.mutableListOf

typealias byteArray String

class Tree {
    lateinit var root : Node
    lateinit var rootHash : String
    lateinit var leafs: MutableList<Node>

    internal fun calculate(a: String, b: String, operation:(String, String) -> String): String {
        return operation(a, b)                                       
    }
    internal fun noHash(a:String, b:String) : byteArray{
        return a + b
    }
    fun HashFunc() : (a:String, b:String) -> byteArray{
        // var a= calculate("a", "b", ::noHash)
        return ::noHash   
    }
    internal fun buildIntermediate(leafs: MutableList<Node>) : Node{
        var nodes = mutableListOf<Node>()
        var hf = this.HashFunc()
        for (i in 0..leafs.size-1 step 2){
            var left = i,
            var right= i + 1
            var node = Node(this)

            node.left = leafs.get(left)
            node.right = leafs.get(right)
            node.hash = hf(leafs.get(left).hash, leafs.get(right).hash)

            nodes.add(node)

            var leftNode = leafs.get(left)
            var rightNode = leafs.get(left)
            leftNode.parent = node
            rightNode.parent = node
            leafs.set(left, leftNode)
            leafs.set(right, rightNode)

            if (leafs.size == 2){
                return node
            }
            
        }
        return buildIntermediate(nodes)
    } 
    fun buildWithContent(cs : List<Content>){
        if (cs.isEmpty()) {
            return
        }
        var leafs = mutableListOf<Node>()
        for (c in cs){
            var node = Node(this)
            node.content = c
            node.hash = c.calculateHash()
            node.leaf = true
            leafs.add(node)
        }
        if (leafs.size %2 == 1){
            val last = leafs.last()
            last.dup = true
            leafs.add(last)
        }
        this.root = buildIntermediate(leafs) : 
    }
}

class Node (_tree:Tree){
    var tree: Tree
    lateinit var parent: Node
    lateinit var left:Node
    lateinit var right: Node
    var leaf: Boolean = false
    var dup: Boolean = false
    lateinit var hash: byteArray
    lateinit var content : Content
    init {
        this.tree = _tree
    }
}

sealed interface Content {
    fun calculateHash(): byteArray
    fun equals(to:strContent) : Boolean
}

sealed class MerkleTree {
    fun NewTree(c : MutableList<Content>): Tree{
        var hashFunc = Tree().
    }
}

class strContent(_data : String) : Content {
    val data: String
    init {
        this.data = _data
    }
    override fun calculateHash() : byteArray{
        return this.data
    }
    override fun equals(to: strContent): Boolean{
        return this.data === to.data
    }
}

fun main() {
    var content = mutableListOf<strContent>()
    content.add(strContent("1"))
    content.add(strContent("2"))
    content.add(strContent("3"))
    content.add(strContent("4"))

    var merkleTree = MerkleTree()