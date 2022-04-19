import java.security.MessageDigest

// Generic interface to describe contents of the merke
sealed interface Content {
    var data: String
    fun calculateHash(): byteArray
    override fun toString(): String
}

// Example class with sha-256 hash strategy
class Content_Sha256(override var data: String) : Content {
    override fun calculateHash() : byteArray{
        return MessageDigest
            .getInstance("SHA-256")
            .digest(data.toByteArray())
            .fold("", { str, it -> str + "%02x".format(it) })
    }
    override fun toString(): String {
        return this.data
    }
}