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
            if (leaf.content?.equals(c) == true){
                var cParent= leaf.parent
                while (cParent != null){
                    val rightB = cParent.right.getNodeHash()
                    val leftB = cParent.left.getNodeHash()
                    val hashFunc = this.tree.HashFunc()
                    if (hashFunc(leftB,rightB) != cParent.hash){
                        return false
                    }
                    cParent.parent.also { cParent = it }// cParent = cParent.parent
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

    fun getMerklePath(c :Content): List<MerklePath>?{
        var merklePath = mutableListOf <MerklePath>()
        for (i in 0 until this.tree.leafs.size){
            var leaf = this.tree.leafs.get(i)
            if (leaf.content == c){
                var nextLeaf = leaf
                var cParent = leaf.parent
                while (cParent != null){
                    if (cParent.left.hash == nextLeaf.hash){
                        merklePath.add(MerklePath(cParent.right.hash, 1))
                    }else{
                        merklePath.add(MerklePath(cParent.left.hash, 0))
                    }
                    nextLeaf = cParent
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
        internal fun buildLeaves(lf: MutableList<Node>) : Node{
            val nodes = mutableListOf<Node>()
            val hf = this.HashFunc()
            for (left in 0 until lf.size step 2){
                var right= left + 1
                if (left+1 == lf.size){
                    right = left
                }
                val node = Node(this)

                node.left = lf[left]
                node.right = lf[right]
                node.hash = hf(lf[left].hash, lf[right].hash)

                nodes.add(node)

                lf[left].Child(node)
                lf[right].Child(node)

                this.leafs.add(lf[left])
                this.leafs.add(lf[right])

                if (lf.size == 2){
                    return node
                }

            }
            return buildLeaves(nodes)
        }
    }
}

data class MerklePath(val neighbour: byteArray, val index: Int) {}