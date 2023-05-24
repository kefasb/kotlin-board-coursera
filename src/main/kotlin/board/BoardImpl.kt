package board

fun createSquareBoard(width: Int): SquareBoard =
    object : SquareBoard {
        override val width: Int
            get() = width

        val cells = Array(width, { i -> Array(width, { j -> Cell(i + 1, j + 1) }) })

        override fun getCellOrNull(i: Int, j: Int): Cell? {
            return cells.getOrNull(i - 1)?.getOrNull(j - 1)
        }

        override fun getCell(i: Int, j: Int): Cell {
            val cellOrNull = getCellOrNull(i, j)
            require(cellOrNull != null) { "Indexes out of range" }
            return cellOrNull
        }

        override fun getAllCells(): Collection<Cell> {
            return cells
                .toList()
                .flatMap { row -> row.toList() }
        }

        override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
            return jRange.map { j -> getCellOrNull(i, j) }.filterNotNull()
        }

        override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
            return iRange.map { i -> getCellOrNull(i, j) }.filterNotNull()
        }

        override fun Cell.getNeighbour(direction: Direction): Cell? {
            val (neighbourI, neighbourJ) = when (direction) {
                Direction.UP -> (i - 1) to j
                Direction.DOWN -> (i + 1) to j
                Direction.RIGHT -> i to (j + 1)
                Direction.LEFT -> i to (j - 1)
            }
            return getCellOrNull(neighbourI, neighbourJ)
        }
    }

fun <T> createGameBoard(width: Int): GameBoard<T> =
    object : GameBoard<T>, SquareBoard by createSquareBoard(width) {
        private val cellToValue = getAllCells().map { it to null }.toMap(mutableMapOf<Cell, T?>())

        override fun get(cell: Cell): T? {
            return cellToValue[cell]
        }

        override fun all(predicate: (T?) -> Boolean): Boolean {
            return cellToValue.values.all(predicate)
        }

        override fun any(predicate: (T?) -> Boolean): Boolean {
            return cellToValue.values.any(predicate)
        }

        /**
         * Could be simply
         *
         * `return filter(predicate).firstOrNull()`
         *
         * However, if some implementation of [Map.toList] is lazy then the laziness would be lost.
         */
        override fun find(predicate: (T?) -> Boolean): Cell? {
            return cellToValue.toList().find { (_, value) -> predicate(value) }?.first
        }

        override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
            return cellToValue.filterValues(predicate).keys
        }

        override fun set(cell: Cell, value: T?) {
            cellToValue[cell] = value
        }

    }


