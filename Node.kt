
class Node (private var tree: MerkleTree.Tree){
    private var leaf: Boolean = false
    private var dup: Boolean = false
    var content : Content? = null
    var parent: Node? = null
    lateinit var left:Node
    lateinit var right: Node
    lateinit var hash: byteArray

    constructor(_tree:MerkleTree.Tree, _content:Content?) : this(_tree){
        if (_content != null) {
            this.hash = _content.calculateHash()
        }
        this.content = _content
        this.leaf = true
    }
    constructor(_node:Node) : this(_node.tree, _node.content) {
        this.dup = true
    }

    fun verify() : byteArray{
        if (this.leaf){
            return this.content!!.calculateHash()
        }
        return calculateHash()
    }

    fun calculateHash(): byteArray {
        val l = this.left.verify()
        val r = this.right.verify()
        val hashFunc = this.tree.HashFunc()

        return hashFunc(l, r)
    }
}
