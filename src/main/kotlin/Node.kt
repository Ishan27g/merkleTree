
interface INode {
    var leaf: Boolean
    var dup: Boolean
    var content : Content?
    var parent: Node?
    fun getNodeHash() : byteArray
    fun verify(): byteArray
    fun child(of: Node)
}

class Node (private var tree: Tree) : INode {
    override var leaf: Boolean = false
    override var dup: Boolean = false
    override var content : Content? = null
    override var parent: Node? = null

    lateinit var left:Node
    lateinit var right: Node
    lateinit var hash: byteArray

    constructor(_tree: Tree, _content:Content?) : this(_tree){
        if (_content != null) {
            this.hash = _content.calculateHash()
        }
        this.content = _content
        this.leaf = true
    }
    constructor(_node:Node) : this(_node.tree, _node.content) {
        this.dup = true
    }
    override fun getNodeHash() : byteArray{
        if (this.leaf){
            return this.content!!.calculateHash()
        }
        return this.tree.ConcatHash()(this.left.hash, this.right.hash)
    }

    override fun verify(): byteArray {
        if (this.leaf) {
            return this.content!!.calculateHash()
        }
        val l = left.verify()
        val r = right.verify()
        return this.tree.ConcatHash()(l, r) // todo toString
    }

    override fun child(of: Node) {
        this.parent = of
        this.parent?.left = of.left
        this.parent?.right = of.right
        this.parent?.hash = of.hash
    }
}
