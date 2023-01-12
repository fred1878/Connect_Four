const val WIN_LENGTH = 4

fun main() {

    println("Connect Four")
    println("First player's name:")
    val firstName = readln()
    println("Second player's name:")
    val secondName = readln()

    val (row, col) = getValidDimensions()

    println("Do you want to play single or multiple games?")
    println("For a single game, input 1 or press Enter")
    println("Input a number of games:")
    val numberOfGames = getNumberOfGames()
    println("$firstName VS $secondName")
    println("$row X $col board")

    gameLoop(row,col,firstName,secondName,numberOfGames)
}

fun gameLoop( row:Int, col:Int, firstName:String, secondName: String, numberOfGames:Int) {
    val board = List(col) { MutableList(row) {' '} }
    var activePlayer = Pair(firstName, 'o')
    printBoard(row, col, board)

    val playedGames = 0
    while (true) {
        println("${activePlayer.first}'s turn:")

        val move = readln()
        if (move == "end") {
            println("Game over!")
            return
        } else if (!isNumber(move)) {
            println("Incorrect column number")
            continue
        } else {
            val colChoice = move.toInt()
            if (colChoice !in 1..col) {
                println("The column number is out of range (1 - $col)")
                continue
            }
            if (board[colChoice - 1].indexOfFirst { it == ' ' } < 0) {
                println("Column $colChoice is full")
                continue
            }

            board[colChoice - 1][board[colChoice - 1].indexOfFirst { it == ' ' }] = activePlayer.second

            printBoard(row, col, board)

            if (board.all { char -> char.all{ c -> c != ' '} }){
                println("It is a draw")
                println("Game Over!")
                break
            }

            if (checkColumnWin(board,activePlayer.second) || checkRowWin(board,activePlayer.second,col) || checkNEDiagonalWin(board,activePlayer.second) || checkNWDiagonalWin(board,activePlayer.second)){
                println("Player ${activePlayer.first} won")
                println("Game Over!")
                break
            }

            activePlayer = toggleActivePlayer(activePlayer.first, firstName, secondName)

        }
    }
}

fun checkColumnWin(board: List<List<Char>>, currentChar:Char):Boolean {
    for (c in board.indices){
        val column = board[c]
        for (r in 0..column.size - WIN_LENGTH){
            val line = listOf(getCell(board,c,r),getCell(board,c,r + 1),getCell(board,c,r + 2),getCell(board,c,r + 3))
            if (line.all { it == currentChar }){
                return true
            }
        }
    }
    return false
}

fun checkRowWin(board: List<List<Char>>, currentChar:Char,col:Int):Boolean {
    for (c in 0 .. col  - WIN_LENGTH){
        val column = board[c]
        for (r in 0 until column.size-1){
            val line = listOf(
                getCell(board,c,r)
                ,getCell(board,c + 1,r)
                ,getCell(board,c + 2,r)
                , getCell(board,c + 3,r))
            if (line.all { it == currentChar }){
                return true
            }
        }
    }
    return false

}

fun checkNEDiagonalWin(board: List<List<Char>>, currentChar:Char):Boolean {
    for (c in 0 .. board.size - WIN_LENGTH){
        val column = board[c]
        for (r in 0 until column.size - WIN_LENGTH){
            val line = listOf(
                getCell(board,c,r)
                ,getCell(board,c + 1,r + 1)
                ,getCell(board,c + 2,r + 2)
                ,getCell(board,c + 3,r + 3))
            if (line.all { it == currentChar }){
                return true
            }
        }
    }
    return false
}

fun checkNWDiagonalWin(board: List<List<Char>>, currentChar:Char):Boolean {
    for (c in WIN_LENGTH - 1 until board.size){
        val column = board[c]
        for (r in 0..column.size - WIN_LENGTH){
            val line = listOf(getCell(board,c,r),getCell(board,c - 1,r + 1),getCell(board,c - 2,r + 2),getCell(board,c - 3,r + 3))
            if (line.all { it == currentChar }){
                return true
            }
        }
    }
    return false
}

fun getCell(board: List<List<Char>>, col: Int, row: Int): Char {
    return board[col][row]
}

fun getNumberOfGames():Int {
    val num = readln().trim()
    if ( num == "" ) {
        return 1
    }
    if (isNumber(num)) {
        val n = num.toInt()
        return if ( n > 0) {
            n
        } else {
            println("Invalid Input")
            getNumberOfGames()
        }
    }
    println("Invalid Input")
    return getNumberOfGames()
}


fun getValidDimensions(): Pair<Int, Int> {
    println("Set the board dimensions (Rows x Columns)")
    println("Press Enter for default (6 x 7)")

    val regex = """\s*(\d)+\s*[xX]\s*(\d)+\s*""".toRegex()
    val boardSize = readln().trim()
    var row = 0
    var col = 0

    if (regex.matches(boardSize)){
        row = boardSize.substring(0,1).toInt()
        col = boardSize.substring(boardSize.length-1,boardSize.length).toInt()
    } else if (boardSize.isEmpty()) {
        row = 6
        col = 7
    } else {
        println("Invalid input")
        return getValidDimensions()
    }

    if (row !in 5..9) {
        println("Board rows should be from 5 to 9")
        return getValidDimensions()
    }

    if (col !in 5..9) {
        println("Board columns should be from 5 to 9")
        return getValidDimensions()
    }

    return Pair(row, col)
}

fun printBoard(rows: Int, cols: Int, board: List<List<Char>>) {
    // header with numbers
    println(" " + (1..cols).joinToString(" "))

    // columns
    repeat (rows) { row ->
        print("║")
        repeat (cols) {col ->
            print("" + board[col].getOrElse(rows - row - 1) {" "} + "║")
        }
        println()
    }

    val bottomRow = CharArray(cols) { '═' }.joinToString("╩")
    println("╚$bottomRow╝")
}

fun toggleActivePlayer(player: String, firstPlayer: String, secondPlayer: String): Pair<String, Char> {
    return if (player == firstPlayer){
        Pair(secondPlayer, '*')
    } else {
        Pair(firstPlayer, 'o')
    }
}

fun isNumber(s: String): Boolean {
    return Regex("\\d+").matches(s)
}