/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the LICENSE file.
 */

package kotlinx.cli

/**
 * Possible types of arguments.
 *
 * Inheritors describe type of argument value. New types can be added by user.
 * In case of options type can have parameter or not.
 */
public abstract class ArgType<T : Any>(public val hasParameter: kotlin.Boolean) {
    /**
     * Text description of type for helpMessage.
     */
    public abstract val description: kotlin.String

    /**
     * Function to convert string argument value to its type.
     * In case of error during conversion also provides help message.
     *
     * @param value value
     */
    public abstract fun convert(value: kotlin.String, name: kotlin.String): T

    /**
     * Argument type for flags that can be only set/unset.
     */
    public object Boolean : ArgType<kotlin.Boolean>(false) {
        override val description: kotlin.String
            get() = ""

        override fun convert(value: kotlin.String, name: kotlin.String): kotlin.Boolean =
            value != "false"
    }

    /**
     * Argument type for string values.
     */
    public object String : ArgType<kotlin.String>(true) {
        override val description: kotlin.String
            get() = "{ String }"

        override fun convert(value: kotlin.String, name: kotlin.String): kotlin.String = value
    }

    /**
     * Argument type for integer values.
     */
    public object Int : ArgType<kotlin.Int>(true) {
        override val description: kotlin.String
            get() = "{ Int }"

        override fun convert(value: kotlin.String, name: kotlin.String): kotlin.Int =
            value.toIntOrNull()
                ?: throw ParsingException("Option $name is expected to be integer number. $value is provided.")
    }

    /**
     * Argument type for double values.
     */
    public object Double : ArgType<kotlin.Double>(true) {
        override val description: kotlin.String
            get() = "{ Double }"

        override fun convert(value: kotlin.String, name: kotlin.String): kotlin.Double =
            value.toDoubleOrNull()
                ?: throw ParsingException("Option $name is expected to be double number. $value is provided.")
    }

    companion {
        /**
         * Helper for arguments that have limited set of possible values represented as enumeration constants.
         */
        public inline fun <reified T : Enum<T>> Choice(
            noinline toVariant: ((kotlin.String) -> T)? = null,
            noinline toString: (T) -> kotlin.String = { it.toString().lowercase() },
        ): Choice<T> {
            return Choice(
                enumValues<T>().toList(),
                toVariant ?: {
                    enumValues<T>().find { e -> toString(e).equals(it, ignoreCase = true) }
                        ?: throw IllegalArgumentException("No enum constant $it")
                },
                toString
            )
        }
    }

    /**
     * Type for arguments that have limited set of possible values.
     */
    public class Choice<T : Any>(
        choices: List<T>,
        public val toVariant: (kotlin.String) -> T,
        public val variantToString: (T) -> kotlin.String = { it.toString() },
    ) : ArgType<T>(true) {
        private val choicesMap: Map<kotlin.String, T> = choices.associateBy { variantToString(it) }

        init {
            require(choicesMap.size == choices.size) {
                "Command line representations of enum choices are not distinct"
            }
        }

        override val description: kotlin.String
            get() {
                return "{ Value should be one of ${choicesMap.keys} }"
            }

        override fun convert(value: kotlin.String, name: kotlin.String): T =
            try {
                toVariant(value)
            } catch (_: Exception) {
                throw ParsingException("Option $name is expected to be one of ${choicesMap.keys}. $value is provided.")
            }
    }
}

public class ParsingException(message: String) : Exception(message)
