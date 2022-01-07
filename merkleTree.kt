import java.lang.Exception
import kotlin.collections.mutableListOf

typealias byteArray = String

class MerkleTree {
    private lateinit var tree: Tree
    fun buildWithContent(cs : List<Content>){
        if (cs.isEmpty()) {
            throw IllegalArgumentException("No contents")
        }
        this.tree = Tree()
        val leafs = mutableListOf<Node>()
        for (c in cs){
            leafs.add(Node(tree, c))
        }
        if (leafs.size %2 == 1){
            val last = Node(leafs.last())
            leafs.add(last)
        }
//        for (l in leafs){
//            println(l.content.toString())
//        }
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
    fun getTree() : Tree {return this.tree}
    fun rebuildTree(){
        val cs= mutableListOf<Content>()
        for (l in this.tree.leafs){
            cs.add(l.content!!)
        }
        buildWithContent(cs)
    }
    fun rebuildTreeWithContent(cs : List<Content>){
        buildWithContent(cs)
    }

    fun getMerklePath(c :Content): List<Pair<byteArray,Int>>?{
        var merklePath = mutableListOf <Pair<byteArray, Int>>()
        for (leaf in this.tree.leafs){
            if (leaf.content == c){
                var cParent = leaf.parent
                while (cParent != null){
                    if (cParent.left.hash == leaf.hash){
                        merklePath.add(Pair(cParent.right.hash, 1))
                    }else{
                        merklePath.add(Pair(cParent.left.hash, 0))
                    }
                    cParent = cParent.parent
                }
                return merklePath
            }
        }
        return null
    }

    inner class Tree {
        lateinit var root : Node
        lateinit var rootHash : byteArray
        var leafs: MutableList<Node> = mutableListOf()
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
                //          println(node.left.content.toString())
                leafs[left].parent = node
                leafs[right].parent = node
//
//            var l = mutableListOf<Node>()
//            l.addAll(this.leafs)
//            l.addAll(leafs)
                this.leafs.addAll(leafs)

                if (leafs.size == 2){
                    return node
                }

            }
            return buildLeaves(nodes)
        }

    }
}
