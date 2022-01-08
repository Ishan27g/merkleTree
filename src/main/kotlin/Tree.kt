
// Single static instance of a Tree per MerkleTree
object Tree {
    lateinit var root : Node
    lateinit var rootHash : byteArray
    var leafs: MutableList<Node> = mutableListOf()

    internal fun calculate(a: String, b: String, operation:(String, String) -> String): String {
        return operation(a, b)
    }
    private fun noHash(a:String, b:String) : byteArray {
        return a.plus(b)
    }
    fun HashFunc() : (a:String, b:String) -> byteArray {
        // var a= calculate("a", "b", ::noHash)
        return ::noHash
    }
    fun isEmpty() : Boolean{
        if (this.rootHash == "" || this.leafs.isEmpty()){
            return true
        }
        return false
    }
    internal fun buildLeaves(lf: MutableList<Node>) : Node {
        val nodes = mutableListOf<Node>()
        val hf = this.HashFunc()
        for (left in 0 until lf.size step 2){
            var right= left + 1
            if (right == lf.size){
                right = left
            }
            val node = Node(this)

            node.left = lf[left]
            node.right = lf[right]
            node.hash = hf(lf[left].hash, lf[right].hash)
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