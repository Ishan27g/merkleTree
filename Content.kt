sealed interface Content {
    var data: String
    fun calculateHash(): byteArray
    fun equals(to: Content) : Boolean
    override fun toString(): String
}
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