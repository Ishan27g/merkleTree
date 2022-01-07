
fun main() {
    var content = mutableListOf<StrContent>()
    content.add(StrContent("1"))
    content.add(StrContent("2"))
    content.add(StrContent("3"))
    content.add(StrContent("4"))
    content.add(StrContent("5"))
    content.add(StrContent("6"))
    content.add(StrContent("7"))
    content.add(StrContent("8"))

    var merkleTree = MerkleTree()
    merkleTree.buildWithContent(content)
    println(merkleTree.getRootHash())
    println(merkleTree.verifyTree())

    var r = MerkleTree()
    r.buildWithContent(content.subList(0,2))
    println(r.getRootHash())
    println(r.verifyTree())


    println(merkleTree.getMerklePath(content[7]))
}