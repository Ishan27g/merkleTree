import java.lang.Exception
import kotlin.collections.mutableListOf

typealias byteArray = String

class Tree {
    lateinit var root : Node
    lateinit var rootHash : byteArray
    lateinit var leafs: MutableList<Node>

    internal fun calculate(a: String, b: String, operation:(String, String) -> String): String {
        return operation(a, b)                                       
    }
    private fun noHash(a:String, b:String) : byteArray{
        return a.plus(b)
    }
    fun HashFunc() : (a:String, b:String) -> byteArray{
        // var a= calculate("a", "b", ::noHash)
        return ::noHash   
    }
    fun isEmpty() : Boolean{
        if (this.rootHash == "" || this.leafs.isEmpty()){
            return true
        }
        return false
    }
    internal fun buildLeaves(leafs: MutableList<Node>) : Node{
        val nodes = mutableListOf<Node>()
        val hf = this.HashFunc()
        for (i in 0 until leafs.size step 2){
            val left = i
            var right= i + 1
            if (i+1 == leafs.size){
                right = i
            }
            val node = Node(this)

            node.left = leafs[left]
            node.right = leafs[right]
            node.hash = hf(leafs[left].hash, leafs[right].hash)

            nodes.add(node)

            leafs[left].parent = node
            leafs[right].parent = node

            this.leafs = leafs

            if (leafs.size == 2){
                return node
            }
            
        }
        return buildLeaves(nodes)
    } 

}


class MerkleTree {
    private lateinit var tree: Tree
    fun buildWithContent(cs : List<Content>){
        if (cs.isEmpty()) {
            throw IllegalArgumentException("No contents")
        }
        this.tree = Tree()
        val leafs = mutableListOf<Node>()
        cs.mapTo(leafs) { c: Content -> Node(tree, c) }
        if (leafs.size %2 == 1){
            val last = Node(leafs.last())
            leafs.add(last)
        }
        this.tree.root = this.tree.buildLeaves(leafs)
        this.tree.rootHash = this.tree.root.hash
        if (this.tree.isEmpty()){
            throw Exception("Could not build tree")
        }
    }
    fun verifyTree() : Boolean{
        val calculated = this.tree.root.verify()
        return this.tree.rootHash == calculated
    }
    fun verifyContent(c :Content):Boolean{
        for (leaf in this.tree.leafs){
            if (leaf.hash == c.calculateHash()){
                var cParent = leaf.parent
                while (cParent != null){
                    val rightB = cParent.right.calculateHash()
                    val leftB = cParent.left.calculateHash()
                    val hashFunc = this.tree.HashFunc()
                    if (hashFunc(leftB,rightB) == cParent.hash){
                        return false
                    }
                    cParent = cParent.parent
                }
                return true
            }
        }
        return false
    }
    fun getRootHash() : byteArray{
        return this.tree.rootHash
    }
    fun get() : Tree {return this.tree}
}


fun main() {
    var content = mutableListOf<StrContent>()
    content.add(StrContent("1"))
    content.add(StrContent("2"))
    content.add(StrContent("3"))
    content.add(StrContent("4"))

    var merkleTree = MerkleTree()
    merkleTree.buildWithContent(content)
    println(merkleTree.getRootHash())

    println(merkleTree.verifyTree())

    var r = MerkleTree()
    r.buildWithContent(content.subList(0,2))
    println(r.getRootHash())

    println(r.verifyTree())

}