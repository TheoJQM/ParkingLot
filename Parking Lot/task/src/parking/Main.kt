package parking

val registrationRegex = Regex("([A-Z]|[a-z]|[0-9]|-)+")
data class Spot(
    val number: Int,
    var registration: String,
    var color: String,
    var free: Boolean
)


class ParkingLot {
    private val spots = mutableListOf<Spot>()


    fun control() {
        var input = readln().split(" ")
        while (input.first() != "exit") {
            when {
                input.first() == "park" && input.size == 3 -> park(input)
                input.first() == "leave" && input.size == 2 -> leave(input[1].toInt())
                input.first() == "reg_by_color" && input.size == 2 -> regByColor(input[1])
                input.first() == "spot_by_color" && input.size == 2 -> spotByColor(input[1])
                input.first() == "spot_by_reg" && input.size == 2 -> spotByReg(input[1])
                input.first() == "create" && input.size == 2 -> createParking(input[1].toInt())
                input.first() == "status" -> showOccupiedSpots()
                input.first() == "exit" -> break

            }
            input = readln().split(" ")
        }
    }

    private fun createParking(spotsNumber: Int) {
        spots.clear()
        repeat(spotsNumber) {
            spots.add(Spot(it+1, "","",true))
        }
        println("Created a parking lot with $spotsNumber spots.")
    }

    private fun park(car: List<String>) {

        when {
            !registrationRegex.matches(car[1]) -> return
            spots.size == 0 -> println("Sorry, a parking lot has not been created.")
            isFull() -> println("Sorry, the parking lot is full.")
            else ->{
                val spot = getSpot()
                spot.registration = car[1]
                spot.color = car[2]
                spot.free = false
                println("${car[2]} car parked in spot ${spot.number}.")
            }
        }
    }

    private fun isFull(): Boolean {
        return !spots.any { spot ->  spot.free}
    }

    private fun getSpot(): Spot {
        spots.forEach {
            if (it.free) {
                return it
            }
        }
        return Spot(0,"","", false)
    }

    private fun leave(spotNumber: Int) {
        val spot = getSpotByNumber(spotNumber)

        when {
            spots.size == 0 -> println("Sorry, a parking lot has not been created.")
            spot.number == 0 -> return
            spot.free -> println("There is no car in spot $spotNumber.")
            !spot.free -> {
                freeCell(spotNumber)
                println("Spot $spotNumber is free.")
            }
        }
    }

    private fun getSpotByNumber(number: Int): Spot {
        spots.forEach {
            if (it.number == number) return it
        }
        return Spot(0, "","",false)
    }

    private fun freeCell(number: Int) {
        spots.forEach {
            if (it.number == number) {
                it.registration = ""
                it.color = ""
                it.free = true
            }
        }
    }

    private fun regByColor(color: String) {
        if (spots.size == 0) {
            println("Sorry, a parking lot has not been created.")
            return
        }

        val cars = mutableListOf<String>()
        spots.forEach {
            if (it.color.uppercase() == color.uppercase()) cars.add(it.registration)
        }

        if (cars.size == 0) println("No cars with color ${color.uppercase()} were found.")
        else println(cars.joinToString(", "))
    }

    private fun spotByColor(color: String) {
        if (spots.size == 0) {
            println("Sorry, a parking lot has not been created.")
            return
        }

        val cars = mutableListOf<Int>()
        spots.forEach {
            if (it.color.uppercase() == color.uppercase()) cars.add(it.number)
        }

        if (cars.size == 0) println("No cars with color ${color.uppercase()} were found.")
        else println(cars.joinToString(", "))
    }

    private fun spotByReg(reg: String) {
        if (spots.size == 0) {
            println("Sorry, a parking lot has not been created.")
            return
        }

        var carExist = false
        spots.forEach {
            if (it.registration == reg) {
                println(it.number)
                carExist = true
            }
        }

        if (!carExist) println("No cars with registration number $reg were found.")
    }

    private fun showOccupiedSpots() {
        if (spots.size == 0)  {
            println("Sorry, a parking lot has not been created.")
            return
        }
        var occupiedSpots = 0
        for (spot in spots) {
            if (!spot.free) {
                println("""
                    ${spot.number} ${spot.registration} ${spot.color}
                """.trimIndent())
                occupiedSpots++
            }
        }
        if (occupiedSpots == 0) println("Parking lot is empty.")
    }

}

fun main() {
    val parkingLot = ParkingLot()
    parkingLot.control()
}