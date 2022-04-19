import org.testng.Assert.*


fun mockShaContent(): List<Content>{
    var content = mutableListOf<Content_Sha256>()
    for (item: Int in 1..10) {
        content.add(Content_Sha256(item.toString()))
    }
    return content
}
class MerkleTreeTests {
    fun testVerifyTree(content:List<Content>) {
        var merkleTree = MerkleTree()
        merkleTree.buildFrom(content)
        assertTrue(merkleTree.verifyTree())
    }
    fun testVerifyContent(content:List<Content>) {
        var merkleTree = MerkleTree()
        merkleTree.buildFrom(content)
        for (i in 0 until content.size -1){
            assertTrue(merkleTree.verifyContent(content[i]))
        }
    }
    fun testMerklePath(content:List<Content>) {
        var merkleTree = MerkleTree()
        merkleTree.buildFrom(content)

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
}
class TestSha256{
    var content = mockShaContent()
    
    @org.testng.annotations.Test
    fun testVerify(){
        MerkleTreeTests().testVerifyTree(content)
    }
    @org.testng.annotations.Test
    fun testVerifyContent() {
        MerkleTreeTests().testVerifyContent(content)
    }
    @org.testng.annotations.Test
    fun testMerklePath() {
        MerkleTreeTests().testMerklePath(content)
    }
}
