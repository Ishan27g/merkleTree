
class Node (_tree:Tree,){
    private var tree: Tree
    private var leaf: Boolean = false
    private var dup: Boolean = false
    private lateinit var content : Content
    lateinit var parent: Node
    lateinit var left:Node
    lateinit var right: Node
    lateinit var hash: byteArray
    init {
        this.tree = _tree
    }
    constructor(_tree:Tree, _content:Content) : this(_tree){
        this.hash = _content.calculateHash()
        this.content = _content
        this.leaf = true
    }
    constructor(_node:Node): this(_node.tree, _node.content){
        this.dup = true
    }
    fun verify() : byteArray{
        if (this.leaf){
            return this.content.calculateHash()
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
