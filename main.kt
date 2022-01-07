
fun main() {
    var content = mutableListOf<StrContent>()
    content.add(StrContent("1"))
    content.add(StrContent("2"))
    content.add(StrContent("3"))
    content.add(StrContent("41"))
    content.add(StrContent("42"))
    content.add(StrContent("43"))
    content.add(StrContent("44"))
    content.add(StrContent("45"))

    var merkleTree = MerkleTree()
    merkleTree.buildWithContent(content)
    println(merkleTree.getRootHash())

    println(merkleTree.verifyTree())

    var r = MerkleTree()
    r.buildWithContent(content.subList(0,2))
    println(r.getRootHash())

    println(r.verifyTree())


    println(merkleTree.getMerklePath(content[2]))
}