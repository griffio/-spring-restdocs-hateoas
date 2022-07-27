package griffio.kollchap.demo

import org.springframework.restdocs.constraints.ConstraintDescriptions
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.snippet.Attributes

class ConstrainedFields constructor(input: Class<*>) {
    private val constraintDescriptions = ConstraintDescriptions(input)

    fun withPath(path: String): FieldDescriptor {
        return PayloadDocumentation.fieldWithPath(path).attributes(
            Attributes.key("constraints").value(
                constraintDescriptions.descriptionsForProperty(path).joinToString(". ")
            )
        )
    }
}
