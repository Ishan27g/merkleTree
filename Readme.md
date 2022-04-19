## Merkle Tree in Kotlin

wip

A merkle tree implementation

```kotlin
// implement `Content` interface to describe contents of the merke
sealed interface Content {
    var data: String
    fun calculateHash(): byteArray // todo `typealias byteArray = String`
    override fun toString(): String
}
// Example class implementing the `Content` interface with sha-256 hash strategy
class StrContent(override var data: String) : Content {
    override fun calculateHash() : byteArray{
        // return data
        return MessageDigest
            .getInstance("SHA-256")
            .digest(data.toByteArray())
            .fold("", { str, it -> str + "%02x".format(it) })
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
    for (i in 0 until content.size -1){
        var path = merkleTree.getMerklePath(content[i])
        var hash = merkleTree.getTree().leafs.get(i).getNodeHash()
        if (path != null) {
            for (p in path){
                if (p.position == Position.Left){
                    hash = merkleTree.getTree().ConcatHash()(hash, p.neighbour)
                }else{
                    hash = merkleTree.getTree().ConcatHash()(p.neighbour, hash)
                }
            }
        }
        assertTrue(merkleTree.getRootHash()==hash)
    }
}
```
