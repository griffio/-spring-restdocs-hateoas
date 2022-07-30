package griffio.kollchap.demo

import org.springframework.restdocs.constraints.ConstraintDescriptions
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.snippet.Attributes

/**
 * Utility for extracting javax.validator annotations into a restdocs "constraints" custom attribute
 * see resources/org/springframework/restdocs/templates/request-fields.snippet
 */
class ConstrainedFields constructor(input: Class<*>) {
    private val constraintDescriptions = ConstraintDescriptions(input)

    fun withPath(path: String): FieldDescriptor {
        return PayloadDocumentation.fieldWithPath(path).attributes(
            Attributes.key("constraints").value(
                constraintDescriptions.descriptionsForProperty(path).joinToString(". ")
            )
        )
    }
    fun subsectionWithPath(path: String): FieldDescriptor {
        return PayloadDocumentation.subsectionWithPath(path).attributes(
            Attributes.key("constraints").value(
                constraintDescriptions.descriptionsForProperty(path).joinToString(". ")
            )
        )
    }
}
