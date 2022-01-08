import org.testng.Assert.*

fun mockContent(): List<Content>{
    var content = mutableListOf<StrContent>()
    content.add(StrContent("1"))
    content.add(StrContent("2"))
    content.add(StrContent("3"))
    content.add(StrContent("4"))
    content.add(StrContent("5"))
    content.add(StrContent("6"))
    content.add(StrContent("7"))
    content.add(StrContent("8"))
    return content
}

class MerkleTreeTest {

    @org.testng.annotations.Test
    fun testVerifyTree() {
        val merkleTree = MerkleTree()
        val content = mockContent()
        merkleTree.buildFrom(content)
        assertTrue(merkleTree.verifyTree())
    }
    @org.testng.annotations.Test
    fun testVerifyContent() {
        val content = mockContent()
        var merkleTree = MerkleTree()
        merkleTree.buildFrom(content)
        for (i in 0 until content.size -1){
            assertTrue(merkleTree.verifyContent(content[i]))
        }
    }
    @org.testng.annotations.Test
    fun testMerklePath() {
        val content = mockContent()
        var merkleTree = MerkleTree()
        merkleTree.buildFrom(content)
        for (i in 0 until content.size -1){
            val path = merkleTree.getMerklePath(content[i])
            var hash = merkleTree.getTree().leafs.get(i).getNodeHash()
            if (path != null) {
                for (p in path){
                    if (p.position == 1){
                        hash = merkleTree.getTree().HashFunc()(hash, p.neighbour)
                    }else{
                        hash = merkleTree.getTree().HashFunc()(p.neighbour, hash)
                    }
                }
            }
            assertTrue(merkleTree.getRootHash()==hash)
        }
    }
}