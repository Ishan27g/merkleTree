
// Single static instance of a Tree per MerkleTree
object Tree {
    lateinit var root : Node
    lateinit var rootHash : byteArray
    var leafs: MutableList<Node> = mutableListOf()

    private fun concatHash(a:byteArray, b:byteArray) : byteArray {
        return a.plus(b)
    }
    fun ConcatHash() : (a:byteArray, b:byteArray) -> byteArray {
        return ::concatHash
    }
    fun isEmpty() : Boolean{
        return this.rootHash == "" || this.leafs.isEmpty()
    }
    internal fun buildLeaves(lf: MutableList<Node>) : Node {
        val nodes = mutableListOf<Node>()
        for (left in 0 until lf.size step 2){
            var right= left + 1
            if (right == lf.size){
                right = left
            }
            val node = Node(this)

            node.left = lf[left]
            node.right = lf[right]
            node.hash = this.ConcatHash()(lf[left].hash, lf[right].hash)
            nodes.add(node)

            lf[left].child(node)
            lf[right].child(node)
            this.leafs.add(lf[left])
            this.leafs.add(lf[right])

            if (lf.size == 2){
                return node
            }
        }
        return buildLeaves(nodes)
    }
}