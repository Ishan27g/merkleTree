import org.testng.Assert.*
import kotlin.test.assertContains

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
    fun testVerifyTree():MerkleTree {
        val merkleTree = MerkleTree()
        merkleTree.buildWithContent(mockContent())
        assertTrue(merkleTree.verifyTree())
        return merkleTree
    }
    @org.testng.annotations.Test
    fun testVerifyContent() {
        val content = mockContent()
        var merkleTree = MerkleTree()
        merkleTree.buildWithContent(content)
        for (i in 0 until content.size -1){
            assertTrue(merkleTree.verifyContent(content[i]))
        }
    }
    @org.testng.annotations.Test
    fun testMerklePath() {
        val content = mockContent()
        var merkleTree = MerkleTree()
        merkleTree.buildWithContent(content)
        for (i in 0 until content.size -1){
            val path = merkleTree.getMerklePath(content[i])
            var hash = merkleTree.getTree().leafs.get(i).getNodeHash()
            if (path != null) {
                for (p in path){
                    if (p.index == 1){
                        hash = merkleTree.getTree().HashFunc()(hash, p.neighbour)
                    }else{
                        hash = merkleTree.getTree().HashFunc()(p.neighbour, hash)
                    }
                }
            }
            assertTrue(merkleTree.getRootHash()==hash)
//            if (merkleTree.getRootHash() == hash){
//                println("WOWOWOWOW")
//            }
        }
    }


}