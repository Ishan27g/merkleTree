import java.lang.Exception
import kotlin.collections.mutableListOf

class MerkleTree : IMerkleTree {
    private lateinit var tree: Tree
    override fun buildFrom(cs : List<Content>){
        if (cs.isEmpty()) {
            throw IllegalArgumentException("No contents")
        }
        this.tree = Tree
        val leafs = mutableListOf<Node>()
        for (c in cs){
            leafs.add(Node(this.tree, c))
        }
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
    override fun rebuildFrom(cs : List<Content>){
        buildFrom(cs)
    }

    override fun verifyTree() : Boolean{
        val calculated = this.tree.root.verify()
        return this.tree.rootHash == calculated
    }
    override fun verifyContent(c :Content):Boolean{
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
                    cParent = cParent.parent
                }
                return true
            }
        }
        return false
    }
    override fun getRootHash() : byteArray{
        return this.tree?.rootHash
    }
    override fun getTree() : Tree {return this?.tree}
    override fun rebuild(){
        val cs= mutableListOf<Content>()
        for (l in this.tree?.leafs){
            cs.add(l.content!!)
        }
        buildFrom(cs)
    }

    override fun getMerklePath(c :Content): List<MerklePath>?{
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

}

