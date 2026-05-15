func main() {
    a := 20
    b := 4
    c := 2
    d := 3

    result := ((a + b) * c) - (a / b) + (d * (c + b))
    comparison := result >= 40

    println(result)
    println(comparison)

    if result >= 40 {
        println(result)

        inner := result / 2
        println(inner)

        if inner != 0 {
            nested := (inner + a) * (b + c)
            println(nested)

            if nested > result {
                println(nested)
            }
        }
    }

    if (a + b) == 24 {
        println(a + b)
    }

    if result != 999 {
        println(123)
    }

    for result < 0 {
        println(result)
    }
}
