package griffio.kollchap.demo

import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import java.io.Serializable
import kotlin.random.Random

// simple pseudo random id instead of numeric sequence
fun Random.alphanumeric(): Sequence<Char> {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    return sequence {
        yieldAll( generateSequence { chars[nextInt(chars.length)] })
    }
}

fun entityIdGen() = Random.alphanumeric().take(12).joinToString("")

class EntityIdGenerator: IdentifierGenerator {
    override fun generate(session: SharedSessionContractImplementor, entity: Any): Serializable {
        return entityIdGen()
    }
}
