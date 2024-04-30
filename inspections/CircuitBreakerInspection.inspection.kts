import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression

@Language("HTML")
val htmlDescription = """
    <html>
    <body>
        Use the circuit breaker pattern when using http client calls
    </body>
    </html>
""".trimIndent()

val message = "Calls to rest client methods must be wrapped by a circuit breaker"

val httpClientWithoutCircuitBreaker = localInspection { psiFile, inspection ->
    val restClientFqn = "org.springframework.web.client.RestClient"
    val circuitBreakerFqn = "io.github.resilience4j.circuitbreaker.CircuitBreaker"
    val isKt = psiFile.name.endsWith(".kt")
    if (!isKt) return@localInspection
    psiFile.descendantsOfType<KtDotQualifiedExpression>().forEach { expression ->
        val isRestClient = analyze(expression.receiverExpression) {
            expression.receiverExpression.getKtType()?.expandedClassSymbol?.getFQN() == restClientFqn
        }
        if (isRestClient) {
            if (!hasCircuitBreakerParent(expression, circuitBreakerFqn)) {
                inspection.registerProblem(expression, message)
            }
        }
    }
}

fun hasCircuitBreakerParent(expression: KtDotQualifiedExpression, circuitBreakerFqn: String): Boolean {
    val parents = mutableListOf<String>()
    var parent = expression.parent
    while (parent != null) {
        if (parent.toString() == "DOT_QUALIFIED_EXPRESSION") {
            val parentExpression = parent as KtDotQualifiedExpression

            val isCircuitBreaker = analyze(parentExpression.receiverExpression) {
                val fqn = parentExpression.receiverExpression.getKtType()?.expandedClassSymbol?.getFQN()
                fqn == circuitBreakerFqn
            }
            parents.add(isCircuitBreaker.toString())
            if (isCircuitBreaker) {
                return true
            }
        }
        parent = parent.parent
    }
    return false
}
emptyList<InspectionKts>()
//
//listOf(
//    InspectionKts(
//        id = "CircuitBreakerInspection", // inspection id (used in qodana.yaml)
//        localTool = httpClientWithoutCircuitBreaker,
//        name = "CircuitBreakerInspection.inspection.kts", // Inspection name, displayed in UI
//        htmlDescription = htmlDescription,
//        level = HighlightDisplayLevel.WARNING,
//    )
//)
