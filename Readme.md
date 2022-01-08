## Merkle Tree in Kotlin

A merkle tree implementation

```kotlin
class StrContent(override var data: String) : Content {
    override fun calculateHash() : byteArray{
        return this.data
    }
    override fun equals(to: Content): Boolean{
        return this.data == (to.data)
    }
    override fun toString(): String {
        return this.data
    }
}

fun main() {
    var content = mutableListOf<StrContent>(StrContent("1"), StrContent("2"),StrContent("3"),StrContent("4"),
        StrContent("5"),StrContent("6"),StrContent("7"),StrContent("8"))

    var merkleTree = MerkleTree()
    merkleTree.buildFrom(content)

    println(merkleTree.getRootHash())
    println(merkleTree.verifyTree())
    println(merkleTree.verifyContent(content[1]))

    for (i in 0 until content.size){
        val path = merkleTree.getMerklePath(content[i])!!
        var hash = merkleTree.getTree().leafs.get(i).getNodeHash()
        for (p in path){
            if (p.position == 1){
                hash = merkleTree.getTree().HashFunc()(hash, p.neighbour)
            }else{
                hash = merkleTree.getTree().HashFunc()(p.neighbour, hash)
            }
        }
        if (merkleTree.getRootHash() != hash){
            println("mismatch")
        }
    }
}
```