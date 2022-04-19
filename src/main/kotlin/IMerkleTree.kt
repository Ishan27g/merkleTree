// Interface for a merkle tree
interface IMerkleTree {
    fun buildFrom(cs: List<Content>)
    fun rebuildFrom(cs: List<Content>)
    fun rebuild()
    fun verifyTree(): Boolean
    fun verifyContent(c: Content): Boolean
    fun getRootHash(): byteArray
    fun getMerklePath(c: Content): List<MerklePath>?
    fun getTree(): Tree // todo remove
}
typealias byteArray = String

data class MerklePath(val neighbour: byteArray, val position: Position) {}
enum class Position {
    Left, Right
}