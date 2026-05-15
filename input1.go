module m1

func main() {
    ticketPrice := 45
    visitorCount := 6
    serviceFee := 12
    discountLimit := 250

    ticketSubtotal := ticketPrice * visitorCount
    estimatedTotal := ticketSubtotal + serviceFee
    averagePerVisitor := estimatedTotal / visitorCount

    println(ticketSubtotal)
    println(estimatedTotal)
    println(averagePerVisitor)

    if estimatedTotal >= discountLimit {
        discountAmount := estimatedTotal / 10
        finalTotal := estimatedTotal - discountAmount
        println(discountAmount)
        println(finalTotal)
    }

    if averagePerVisitor < ticketPrice {
        println(averagePerVisitor)
    }

    m2.printBudgetReport()
    m2.printSafetyReport()

    for estimatedTotal < 0 {
        println(estimatedTotal)
    }
}
