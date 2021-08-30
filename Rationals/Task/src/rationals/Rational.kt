package rationals

import java.lang.IllegalArgumentException
import java.math.BigInteger

class Rational(val numerator: BigInteger, val denominator: BigInteger) : Comparable<Rational> {

    init {
        if (denominator == BigInteger.ZERO) {
            throw IllegalArgumentException("Cannot divide by zero!");
        }
    }

    override fun compareTo(other: Rational): Int {
        // normalize to the same common denominator
        val (left, right) = normalize(this, other)

        return when {
            // greater than (1)
            left.numerator > right.numerator -> 1
            // less than (-1)
            left.numerator < right.numerator -> -1
            // equal to (0)
            else -> 0
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Rational) {
            false
        }
        val (left, right) = normalize(this, other as Rational)
        return left.numerator == right.numerator && left.denominator == right.denominator
    }

    override fun toString(): String {
        val simplified = simplify()
        return if (simplified.denominator == BigInteger.ONE)
            "${simplified.numerator}"
        else
            "${simplified.numerator}/${simplified.denominator}"
    }

    private fun simplify(): Rational {
        val gcd = this.numerator.gcd(this.denominator)
        return if (this.numerator < BigInteger.ZERO && this.denominator < BigInteger.ZERO)
            Rational(this.numerator.abs() / gcd, this.denominator.abs() / gcd)
        else if (this.numerator > BigInteger.ZERO && this.denominator < BigInteger.ZERO)
            Rational(this.numerator.negate() / gcd, this.denominator.abs() / gcd)
        else
            Rational(this.numerator / gcd, this.denominator / gcd)
    }

}

private fun normalize(left: Rational, right: Rational): Pair<Rational, Rational> {
    return Pair(
        Rational(left.numerator * right.denominator, left.denominator * right.denominator),
        Rational(right.numerator * left.denominator, right.denominator * left.denominator)
    )
}

// String extension
fun String.toRational(): Rational {
    val (num: String, den: String) = this.split('/')
    return if (den == "") {
        Rational(this.toBigInteger(), BigInteger.ONE)
    } else {
        Rational(num.toBigInteger(), den.toBigInteger())
    }
}

// a + b
operator fun Rational.plus(other: Rational): Rational {
    val (left, right) = normalize(this, other)
    return Rational(left.numerator + right.numerator, left.denominator)
}

// a - b
operator fun Rational.minus(other: Rational): Rational {
    val (left, right) = normalize(this, other)
    return Rational(left.numerator - right.numerator, left.denominator)
}

// a * b
operator fun Rational.times(other: Rational): Rational {
    return Rational(this.numerator * other.numerator, this.denominator * other.denominator)
}

// a / b
operator fun Rational.div(other: Rational): Rational {
    return Rational(this.numerator * other.denominator, this.denominator * other.numerator)
}

// -a
operator fun Rational.unaryMinus() = Rational(-this.numerator, this.denominator)

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}

// Int extension
private infix fun Int.divBy(i: Int): Rational {
    return Rational(this.toBigInteger(), i.toBigInteger())
}

// Long extension
private infix fun Long.divBy(l: Long): Rational {
    return Rational(this.toBigInteger(), l.toBigInteger())
}

// BigInteger extension
private infix fun BigInteger.divBy(toBigInteger: BigInteger): Any {
    return Rational(this, toBigInteger)
}
