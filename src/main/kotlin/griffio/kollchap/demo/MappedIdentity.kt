package griffio.kollchap.demo

import jakarta.persistence.*
import org.hibernate.annotations.IdGeneratorType
import org.springframework.data.domain.Persistable
import org.springframework.data.util.ProxyUtils
import org.springframework.lang.Nullable
import java.io.Serializable

import java.util.*

@IdGeneratorType(EntityIdGenerator::class)
@Retention(AnnotationRetention.RUNTIME) @Target(AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
annotation class EntityGeneratedId

@MappedSuperclass
abstract class MappedIdentity<PK : Serializable> : Persistable<PK> {
    @Id
    @GeneratedValue(generator = "entity_id")
    @EntityGeneratedId
    @Nullable
    private var id: PK? = null

    @Nullable
    override fun getId(): PK? {
        return id
    }

    protected fun setId(@Nullable id: PK) {
        this.id = id
    }

    /**
     * Must be [Transient] in order to ensure that no JPA provider complains because of a missing setter.
     *
     * @see org.springframework.data.domain.Persistable.isNew
     */
    @Transient // DATAJPA-622
    override fun isNew(): Boolean {
        return null == getId()
    }

    override fun toString(): String {
        return String.format(Locale.US, "Entity of type %s with id: %s", this.javaClass.name, getId())
    }

    override fun equals(other: Any?): Boolean {
        if (null == other) {
            return false
        }
        if (this === other) {
            return true
        }
        if (javaClass != ProxyUtils.getUserClass(other)) {
            return false
        }
        val that = other as MappedIdentity<*>
        return if (null == getId()) false else getId() == that.getId()
    }

    override fun hashCode(): Int {
        var hashCode = 17
        hashCode += if (null == getId()) 0 else getId().hashCode() * 31
        return hashCode
    }
}
