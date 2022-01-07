

sealed interface Content {
    fun calculateHash(): byteArray
    fun equals(to:StrContent) : Boolean
}
class StrContent(_data : String) : Content {
    private val data: String
    init {
        this.data = _data
    }
    override fun calculateHash() : byteArray{
        return this.data
    }
    override fun equals(to: StrContent): Boolean{
        return this.data.equals(to)
    }
}