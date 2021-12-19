import kotlin.math.abs

fun main() {

    data class Point(
        val x: Int,
        val y: Int,
        val z: Int
    )

    fun List<String>.parse(): List<List<Point>> {
        val scanners = mutableListOf<List<Point>>()

        val points = mutableListOf<Point>()
        forEach { line ->
            if (line.contains(',')) {
                val (x, y, z) = line.split(',').map { it.toInt() }
                points.add(Point(x, y, z))
            } else if (points.isNotEmpty()) {
                scanners.add(points.toList())
                points.clear()
            }
        }
        scanners.add(points.toList())

        return scanners
    }

    fun List<Point>.transform() = listOf(
        map { Point(it.x, it.y, it.z) },
        map { Point(it.x, it.y, -it.z) },
        map { Point(it.x, -it.y, it.z) },
        map { Point(it.x, -it.y, -it.z) },
        map { Point(-it.x, it.y, it.z) },
        map { Point(-it.x, it.y, -it.z) },
        map { Point(-it.x, -it.y, it.z) },
        map { Point(-it.x, -it.y, -it.z) },

        map { Point(it.x, it.z, it.y) },
        map { Point(it.x, -it.z, it.y) },
        map { Point(it.x, it.z, -it.y) },
        map { Point(it.x, -it.z, -it.y) },
        map { Point(-it.x, it.z, it.y) },
        map { Point(-it.x, -it.z, it.y) },
        map { Point(-it.x, it.z, -it.y) },
        map { Point(-it.x, -it.z, -it.y) },

        map { Point(it.y, it.z, it.x) },
        map { Point(it.y, -it.z, it.x) },
        map { Point(-it.y, it.z, it.x) },
        map { Point(-it.y, -it.z, it.x) },
        map { Point(it.y, it.z, -it.x) },
        map { Point(it.y, -it.z, -it.x) },
        map { Point(-it.y, it.z, -it.x) },
        map { Point(-it.y, -it.z, -it.x) },

        map { Point(it.y, it.x, it.z) },
        map { Point(it.y, it.x, -it.z) },
        map { Point(-it.y, it.x, it.z) },
        map { Point(-it.y, it.x, -it.z) },
        map { Point(it.y, -it.x, it.z) },
        map { Point(it.y, -it.x, -it.z) },
        map { Point(-it.y, -it.x, it.z) },
        map { Point(-it.y, -it.x, -it.z) },

        map { Point(it.z, it.x, it.y) },
        map { Point(-it.z, it.x, it.y) },
        map { Point(it.z, it.x, -it.y) },
        map { Point(-it.z, it.x, -it.y) },
        map { Point(it.z, -it.x, it.y) },
        map { Point(-it.z, -it.x, it.y) },
        map { Point(it.z, -it.x, -it.y) },
        map { Point(-it.z, -it.x, -it.y) },

        map { Point(it.z, it.y, it.x) },
        map { Point(-it.z, it.y, it.x) },
        map { Point(it.z, -it.y, it.x) },
        map { Point(-it.z, -it.y, it.x) },
        map { Point(it.z, it.y, -it.x) },
        map { Point(-it.z, it.y, -it.x) },
        map { Point(it.z, -it.y, -it.x) },
        map { Point(-it.z, -it.y, -it.x) },
    )

    fun List<Point>.adjust(point: Point) = map { Point(it.x - point.x, it.y - point.y, it.z - point.z) }

    fun List<Point>.overlap(scanner: List<Point>): Pair<Point, List<Point>>? {
        scanner.transform().forEach { transformedScanner ->
            forEach { firstPoint ->
                val adjustedFirstScanner = adjust(firstPoint)
                transformedScanner.forEach { secondPoint ->
                    val adjustedTransformedScanner = transformedScanner.adjust(secondPoint)
                    val intersections = adjustedTransformedScanner.intersect(adjustedFirstScanner)
                    if (intersections.size >= 12) {
                        val adjustScannerPoint = Point(
                            secondPoint.x - firstPoint.x,
                            secondPoint.y - firstPoint.y,
                            secondPoint.z - firstPoint.z
                        )
                        val adjustedScanner = transformedScanner.map {
                            Point(
                                x = it.x - secondPoint.x + firstPoint.x,
                                y = it.y - secondPoint.y + firstPoint.y,
                                z = it.z - secondPoint.z + firstPoint.z
                            )
                        }
                        return adjustScannerPoint to adjustedScanner
                    }
                }
            }
        }
        return null
    }

    fun List<List<Point>>.solve(): Pair<List<Point>, List<Point>> {
        val origins = mutableSetOf<Point>()
        val mergedScanner = mutableSetOf<Point>()
        mergedScanner.addAll(first())
        var scanners = this.drop(1)

        while (scanners.isNotEmpty()) {
            scanners = scanners.mapNotNull { scanner ->
                val (adjustedOrigin, adjustedScanner) = mergedScanner.toList().overlap(scanner) ?: return@mapNotNull scanner
                mergedScanner.addAll(adjustedScanner)
                origins.add(adjustedOrigin)
                null
            }
        }

        return origins.toList() to mergedScanner.toList()
    }

    fun part1(
        input: List<String>
    ) = input.parse().solve().let { (_, scanner) -> scanner.count() }

    fun part2(
        input: List<String>
    ) = input.parse().solve().let { (origins, _) ->
        origins.map { firstPoint ->
            origins.map { secondPoint ->
                abs(firstPoint.x - secondPoint.x) + abs(firstPoint.y - secondPoint.y) + abs(firstPoint.z - secondPoint.z)
            }
        }.flatten().maxOf { it }
    }

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}
