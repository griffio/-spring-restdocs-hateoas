package griffio.kollchap.demo

import org.springframework.data.domain.Persistable
import org.springframework.data.util.ProxyUtils
import org.springframework.lang.Nullable
import java.io.Serializable
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.Transient

@MappedSuperclass
abstract class MappedPersistable<PK : Serializable> : Persistable<PK> {
    @Id
    @GeneratedValue()
    @Nullable
    private var id: PK? = null

    @Nullable
    override fun getId(): PK? {
        return id
    }

    /**
     * Sets the id of the entity.
     *
     * @param id the id to set
     */
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

    /*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
    override fun toString(): String {
        return String.format("Entity of type %s with id: %s", this.javaClass.name, getId())
    }

    /*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
        val that = other as MappedPersistable<*>
        return if (null == getId()) false else getId() == that.getId()
    }

    /*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
    override fun hashCode(): Int {
        var hashCode = 17
        hashCode += if (null == getId()) 0 else getId().hashCode() * 31
        return hashCode
    }
}
