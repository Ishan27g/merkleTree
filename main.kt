
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
    println(merkleTree.verifyContent(content[1]))
    println(merkleTree.getMerklePath(content[1]))

    for (i in 0 until content.size){
        val path = merkleTree.getMerklePath(content[i])!!
        var hash = merkleTree.getTree().leafs.get(i).getNodeHash()
        for (p in path){
            if (p.index == 1){
                hash = merkleTree.getTree().HashFunc()(hash, p.neighbour)
            }else{
                hash = merkleTree.getTree().HashFunc()(p.neighbour, hash)
            }
        }
        if (merkleTree.getRootHash() == hash){
            println("WOWOWOWOW")
        }
    }
}