## Merkle Tree in Kotlin

wip

A merkle tree implementation

```kotlin
// implement Content interface to describe contents of the merke
sealed interface Content {
    var data: String
    fun calculateHash(): byteArray // todo `typealias byteArray = String`
    fun equals(to: Content) : Boolean
    override fun toString(): String
}
// example (without hash)
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

    // verify merkle path for data
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
            println("path mismatch")
        }
    }
}
```
